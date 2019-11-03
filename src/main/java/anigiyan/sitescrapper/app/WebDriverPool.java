package anigiyan.sitescrapper.app;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

/**
 * Developer: nigiyan
 * Date: 03/11/2019
 */

public class WebDriverPool extends GenericObjectPool<WebDriver> {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverPool.class);

    public WebDriverPool(PooledObjectFactory<WebDriver> factory, GenericObjectPoolConfig<WebDriver> config) {
        super(factory, config);
    }

    @Override
    public WebDriver borrowObject() throws RuntimeException {
        try {
            return super.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("Exception at object borrow", e);
        }
    }

    @PreDestroy
    void destroy() {
        logger.info("Closing the web drivers pool");

        close();
    }
}
