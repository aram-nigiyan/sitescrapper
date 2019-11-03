package anigiyan.sitescrapper.app;

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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Component
@Configuration
public class Runner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    @Autowired
    private Configs configs;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting application...");

    }

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
            public void destroyObject(PooledObject<WebDriver> p) throws Exception {
                logger.info("Destroying web driver object");
                p.getObject().quit();
            }

        }, config);
    }
}
