package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.Configs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

@Component
public class WebDriverProvider {
    @Autowired
    Configs configs;

    WebDriver newDriver() {
        return new ChromeDriver();
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
