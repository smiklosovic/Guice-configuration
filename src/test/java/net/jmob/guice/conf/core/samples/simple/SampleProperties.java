/**
 * Copyright 2015 Yves Galante
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jmob.guice.conf.core.samples.simple;

import com.google.inject.AbstractModule;
import net.jmob.guice.conf.core.BindConfig;
import net.jmob.guice.conf.core.ConfigurationModule;
import net.jmob.guice.conf.core.InjectConfig;
import org.junit.Before;
import org.junit.Test;

import static com.google.inject.Guice.createInjector;
import static net.jmob.guice.conf.core.Syntax.PROPERTIES;
import static org.junit.Assert.assertEquals;

@BindConfig(value = "net/jmob/guice/conf/core/samples/sample_00", syntax = PROPERTIES)
public class SampleProperties {

    @InjectConfig(value = "value")
    private String value;

    @Before
    public void init() {
        createInjector(new SampleModule(this));
    }

    @Test
    public void test() {
        assertEquals("1234", value);
    }

    public static class SampleModule extends AbstractModule {

        private final Object test;

        public SampleModule(Object test) {
            this.test = test;
        }

        @Override
        protected void configure() {
            install(ConfigurationModule.create());
            requestInjection(test);
        }
    }
}