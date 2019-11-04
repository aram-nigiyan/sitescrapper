package anigiyan.sitescrapper.app.webdriver;

import org.openqa.selenium.WebDriver;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

/**
 * Abstraction to provide chrome/phantomjs/firefox drivers based on config.
 */
public interface WebDriverProvider {
    WebDriver newDriver();

    String emptyLocation();
}


