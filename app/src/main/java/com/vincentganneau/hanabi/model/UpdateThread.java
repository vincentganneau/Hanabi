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
 * {@link GameThread} subclass that continuously runs updates on the game engine.
 * @author Vincent Ganneau
 */
public class UpdateThread extends GameThread {

    // Constructor
    /**
     * Creates a new {@link UpdateThread}.
     * @param gameEngine the {@link GameEngine} instance.
     */
    public UpdateThread(GameEngine gameEngine) {
        super(gameEngine);
    }

    // Game loop
    @Override
    public void update(long elapsedMillis) {
        // Update the game
        mGameEngine.updateGame(elapsedMillis);
    }
}
