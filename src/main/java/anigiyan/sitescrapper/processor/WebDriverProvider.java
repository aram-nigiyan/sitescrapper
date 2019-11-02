package anigiyan.sitescrapper.processor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

@Component
public class WebDriverProvider {

    WebDriver newDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }
//
//    WebDriver newDriver() {
//        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setJavascriptEnabled(true);
//        caps.setCapability("takesScreenshot", true);
//        caps.setCapability(
//                                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//                                configs.getWebDriverUrl()
//                            );
//        return new PhantomJSDriver(caps);
//    }
}
