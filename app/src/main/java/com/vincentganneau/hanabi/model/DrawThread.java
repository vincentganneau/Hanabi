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
 * {@link GameThread} subclass that continuously renders the game objects.
 * @author Vincent Ganneau
 */
public class DrawThread extends GameThread {

    // Frames per second
    /**
     * The minimal number of milliseconds between updates to ensure the wanted number of frames per second.
     */
    public long mMinElapsedMillis;

    // Constructor
    /**
     * Creates a new {@link DrawThread}.
     * @param gameEngine the {@link GameEngine} instance.
     * @param wantedFramesPerSecond the wanted number of frames per second.
     */
    public DrawThread(GameEngine gameEngine, int wantedFramesPerSecond) {
        super(gameEngine);
        mMinElapsedMillis = 1000 / wantedFramesPerSecond;
    }

    // Game loop
    @Override
    public void update(long elapsedMillis) {
        if (elapsedMillis < mMinElapsedMillis) {
            try {
                Thread.sleep(mMinElapsedMillis - elapsedMillis);
            } catch (InterruptedException e) {
                // We just continue.
            }
        }
        mGameEngine.drawGame();
    }
}
