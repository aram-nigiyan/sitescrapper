package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.Configs;
import anigiyan.sitescrapper.ResourceLoader;
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

    @Autowired
    private ResourceLoader resourceLoader;

    private List<CompanyData> companies = Collections.synchronizedList(new ArrayList<>(64));

    /**
     * Extract company data
     *
     * @param workerCount number of threads processing page ranges paralelly
     * @param maxPages    -1 means all (used for tests)
     * @see CompanyData
     */
    public void extract(int workerCount, int maxPages) {
        logger.info("Data extraction is started");
        companies.clear();

        long start = System.currentTimeMillis();

        int totalPageNumber = maxPages == -1 ? resolveTotalPageNumber() : maxPages;
        collectInParallel(workerCount, totalPageNumber);

        logger.info("Data extraction completed in {}ms", System.currentTimeMillis() - start);
    }

    private void collectInParallel(int workerCount, int totalPageNumber) {
        int range = totalPageNumber / workerCount + (totalPageNumber % workerCount == 0 ? 0 : 1);

        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        Collection<Future<List<CompanyData>>> futures = new ArrayList<>();

        for (int page = 1; page <= totalPageNumber; page += range + 1) {
            Future<List<CompanyData>> future = executor.submit(new Worker(page, page + range > totalPageNumber ? totalPageNumber : page + range));
            futures.add(future);
        }

        for (Future<List<CompanyData>> future : futures) {
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

    public List<CompanyData> getCompanies() {
        return companies;
    }

    private class Worker implements Callable<List<CompanyData>> {

        int startPage;
        int endPage;
        WebDriver driver;

        Worker(int startPage, int endPage) {
            this.startPage = startPage;
            this.endPage = endPage;
            driver = webDriverProvider.newDriver();
        }

        @Override
        public List<CompanyData> call() {
            logger.info("Processing pages: [{}, {}]", startPage, endPage);

            try {
                long start = System.currentTimeMillis();
                driver.get(configs.getCmpListUrl());
                waitListLoad(driver);
                changePage(startPage);
                waitListLoad(driver);
                logger.debug("Navigation on {} page took {}ms", startPage, System.currentTimeMillis() - start);
                List<CompanyData> result = new ArrayList<>();

                for (int i = startPage; i <= endPage; i++) {

                    logger.info("Processing page {}", i);

                    result.addAll(extractCompaniesOnPage());

                    changePage(i + 1);

                    waitListLoad(driver);
                }
                return result;
            } finally {
                driver.quit();
            }
        }

        private List<CompanyData> extractCompaniesOnPage() {

            List<CompanyData> companies = loadRowData();

            downloadImages(companies);

            return companies;
        }

        private List<CompanyData> loadRowData() {
            List<WebElement> rows = driver.findElements(By.cssSelector(".content .cursorPointer"));
            List<CompanyData> companies = new ArrayList<>();

            for (WebElement row : rows) {

                String companyName = row.findElement(By.tagName("span")).getText();
                String imageSrc = row.findElement(By.tagName("img")).getAttribute("src");

                logger.trace("Parsed company:\nname={}\ninmagesrc={}", companyName, imageSrc);

                companies.add(new CompanyData(companyName, imageSrc));
            }

            return companies;
        }

        private void downloadImages(Collection<CompanyData> companies) {
            long start = System.currentTimeMillis();
            companies.stream().filter(c -> !c.getImageUrl().contains("nologo_Small")).parallel().forEach(company -> {
                company.setImage(resourceLoader.load(company.getImageUrl()));
            });
            logger.debug("Loading {} images took {}ms", companies.size(), System.currentTimeMillis() - start);
        }

        private void changePage(int page) {
            WebElement pageInputElement = driver.findElement(By.cssSelector(".x-toolbar-left-row input"));
            pageInputElement.clear();
            pageInputElement.sendKeys(String.valueOf(page));
            pageInputElement.sendKeys(Keys.RETURN);
        }
    }
}
