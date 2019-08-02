package me.maiz.app.little;

import junit.framework.TestCase;
import me.maiz.app.little.config.ConfigParser;
import me.maiz.app.little.config.Configuration;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConfigFileParseTest {

    @Test
    public void testConfig(){
        ConfigParser configParser = new ConfigParser();
        final Configuration configuration = configParser.parse("mvc.xml");
        assertNotNull(configuration);
        assertNotNull(configuration.getBasePackage());
        assertNotNull(configuration.getViewPrefix());
        assertNotNull(configuration.getViewSuffix());
        assertEquals("me.maiz.app.little.main",configuration.getBasePackage());
        assertEquals("/WEB-INF/pages/",configuration.getViewPrefix());
        assertEquals(".jsp",configuration.getViewSuffix());
    }

}
