package anigiyan.sitescrapper.app.webdriver;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;

/**
 * Developer: nigiyan
 * Date: 03/11/2019
 */

public class WebDriverPool extends GenericObjectPool<WebDriver> {

    @Autowired
    WebDriverProvider webDriverProvider;

    private static final Logger logger = LoggerFactory.getLogger(WebDriverPool.class);

    public WebDriverPool(PooledObjectFactory<WebDriver> factory, GenericObjectPoolConfig<WebDriver> config) {
        super(factory, config);
    }

    @Override
    public WebDriver borrowObject() throws RuntimeException {
        try {
            WebDriver webDriver = super.borrowObject();
            webDriver.get(webDriverProvider.emptyLocation());
            return webDriver;
        } catch (Exception e) {
            throw new RuntimeException("Exception at object borrow", e);
        }
    }

    @Override
    public void returnObject(WebDriver obj) {
        super.returnObject(obj);
        obj.get(webDriverProvider.emptyLocation());
    }

    @PreDestroy
    void destroy() {
        logger.info("Closing the web drivers pool");
        close();
    }
}
