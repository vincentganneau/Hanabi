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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vincentganneau.hanabi.R;
import com.vincentganneau.hanabi.model.GameEngine;
import com.vincentganneau.hanabi.model.GameScore;

/**
 * {@link Fragment} subclass that handles the game user interface.
 * @author Vincent Ganneau
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    // Game engine
    /**
     * The game engine.
     */
    @VisibleForTesting
    public GameEngine mGameEngine;

    // Views
    /**
     * The start/stop button.
     */
    private Button mStartStopButton;
    /**
     * The pause/resume button.
     */
    private Button mPauseResumeButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameEngine = new GameEngine(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add game score object
        mGameEngine.addGameObject(new GameScore(view.findViewById(android.R.id.text1)));

        // Set views
        mStartStopButton = view.findViewById(android.R.id.button1);
        mPauseResumeButton = view.findViewById(android.R.id.button2);
        mPauseResumeButton.setEnabled(mGameEngine.isGameRunning());

        // Set listeners
        mStartStopButton.setOnClickListener(this);
        mPauseResumeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == android.R.id.button1 && mGameEngine.isGameRunning()) {
            mGameEngine.stopGame();
            mStartStopButton.setText(R.string.btn_start);
            mPauseResumeButton.setEnabled(false);
        } else if (id == android.R.id.button1) {
            mGameEngine.startGame();
            mStartStopButton.setText(R.string.btn_stop);
            mPauseResumeButton.setText(R.string.btn_pause);
            mPauseResumeButton.setEnabled(true);
        } else if (id == android.R.id.button2 && !mGameEngine.isGamePaused()) {
            mGameEngine.pauseGame();
            mPauseResumeButton.setText(R.string.btn_resume);
        } else if (id == android.R.id.button2) {
            mGameEngine.resumeGame();
            mPauseResumeButton.setText(R.string.btn_pause);
        }
    }
}
