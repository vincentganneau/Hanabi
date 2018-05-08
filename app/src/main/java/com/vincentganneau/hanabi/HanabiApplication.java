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

import android.app.Application;

import com.vincentganneau.hanabi.di.AppComponent;
import com.vincentganneau.hanabi.di.DaggerAppComponent;

/**
 * The Hanabi {@link Application}.
 * <p>
 * Builds the graph of dependencies using {@link AppComponent}.
 * </p>
 * @author Vincent Ganneau
 */
public class HanabiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }
}
