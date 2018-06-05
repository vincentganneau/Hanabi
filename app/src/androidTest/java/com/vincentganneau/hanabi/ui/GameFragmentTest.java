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

package com.vincentganneau.hanabi.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vincentganneau.hanabi.R;
import com.vincentganneau.hanabi.model.GameEngine;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests the {@link GameFragment} class.
 * @author Vincent Ganneau
 */
@RunWith(AndroidJUnit4.class)
public class GameFragmentTest {

    @Rule
    public final ActivityTestRule<GameActivity> activityTestRule = new ActivityTestRule(GameActivity.class);

    // Fragment
    private GameFragment mGameFragment;

    @Before
    public void setUp() {
        mGameFragment = (GameFragment) activityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    /**
     * Tests the entire game lifecycle.
     */
    @Test
    public void testGameLifecycle() {
        // Given
        mGameFragment.mGameEngine = mock(GameEngine.class);
        onView(allOf(withId(android.R.id.button1), withText(R.string.btn_start))).check(matches(isEnabled()));
        onView(allOf(withId(android.R.id.button2), withText(R.string.btn_pause))).check(matches(not(isEnabled())));

        // When
        onView(withId(android.R.id.button1)).perform(click());

        // Then
        verify(mGameFragment.mGameEngine).startGame();
        onView(allOf(withId(android.R.id.button1), withText(R.string.btn_stop))).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button2), withText(R.string.btn_pause))).check(matches(isEnabled()));

        // When
        onView(withId(android.R.id.button2)).perform(click());

        // Then
        verify(mGameFragment.mGameEngine).pauseGame();
        onView(allOf(withId(android.R.id.button2), withText(R.string.btn_resume))).check(matches(isDisplayed()));
    }
}