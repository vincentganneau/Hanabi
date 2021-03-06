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

package com.vincentganneau.hanabi;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Tests the {@link HanabiApplication} class.
 * @author Vincent Ganneau
 */
@RunWith(AndroidJUnit4.class)
public class HanabiApplicationTest {

    /**
     * Tests the application context.
     */
    @Test
    public void testApplicationContext() {
        // Given
        final Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();

        // Then
        assertEquals("com.vincentganneau.hanabi", context.getPackageName());
        assertTrue(context instanceof HanabiApplication);
    }
}