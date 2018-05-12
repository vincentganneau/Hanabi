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

import android.support.annotation.VisibleForTesting;

/**
 * Abstract {@link Thread} subclass for game threads.
 * @author Vincent Ganneau
 */
public abstract class GameThread extends Thread {

    // Dependencies
    /**
     * The game engine.
     */
    protected final GameEngine mGameEngine;

    // States
    /**
     * Indicates whether the game is running.
     */
    public volatile boolean mGameRunning;
    /**
     * Indicates whether the game is paused.
     */
    public volatile boolean mGamePaused;

    // Lock
    /**
     * The lock for the thread to wait until the game is resumed.
     */
    protected final Object mGamePausedLock = new Object();

    // Constructor
    /**
     * Creates a new {@link GameThread}.
     * @param gameEngine the {@link GameEngine} instance.
     */
    protected GameThread(GameEngine gameEngine) {
        super();
        mGameEngine = gameEngine;
    }

    // Game loop
    /**
     * Fires an update inside the game loop.
     * @param elapsedMillis the number of milliseconds that have passed since the previous call.
     */
    public abstract void update(long elapsedMillis);

    @Override
    public void run() {
        long previousTimeMillis = System.currentTimeMillis();
        long currentTimeMillis;
        long elapsedMillis;
        while (mGameRunning) {
            // Get the current time
            currentTimeMillis = System.currentTimeMillis();

            // Calculate the elapsed milliseconds since the previous run
            elapsedMillis = currentTimeMillis - previousTimeMillis;

            // Handle pause
            if (mGamePaused) {
                while (mGamePaused) {
                    try {
                        waitGamePausedLock();
                    } catch (InterruptedException e) {
                        // Stay on the loop
                    }
                }
                currentTimeMillis = System.currentTimeMillis();
            }

            // Fire update
            update(elapsedMillis);

            // Store the current time
            previousTimeMillis = currentTimeMillis;
        }
    }

    // Game lifecycle
    /**
     * Starts the game.
     */
    public void startGame() {
        mGameRunning = true;
        mGamePaused = false;
        start();
    }

    /**
     * Resumes the game.
     */
    public void resumeGame() {
        if (mGamePaused) {
            mGamePaused = false;
            notifyGamePausedLock();
        }
    }

    /**
     * Pauses the game.
     */
    public void pauseGame() {
        mGamePaused = true;
    }

    /**
     * Stops the game.
     */
    public void stopGame() {
        mGameRunning = false;
        resumeGame();
    }

    // Lock methods
    /**
     * Calls {@link Object#wait()} on the lock.
     */
    @VisibleForTesting
    public void waitGamePausedLock() throws InterruptedException {
        synchronized (mGamePausedLock) {
            mGamePausedLock.wait();
        }
    }

    /**
     * Calls {@link Object#notify()} on the lock.
     */
    @VisibleForTesting
    public void notifyGamePausedLock() {
        synchronized (mGamePausedLock) {
            mGamePausedLock.notify();
        }
    }
}
