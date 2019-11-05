package anigiyan.sitescrapper.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Component
public class Configs {

    @Value("${sitescrapper.webdriver.url}")
    private String webdriverUrl;

    @Value("${sitescrapper.companies.list.url}")
    private String cmpListUrl;

    @Value("${sitescrapper.companies.id.by.name.url}")
    private String cmpIdRequestUrl;

    @Value("${sitescrapper.companies.details.by.id.url}")
    private String cmpDetailsByIdUrl;

    @Value("${sitescrapper.worker.count}")
    private int workerCount;

    @Value("${sitescrapper.csv.file.name}")
    private String csvFileName;

    public String getWebDriverUrl() {
        return webdriverUrl;
    }

    public void setWebdriverUrl(String webdriverUrl) {
        this.webdriverUrl = webdriverUrl;
    }

    public String getCmpListUrl() {
        return cmpListUrl;
    }

    public void setCmpListUrl(String cmpListUrl) {
        this.cmpListUrl = cmpListUrl;
    }

    public String getCmpIdRequestUrl() {
        return cmpIdRequestUrl;
    }

    public void setCmpIdRequestUrl(String cmpIdRequestUrl) {
        this.cmpIdRequestUrl = cmpIdRequestUrl;
    }

    public String getCmpDetailsByIdUrl() {
        return cmpDetailsByIdUrl;
    }

    public void setCmpDetailsByIdUrl(String cmpDetailsByIdUrl) {
        this.cmpDetailsByIdUrl = cmpDetailsByIdUrl;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public String getCsvFileName() {
        return csvFileName;
    }

    public void setCsvFileName(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    @PostConstruct
    private void initDriverLocation() {
        System.setProperty("webdriver.chrome.driver", webdriverUrl);
    }
}
