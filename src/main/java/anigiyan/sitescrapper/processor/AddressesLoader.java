package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.Configs;
import anigiyan.sitescrapper.ExecutorsPool;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
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
    private WebDriverProvider webDriverProvider;

    public Future<String> load(Long id) {

        logger.info("Starting details load for company with id", id);

        return executorsPool.getExecutorService().submit(new Worker(id, webDriverProvider.newDriver() /*todo: implement pool*/));
    }

    private class Worker implements Callable<String> {

        private WebDriver webDriver;
        private Long companyId;

        public Worker(Long companyId, WebDriver webDriver) {
            this.webDriver = webDriver;
            this.companyId = companyId;
        }

        @Override
        public String call() throws Exception {
            webDriver.get(configs.getCmpDetailsByIdUrl() + companyId);
            waitDetailsLoad();
            webDriver.findElement(By.cssSelector("div.mwf-AccordionPanel-Main > div:nth-child(1) > div.mwf-AccordionItem-ListTitle")).click();
            String text = webDriver.findElement(By.cssSelector(".mwf-AccordionItem-Content tr:nth-of-type(6) td:nth-of-type(2) div")).getText();

            webDriver.quit();

            return StringUtils.trim(text);
        }

        private void waitDetailsLoad() {


            new WebDriverWait(webDriver, 20).until(
                    ExpectedConditions.and(
                            ExpectedConditions.numberOfElementsToBe(By.className("mwf-Spinner-Glass"), 0),
                            ExpectedConditions.visibilityOfElementLocated(By.className("mwf-AccordionItem-ListTitle"))
                    )
            );
        }
    }


}
