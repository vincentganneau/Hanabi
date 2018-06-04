/*__________________________________________________________________________

    Copyright (C) 2018 Vincent Ganneau

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
  __________________________________________________________________________*/

package com.vincentganneau.hanabi.model;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * The game engine.
 * @author Vincent Ganneau
 */
public class GameEngine {

    // Activity
    /**
     * The {@link Activity} the game is running in.
     */
    @VisibleForTesting
    public final Activity mActivity;

    // Game objects
    /**
     * The game objects.
     */
    @VisibleForTesting
    public final List<GameObject> mGameObjects = new ArrayList<>();
    /**
     * The game objects to be added to the game.
     */
    @VisibleForTesting
    public final List<GameObject> mGameObjectsToAdd = new ArrayList<>();
    /**
     * The game objects to be removed from the game.
     */
    @VisibleForTesting
    public final List<GameObject> mGameObjectsToRemove = new ArrayList<>();

    // Threads
    /**
     * The thread that will update the game objects.
     */
    @VisibleForTesting
    public UpdateThread mUpdateThread;
    /**
     * The thread that will render the game objects.
     */
    @VisibleForTesting
    public DrawThread mDrawThread;

    // Runnables
    /**
     * The {@link Runnable} that will be run inside the UI thread to render the game objects.
     * @see Activity#runOnUiThread(Runnable)
     */
    @VisibleForTesting
    public final Runnable mDrawRunnable = () -> {
        synchronized (mGameObjects) {
            final int count = mGameObjects.size();
            for (int i = 0; i < count; i++) {
                mGameObjects.get(i).onDraw();
            }
        }
    };

    // Constructor
    /**
     * Creates a new {@link GameEngine}.
     * @param activity the {@link Activity} the game is running in.
     */
    public GameEngine(Activity activity) {
        mActivity = activity;
    }

    // Getters
    /**
     * Indicates whether the game is running.
     * @return <code>true</code> if the game is running, <code>false</code> otherwise.
     */
    public boolean isGameRunning() {
        return mUpdateThread != null && mUpdateThread.mGameRunning;
    }

    /**
     * Indicates whether the game is paused.
     * @return <code>true</code> if the game is paused, <code>false</code> otherwise.
     */
    public boolean isGamePaused() {
        return mUpdateThread != null && mUpdateThread.mGamePaused;
    }

    // Game lifecycle
    /**
     * Starts the game.
     */
    public void startGame() {
        // Stop the game if it is already running
        stopGame();

        // Initialize the game objects
        final int count = mGameObjects.size();
        for (int i = 0; i < count; i++) {
            mGameObjects.get(i).onGameStart();
        }

        // Start the update thread
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.startGame();

        // Start the drawing thread
        mDrawThread = new DrawThread(this, 60);
        mDrawThread.startGame();
    }

    /**
     * Resumes the game.
     */
    public void resumeGame() {
        mUpdateThread.resumeGame();
        mDrawThread.resumeGame();
    }

    /**
     * Updates the game.
     * @param elapsedMillis the number of milliseconds that have passed since the previous call.
     */
    public void updateGame(long elapsedMillis) {
        // Update the game objects
        final int count = mGameObjects.size();
        for (int i = 0; i < count; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
        }

        // Handle objects that must be removed or added
        synchronized (mGameObjects) {
            while (!mGameObjectsToRemove.isEmpty()) {
                mGameObjects.remove(mGameObjectsToRemove.remove(0));
            }
            while (!mGameObjectsToAdd.isEmpty()) {
                mGameObjects.add(mGameObjectsToAdd.remove(0));
            }
        }
    }

    /**
     * Renders the game objects.
     */
    public void drawGame() {
        mActivity.runOnUiThread(mDrawRunnable);
    }

    /**
     * Pauses the game.
     */
    public void pauseGame() {
        mUpdateThread.pauseGame();
        mDrawThread.pauseGame();
    }

    /**
     * Stops the game.
     */
    public void stopGame() {
        if (mUpdateThread != null) {
            mUpdateThread.stopGame();
        }
        if (mDrawThread != null) {
            mDrawThread.stopGame();
        }
    }

    /**
     * Adds a game object to the game.
     * @param gameObject the game object to be added.
     */
    public void addGameObject(final GameObject gameObject) {
        if (isGameRunning()) {
            mGameObjectsToAdd.add(gameObject);
        } else {
            mGameObjects.add(gameObject);
        }
        mActivity.runOnUiThread(gameObject.mOnAddedRunnable);
    }

    /**
     * Removes a game object from the game.
     * @param gameObject the game object to be removed.
     */
    public void removeGameObject(final GameObject gameObject) {
        mGameObjectsToRemove.add(gameObject);
        mActivity.runOnUiThread(gameObject.mOnRemovedRunnable);
    }
}
