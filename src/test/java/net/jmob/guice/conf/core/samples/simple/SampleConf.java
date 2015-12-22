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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@BindConfig(value = "net/jmob/guice/conf/core/samples/sample_01")
public class SampleConf {

    @InjectConfig
    private int port;

    @InjectConfig("complexType")
    private ServiceConfiguration config;

    @Before
    public void init() {
        createInjector(new SampleModule(this));
    }

    @Test
    public void test() {
        assertEquals(12, port);
        assertNotNull(config);
        assertEquals("Hello World", config.getValue());
        assertNotNull(config.getAMap());
        assertEquals("value1", config.getAMap().get("key1"));
        assertEquals("value2", config.getAMap().get("key2"));
        assertEquals("value1", config.getAList().get(0));
        assertEquals("value2", config.getAList().get(1));
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