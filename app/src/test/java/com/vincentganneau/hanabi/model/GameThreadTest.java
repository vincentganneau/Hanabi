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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests the {@link GameThread} subclasses.
 * @author Vincent Ganneau
 */
@RunWith(JUnit4.class)
public abstract class GameThreadTest {

    // Dependencies
    @Mock
    protected GameEngine mGameEngine;

    // Game thread
    protected GameThread mGameThread;

    @Before
    public void setUp() throws InterruptedException {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Verifies the {@link GameThread#update(long)} is never called.
     */
    protected abstract void verifyNeverUpdate();

    /**
     * Verifies the {@link GameThread#update(long)} is called at least once.
     */
    protected abstract void verifyAtLeastOnceUpdate();

    /**
     * Tests the {@link GameThread} constructor.
     */
    @Test
    public void testConstructor() {
        assertEquals(mGameEngine, mGameThread.mGameEngine);
        assertFalse(mGameThread.mGameRunning);
        assertFalse(mGameThread.mGamePaused);
    }

    /**
     * Tests the {@link GameThread#startGame()} method.
     */
    @Test
    public void testStartGame() {
        // When
        mGameThread.startGame();

        // Then
        assertTrue(mGameThread.mGameRunning);
        assertFalse(mGameThread.mGamePaused);
        verify(mGameThread).start();
    }

    /**
     * Tests the {@link GameThread#resumeGame()} method when the game is not paused.
     */
    @Test
    public void testResumeUnpausedGame() {
        // When
        mGameThread.resumeGame();

        // Then
        verify(mGameThread, never()).notifyGamePausedLock();
    }

    /**
     * Tests the {@link GameThread#resumeGame()} method when the game is paused.
     */
    @Test
    public void testResumePausedGame() {
        // Given
        mGameThread.mGamePaused = true;

        // When
        mGameThread.resumeGame();

        // Then
        assertFalse(mGameThread.mGamePaused);
        verify(mGameThread).notifyGamePausedLock();
    }

    /**
     * Tests the {@link GameThread#pauseGame()} method.
     */
    @Test
    public void testPauseGame() {
        // When
        mGameThread.pauseGame();

        // Then
        assertTrue(mGameThread.mGamePaused);
    }

    /**
     * Tests the {@link GameThread#stopGame()} method.
     */
    @Test
    public void testStopGame() {
        // Given
        mGameThread.mGameRunning = true;

        // When
        mGameThread.stopGame();

        // Then
        assertFalse(mGameThread.mGameRunning);
        verify(mGameThread).resumeGame();
    }

    /**
     * Tests the {@link GameThread#run()} method when game has not been started.
     */
    @Test
    public void testNonRunningGame() {
        // When
        mGameThread.start();

        // Then
        verify(mGameThread, never()).update(anyLong());
        verifyNeverUpdate();
    }

    /**
     * Tests the {@link GameThread#run()} method when game is started.
     */
    @Test
    public void testRunningGame() throws InterruptedException {
        // When
        mGameThread.startGame();
        Thread.sleep(200);

        // Then
        verify(mGameThread, atLeastOnce()).update(anyLong());
        verifyAtLeastOnceUpdate();

        // When
        mGameThread.pauseGame();
        Thread.sleep(200);
        clearInvocations(mGameEngine);

        // Then
        verifyNeverUpdate();
        verify(mGameThread, atLeastOnce()).waitGamePausedLock();

        // When
        mGameThread.resumeGame();
        Thread.sleep(200);
        clearInvocations(mGameEngine);
        Thread.sleep(200);

        // Then
        verifyAtLeastOnceUpdate();

        // When
        mGameThread.pauseGame();
        Thread.sleep(200);
        clearInvocations(mGameEngine);

        // Then
        verifyNeverUpdate();
        verify(mGameThread, atLeastOnce()).waitGamePausedLock();

        // When
        mGameThread.stopGame();
        Thread.sleep(200);
        clearInvocations(mGameEngine);
        clearInvocations(mGameThread);

        // Then
        verifyNoMoreInteractions(mGameEngine);
        verifyNoMoreInteractions(mGameThread);
    }
}