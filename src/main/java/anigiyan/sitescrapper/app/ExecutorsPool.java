package anigiyan.sitescrapper.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Developer: nigiyan
 * Date: 03/11/2019
 */

@Component
public class ExecutorsPool {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorsPool.class);

    private ExecutorService executorService;

    @Autowired
    private Configs configs;

    @PostConstruct
    void initPool() {
        executorService = Executors.newFixedThreadPool(configs.getWorkerCount());
        logger.info("Executors pool initialized with number of threads: {}", configs.getWorkerCount());
    }

    @PreDestroy
    void cleanup() {
        logger.info("Shutting down executors pool");
        executorService.shutdown();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
