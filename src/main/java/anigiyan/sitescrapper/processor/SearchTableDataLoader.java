package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Component
public class SearchTableDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(SearchTableDataLoader.class);

    @Autowired
    private Configs configs;

    @Autowired
    private WebDriverProvider webDriverProvider;

    private List<CompanySearchPageData> companies = Collections.synchronizedList(new ArrayList<>(64));

    public void extract(int workerCount) {
        logger.info("Data extraction is started");
        long start = System.currentTimeMillis();

        int totalPageNumber = resolveTotalPageNumber();
        collectInParallel(workerCount, totalPageNumber);

        logger.info("Data extraction completed in {}ms", System.currentTimeMillis() - start);
    }

    private void collectInParallel(int workerCount, int totalPageNumber) {
        int range = totalPageNumber / workerCount + (totalPageNumber % workerCount == 0 ? 0 : 1);

        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        Collection<Future<List<CompanySearchPageData>>> futures = new ArrayList<>();

        for (int page = 1; page <= totalPageNumber; page += range + 1) {
            Future<List<CompanySearchPageData>> future = executor.submit(new Worker(page, page + range > totalPageNumber ? totalPageNumber : page + range));
            futures.add(future);
        }

        for (Future<List<CompanySearchPageData>> future : futures) {
            try {
                companies.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("", e);
            }
        }
        logger.info("Number of companies collected: {}", companies.size());
        executor.shutdown();
    }


    private int resolveTotalPageNumber() {
        WebDriver driver = webDriverProvider.newDriver();
        try {
            driver.get(configs.getCmpListUrl());
            waitListLoad(driver);

            int totalPages = extractTotalPages(driver);
            logger.info("Total number of pages: {}", totalPages);

            return totalPages;

        } finally {
            driver.quit();
        }
    }


    /**
     * ajax calls on pagination cause spinner appear until response processed (hopefully after result is rendered) <br/>
     * wait until spinner remove & footer appear
     *
     * @param driver
     */
    private static void waitListLoad(WebDriver driver) {
        new WebDriverWait(driver, 20).until(
                ExpectedConditions.and(
                        ExpectedConditions.numberOfElementsToBe(By.className("mwf-Spinner-Glass"), 0),
                        ExpectedConditions.presenceOfElementLocated(By.className("mwf-grid-footer"))
                )
        );
    }

    private static int extractTotalPages(WebDriver driver) {
        List<WebElement> elements = driver.findElements(By.cssSelector(".x-toolbar-left .my-paging-text"));

        String text = elements.get(elements.size() - 1).getText();
        String[] split = text.split(" ");
        Assert.isTrue(split.length == 2, "Unexpected value in parsing number of total pages: " + text);

        return Integer.parseInt(split[1]);
    }

    public List<CompanySearchPageData> getCompanies() {
        return companies;
    }

    class Worker implements Callable<List<CompanySearchPageData>> {

        int startPage;
        int endPage;
        WebDriver driver;

        Worker(int startPage, int endPage) {
            this.startPage = startPage;
            this.endPage = endPage;
            driver = webDriverProvider.newDriver();
        }

        @Override
        public List<CompanySearchPageData> call() {
            logger.info("Processing pages: [{}, {}]", startPage, endPage);

            try {
                long start = System.currentTimeMillis();
                driver.get(configs.getCmpListUrl());
                waitListLoad(driver);
                changePage(startPage);
                waitListLoad(driver);
                logger.debug("Navigation on {} page took {}ms", startPage, System.currentTimeMillis() - start);
                List<CompanySearchPageData> result = new ArrayList<>();

                for (int i = startPage; i <= endPage; i++) {

                    logger.info("Processing page {}", i);

                    result.addAll(extractCompanies());

                    changePage(i + 1);

                    waitListLoad(driver);
                }
                return result;
            } finally {
                driver.quit();
            }
        }

        private void changePage(int page) {
            WebElement pageInputElement = driver.findElement(By.cssSelector(".x-toolbar-left-row input"));
            pageInputElement.clear();
            pageInputElement.sendKeys(String.valueOf(page));
            pageInputElement.sendKeys(Keys.RETURN);
        }

        private List<CompanySearchPageData> extractCompanies() {

            Stream<WebElement> rows = driver.findElements(By.cssSelector(".content .cursorPointer")).stream();
            return rows.map(it -> {

                        String companyName = it.findElement(By.tagName("span")).getText();
                        String imageSrc = it.findElement(By.tagName("img")).getAttribute("src");
                        logger.trace("Parsed company:\nname={}\ninmagesrc={}", companyName, imageSrc);

                        return new CompanySearchPageData(companyName, imageSrc);
                    }

            ).collect(Collectors.toList());
        }
    }

}
