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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests the {@link UpdateThread} class.
 * @author Vincent Ganneau
 */
@RunWith(JUnit4.class)
public class UpdateThreadTest {

    // Dependencies
    @Mock
    private GameEngine mGameEngine;

    // Update thread
    private UpdateThread mUpdateThread;

    @Before
    public void setUp() throws InterruptedException {
        MockitoAnnotations.initMocks(this);
        mUpdateThread = spy(new UpdateThread(mGameEngine));
    }

    /**
     * Tests the {@link UpdateThread#startGame()} method.
     */
    @Test
    public void testStartGame() throws InterruptedException {
        // When
        mUpdateThread.startGame();

        // Then
        assertTrue(mUpdateThread.mGameRunning);
        assertFalse(mUpdateThread.mGamePaused);
        verify(mUpdateThread).start();
    }

    /**
     * Tests the {@link UpdateThread#resumeGame()} method when the game is not paused.
     */
    @Test
    public void testResumeUnpausedGame() {
        // When
        mUpdateThread.resumeGame();

        // Then
        verify(mUpdateThread, never()).notifyGamePausedLock();
    }

    /**
     * Tests the {@link UpdateThread#resumeGame()} method when the game is paused.
     */
    @Test
    public void testResumePausedGame() {
        // Given
        mUpdateThread.mGamePaused = true;

        // When
        mUpdateThread.resumeGame();

        // Then
        assertFalse(mUpdateThread.mGamePaused);
        verify(mUpdateThread).notifyGamePausedLock();
    }

    /**
     * Tests the {@link UpdateThread#pauseGame()} method.
     */
    @Test
    public void testPauseGame() {
        // When
        mUpdateThread.pauseGame();

        // Then
        assertTrue(mUpdateThread.mGamePaused);
    }

    /**
     * Tests the {@link UpdateThread#stopGame()} method.
     */
    @Test
    public void testStopGame() {
        // Given
        mUpdateThread.mGameRunning = true;

        // When
        mUpdateThread.stopGame();

        // Then
        assertFalse(mUpdateThread.mGameRunning);
        verify(mUpdateThread).resumeGame();
    }

    /**
     * Tests the {@link UpdateThread#run()} method when game has not been started.
     */
    @Test
    public void testNonRunningGame() {
        // When
        mUpdateThread.start();

        // Then
        verify(mGameEngine, never()).updateGame(anyLong());
    }

    /**
     * Tests the {@link UpdateThread#run()} method when game is started.
     */
    @Test
    public void testRunningGame() throws InterruptedException {
        // When
        mUpdateThread.startGame();
        Thread.sleep(20);

        // Then
        verify(mGameEngine, atLeastOnce()).updateGame(anyLong());

        // When
        mUpdateThread.pauseGame();
        Thread.sleep(20);
        clearInvocations(mGameEngine);

        // Then
        verify(mGameEngine, never()).updateGame(anyLong());
        verify(mUpdateThread, atLeastOnce()).waitGamePausedLock();

        // When
        mUpdateThread.resumeGame();
        Thread.sleep(20);
        clearInvocations(mGameEngine);

        // Then
        verify(mGameEngine, atLeastOnce()).updateGame(anyLong());

        // When
        doThrow(new InterruptedException()).when(mUpdateThread).waitGamePausedLock();
        mUpdateThread.pauseGame();
        Thread.sleep(20);
        clearInvocations(mGameEngine);

        // Then
        verify(mGameEngine, never()).updateGame(anyLong());
        verify(mUpdateThread, atLeastOnce()).waitGamePausedLock();

        // When
        mUpdateThread.stopGame();
        Thread.sleep(20);
        clearInvocations(mGameEngine);
        clearInvocations(mUpdateThread);

        // Then
        verifyNoMoreInteractions(mGameEngine);
        verifyNoMoreInteractions(mUpdateThread);
    }
}