package anigiyan.sitescrapper.app.config;

import anigiyan.sitescrapper.app.ResourceLoader;
import anigiyan.sitescrapper.app.webdriver.WebDriverPool;
import anigiyan.sitescrapper.app.webdriver.WebDriverProvider;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Developer: nigiyan
 * Date: 05/11/2019
 */

@Configuration
public class Beans {

    @Autowired
    private Configs configs;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Bean
    public ResourceLoader initHttpClientPool() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(200);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

        return new ResourceLoader(httpClient);
    }

    @Bean
    public WebDriverPool initWebDriverPool() {
        GenericObjectPoolConfig<WebDriver> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(configs.getWorkerCount());
        config.setMaxIdle(configs.getWorkerCount());
        config.setBlockWhenExhausted(true);

        return new WebDriverPool(new BasePooledObjectFactory<WebDriver>() {

            final Logger logger = LoggerFactory.getLogger(WebDriverPool.class);

            @Override
            public WebDriver create() {
                return webDriverProvider.newDriver();
            }

            @Override
            public PooledObject<WebDriver> wrap(WebDriver obj) {
                return new DefaultPooledObject<>(obj);
            }

            @Override
            public void destroyObject(PooledObject<WebDriver> pooledObject) throws Exception {
                logger.debug("Destroying web driver object");
                pooledObject.getObject().quit();
            }

            @Override
            public void passivateObject(PooledObject<WebDriver> pooledObject) throws Exception {
                super.passivateObject(pooledObject);
                logger.trace("Driver returned to pool. Resetting location");
                pooledObject.getObject().get(webDriverProvider.emptyLocation());
            }

            @Override
            public void activateObject(PooledObject<WebDriver> p) throws Exception {
                super.activateObject(p);
                logger.trace("Driver requested from pool");
            }
        }, config);
    }
}
