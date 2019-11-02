package anigiyan.sitescrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Component
public class Runner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);



    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting application...");

    }
}
