package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.app.ExecutorsPool;
import anigiyan.sitescrapper.app.config.Configs;
import anigiyan.sitescrapper.app.webdriver.WebDriverPool;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Developer: nigiyan
 * Date: 03/11/2019
 */

@Component
public class AddressesLoader {

    private static final Logger logger = LoggerFactory.getLogger(AddressesLoader.class);

    @Autowired
    private Configs configs;

    @Autowired
    private ExecutorsPool executorsPool;

    @Autowired
    private WebDriverPool webDriverPool;

    public void load(Collection<CompanyData> companies) {
        logger.info("Starting loading addresses for {} companies", companies.size());

        Collection<Future<CompanyData>> futures = new ArrayList<>(companies.size());
        long start = System.currentTimeMillis();

        for (CompanyData c : companies) {
            Future<CompanyData> future = executorsPool.getExecutorService().submit(new Worker(c));
            futures.add(future);
        }

        for (Future<CompanyData> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("", e);
            }
        }
        logger.info("Addresses load complete in {}ms", System.currentTimeMillis() - start);
    }

    private class Worker implements Callable<CompanyData> {

        private CompanyData companyData;

        public Worker(CompanyData companyData) {
            this.companyData = companyData;
        }

        @Override
        public CompanyData call() {
            logger.debug("Loading address for company with id {}", companyData.getRemoteId());
            long start = System.currentTimeMillis();

            WebDriver webDriver = webDriverPool.borrowObject();

            try {
                String url = configs.getCmpDetailsByIdUrl() + companyData.getRemoteId();
                logger.trace("Loading page {}", url);

                webDriver.get(url);
                logger.trace("Waiting page to load fully, company id: {}", companyData.getRemoteId());

                waitDetailsLoad(webDriver);

                logger.trace("Making details visible, company id: {}", companyData.getRemoteId());

                webDriver.findElement(By.cssSelector("div.mwf-AccordionPanel-Main > div:nth-child(1) > div.mwf-AccordionItem-ListTitle.mwf-Accordion-collapsed")).click();

                logger.trace("Details become visible, getting address content, company id: {}", companyData.getRemoteId());
                String text = webDriver.findElement(By.cssSelector(".mwf-AccordionItem-Content tr:nth-of-type(6) td:nth-of-type(2) div")).getText();

                companyData.setAddress(StringUtils.trim(text));

                logger.debug("Address for company with id {} resolved to {} loaded in {}ms", companyData.getRemoteId(), companyData.getAddress(), System.currentTimeMillis() - start);
            } catch (WebDriverException e) {
                logger.error("", e);
            } finally {
                webDriverPool.returnObject(webDriver);
            }

            return companyData;
        }

        private void waitDetailsLoad(WebDriver webDriver) {
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
            webDriverWait.pollingEvery(Duration.ofMillis(200));
            webDriverWait.until(
                    ExpectedConditions.and(
                            ExpectedConditions.numberOfElementsToBe(By.className("mwf-AccordionPanel-Main"), 1),
                            ExpectedConditions.numberOfElementsToBe(By.className("mwf-Spinner-Glass"), 0)
                    )
            );
        }
    }
}
