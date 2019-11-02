package anigiyan.sitescrapper;

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

    @PostConstruct
    private void initDriverLocation() {
        System.setProperty("webdriver.chrome.driver", webdriverUrl);
    }
}
