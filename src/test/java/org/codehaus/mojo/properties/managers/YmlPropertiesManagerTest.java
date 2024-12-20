package org.codehaus.mojo.properties.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YmlPropertiesManagerTest {

    private static final String NL = System.lineSeparator();

    private final YmlPropertiesManager manager = new YmlPropertiesManager();

    @Test
    public void testLoad() throws IOException {

        // given
        final String props = "# comments" + NL + NL + "key1:" + NL + "  key2: value1" + NL + "key3: value2" + NL
                + "key4: " + NL + "  - A" + NL + "  - B" + NL + "  - C" + NL;

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(props.getBytes());

        // when
        final Properties properties = manager.load(inputStream);

        // then
        assertEquals(5, properties.size());
        assertEquals("value1", properties.getProperty("key1.key2"));
        assertEquals("value2", properties.getProperty("key3"));
        assertEquals("A", properties.getProperty("key4[0]"));
    }

    @Test
    public void testSave() throws IOException {

        // given
        final Properties properties = new Properties();
        properties.setProperty("key1", "value1");
        properties.setProperty("key2", "value2");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // when
        manager.save(properties, outputStream, "Test comments");

        // then
        final String expected = "#Test comments" + NL + "key1=value1" + NL + "key2=value2" + NL;

        assertEquals(expected, outputStream.toString());
    }
}
