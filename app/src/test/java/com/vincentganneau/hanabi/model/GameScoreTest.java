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

import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Tests the {@link GameScore} class.
 * @author Vincent Ganneau
 */
@RunWith(JUnit4.class)
public class GameScoreTest {

    // Dependencies
    @Mock
    private TextView mTextView;

    // Game score
    private GameScore mGameScore;

    // Random number generator
    private final Random mRandom = new Random();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mGameScore = spy(new GameScore(mTextView));
    }

    /**
     * Tests the {@link GameScore} constructor.
     */
    @Test
    public void testConstructor() {
        assertEquals(mTextView, mGameScore.mTextView);
    }

    /**
     * Tests the {@link GameScore#onGameStart()} callback.
     */
    @Test
    public void testGameStart() {
        // Given
        mGameScore.mTotalMillis = mRandom.nextLong();

        // When
        mGameScore.onGameStart();

        // Then
        assertEquals(0, mGameScore.mTotalMillis);
    }

    /**
     * Tests the {@link GameScore#onUpdate(long, GameEngine)} callback.
     */
    @Test
    public void testUpdate() {
        // Given
        final GameEngine gameEngine = mock(GameEngine.class);
        final long millis = mRandom.nextLong();
        final long elapsedMillis = mRandom.nextLong();
        mGameScore.mTotalMillis = millis;

        // When
        mGameScore.onUpdate(elapsedMillis, gameEngine);

        // Then
        assertEquals(millis + elapsedMillis, mGameScore.mTotalMillis);
    }

    /**
     * Tests the {@link GameScore#onDraw()} callback.
     */
    @Test
    public void testDraw() {
        // Given
        mGameScore.mTotalMillis = mRandom.nextLong();

        // When
        mGameScore.onDraw();

        // Then
        verify(mTextView).setText(String.valueOf(mGameScore.mTotalMillis));
    }
}