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

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests the {@link UpdateThread} class.
 * @author Vincent Ganneau
 */
@RunWith(JUnit4.class)
public class UpdateThreadTest extends GameThreadTest {

    @Override
    protected void verifyNeverUpdate() {
        verify(mGameEngine, never()).updateGame(anyLong());
    }

    @Override
    protected void verifyAtLeastOnceUpdate() {
        verify(mGameEngine, atLeastOnce()).updateGame(anyLong());
    }
}