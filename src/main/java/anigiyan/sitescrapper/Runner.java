package anigiyan.sitescrapper;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting application...");

    }

    @Bean
    public ResourceLoader initPooledHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(200);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

        return new ResourceLoader(httpClient);
    }
}
