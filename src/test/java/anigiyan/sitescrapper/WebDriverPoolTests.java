package anigiyan.sitescrapper;

import anigiyan.sitescrapper.app.webdriver.WebDriverPool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.IntStream;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootApp.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class WebDriverPoolTests {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverPoolTests.class);

    @Autowired
    private WebDriverPool webDriverPool;

    @Test
    public void driverAccessibilityAndInitCostTest() {
        logger.info("Simple test for web drivers pool");

        IntStream.range(0, 10).forEach(it -> {
            WebDriver webDriver = webDriverPool.borrowObject();
            webDriverPool.returnObject(webDriver);
        });
        Assert.assertEquals(0, webDriverPool.getNumActive());
    }

}
