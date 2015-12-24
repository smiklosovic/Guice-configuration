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
package net.jmob.guice.conf.core.impl.virtual;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class VirtualBeanFactoryTest {


    @Mock
    private Config config;
    @Mock
    private ConfigObject configObject;
    @Mock
    private ConfigValue configValue;

    private Integer field;
    private Optional<Integer> optional;

    @Test
    public void nominal() throws NoSuchFieldException {

        when(config.getAnyRef("test")).thenReturn(111);

        Field field = this.getClass().getDeclaredField("field");
        VirtualBeanFactory virtualBeanFactory = new VirtualBeanFactory()
                .withConfig(config)
                .withField(field)
                .withPath("test")
                .withType(Integer.class);

        Object value = virtualBeanFactory.buildValue();

        assertThat(virtualBeanFactory.getField(), is(field));
        assertThat(value, instanceOf(Integer.class));
        assertThat(value, is(111));
    }

    @Test
    public void optional() throws NoSuchFieldException {
        VirtualBeanFactory virtualBeanFactory = new VirtualBeanFactory()
                .withConfig(config)
                .withField(this.getClass().getDeclaredField("optional"))
                .withPath("optional")
                .withType(Optional.class);

        Object value = virtualBeanFactory.buildValue();

        assertThat(value, instanceOf(Optional.class));
        assertThat(value, is(Optional.empty()));
    }

    @Test
    public void interfaces() throws NoSuchFieldException {

        when(configValue.unwrapped()).thenReturn(999);
        when(config.getConfig("test")).thenReturn(config);
        when(config.root()).thenReturn(configObject);
        when(configObject.entrySet()).thenReturn(new HashSet<>(Collections.singleton(new
                AbstractMap.SimpleEntry<>("integer", configValue))));

        VirtualBeanFactory virtualBeanFactory = new VirtualBeanFactory()
                .withConfig(config)
                .withField(this.getClass().getDeclaredField("field"))
                .withPath("test")
                .withType(InterfaceForTest.class);

        Object value = virtualBeanFactory.buildValue();

        assertThat(value, instanceOf(InterfaceForTest.class));
        assertThat(((InterfaceForTest) value).getInteger(), is(999));
    }

    @Test(expected = RuntimeException.class)
    public void typeNotSupported() throws NoSuchFieldException {

        VirtualBeanFactory virtualBeanFactory = new VirtualBeanFactory()
                .withConfig(config)
                .withField(this.getClass().getDeclaredField("field"))
                .withPath("test")
                .withType(Float.class);

        virtualBeanFactory.buildValue();
    }

    public interface InterfaceForTest {
        Integer getInteger();
    }
}