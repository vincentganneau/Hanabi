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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link GameEngine} class.
 * @author Vincent Ganneau
 */
@RunWith(JUnit4.class)
public class GameEngineTest {

    // Dependencies
    @Mock
    private Activity mActivity;

    // Game engine
    private GameEngine mGameEngine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mGameEngine = spy(new GameEngine(mActivity));
    }

    /**
     * Tests the {@link GameEngine} constructor.
     */
    @Test
    public void testConstructor() {
        assertEquals(mActivity, mGameEngine.mActivity);
        assertTrue(mGameEngine.mGameObjects.isEmpty());
        assertTrue(mGameEngine.mGameObjectsToAdd.isEmpty());
        assertTrue(mGameEngine.mGameObjectsToRemove.isEmpty());
        assertNull(mGameEngine.mUpdateThread);
        assertNull(mGameEngine.mDrawThread);
        assertFalse(mGameEngine.isGameRunning());
    }

    /**
     * Tests the entire game lifecycle.
     */
    @Test
    public void testGameLifecycle() throws InterruptedException {
        // Given
        final GameObject gameObject = spy(new DummyGameObject());

        // When
        when(mGameEngine.isGameRunning()).thenReturn(true);
        mGameEngine.addGameObject(gameObject);

        // Then
        assertEquals(1, mGameEngine.mGameObjectsToAdd.size());
        verify(mActivity).runOnUiThread(gameObject.mOnAddedRunnable);

        // When
        when(mGameEngine.isGameRunning()).thenCallRealMethod();
        mGameEngine.addGameObject(gameObject);

        // Then
        assertEquals(1, mGameEngine.mGameObjects.size());

        // When
        mGameEngine.startGame();

        // Then
        verify(mGameEngine).stopGame();
        verify(gameObject).onGameStart();
        assertNotNull(mGameEngine.mUpdateThread);
        assertNotNull(mGameEngine.mDrawThread);
        assertTrue(mGameEngine.isGameRunning());

        // When
        mGameEngine.updateGame(1000 / 60);
        Thread.sleep(200);

        // Then
        assertEquals(2, mGameEngine.mGameObjects.size());
        assertTrue(mGameEngine.mGameObjectsToAdd.isEmpty());
        verify(gameObject, atLeastOnce()).onUpdate(1000 / 60, mGameEngine);

        // When
        doAnswer(invocation -> {
            mGameEngine.mDrawRunnable.run();
            return null;
        }).when(mActivity).runOnUiThread(mGameEngine.mDrawRunnable);
        mGameEngine.drawGame();
        Thread.sleep(200);

        // Then
        verify(mActivity, atLeastOnce()).runOnUiThread(mGameEngine.mDrawRunnable);
        verify(gameObject, atLeastOnce()).onDraw();

        // When
        mGameEngine.removeGameObject(gameObject);

        // Then
        verify(mActivity).runOnUiThread(gameObject.mOnRemovedRunnable);

        // When
        mGameEngine.stopGame();

        // Then
        assertFalse(mGameEngine.isGameRunning());
    }

    /**
     * Dummy {@link GameObject} subclass.
     */
    private class DummyGameObject extends GameObject {

        @Override
        public void onGameStart() {

        }

        @Override
        public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

        }

        @Override
        public void onDraw() {

        }
    }
}