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
import android.widget.TextView;

/**
 * {@link GameObject} subclass that displays the game score.
 * @author Vincent Ganneau
 */
public class GameScore extends GameObject {

    // Views
    /**
     * The text view that displays the game score.
     */
    @VisibleForTesting
    public final TextView mTextView;

    // States
    /**
     * The total number of milliseconds that have passed.
     */
    @VisibleForTesting
    public long mTotalMillis;

    // Constructor
    /**
     * Creates a new {@link GameScore} object.
     * @param textView the {@link TextView} that displays the game score.
     */
    public GameScore(TextView textView) {
        mTextView = textView;
    }

    @Override
    public void onGameStart() {
        mTotalMillis = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mTotalMillis += elapsedMillis;
    }

    @Override
    public void onDraw() {
        mTextView.setText(String.valueOf(mTotalMillis));
    }
}
