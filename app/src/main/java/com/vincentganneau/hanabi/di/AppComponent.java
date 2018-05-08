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

package com.vincentganneau.hanabi.di;

import android.app.Application;

import com.vincentganneau.hanabi.HanabiApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Dagger {@link Component} that uses modules to fulfill requested dependencies.
 * @author Vincent Ganneau
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    /**
     * {@link Component.Builder} that enables to bind an {@link Application} instance to the Dagger graph.
     */
    @Component.Builder
    interface Builder {

        /**
         * Binds an {@link Application} instance to the Dagger graph.
         * @param application the {@link Application} instance.
         * @return the {@link Builder} instance.
         */
        @BindsInstance
        Builder application(Application application);

        /**
         * Builds the Dagger graph.
         * @return the {@link AppComponent} instance.
         */
        AppComponent build();
    }

    /**
     * Injects the {@link HanabiApplication} instance into the graph.
     * @param application the {@link HanabiApplication} instance.
     */
    void inject(HanabiApplication application);
}
