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

/**
 * Abstract class for all game objects.
 * @author Vincent Ganneau
 */
public abstract class GameObject {

    // Callbacks
    /**
     * Callback invoked to initialize the object before the game can start.
     */
    public abstract void onGameStart();

    /**
     * Callback invoked to update the object.
     * <p>
     * The game engine should call this method as fast as possible.
     * </p>
     * @param elapsedMillis the number of milliseconds that have passed since the previous call.
     * @param gameEngine the {@link GameEngine} instance.
     */
    public abstract void onUpdate(long elapsedMillis, GameEngine gameEngine);

    /**
     * Callback invoked to render the object.
     */
    public abstract void onDraw();

    /**
     * Callback invoked when the object is added to the game.
     */
    public void onAddedToGameUiThread() { }

    /**
     * Callback invoked when the object is removed from the game.
     */
    public void onRemovedFromGameUiThread() { }

    // Runnables
    /**
     * The {@link Runnable} that will be run inside the UI thread when the object is added to the game.
     * @see android.app.Activity#runOnUiThread(Runnable)
     */
    public final Runnable mOnAddedRunnable = () -> onAddedToGameUiThread();

    /**
     * The {@link Runnable} that will be run inside the UI thread when the object is removed from the game.
     * @see android.app.Activity#runOnUiThread(Runnable)
     */
    public final Runnable mOnRemovedRunnable = () -> onRemovedFromGameUiThread();
}
