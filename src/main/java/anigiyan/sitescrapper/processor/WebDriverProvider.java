package anigiyan.sitescrapper.processor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

@Component
public class WebDriverProvider {

    WebDriver newDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // +10% performance if no images rendered thanks to skip requests for "img src"
        HashMap<String, Object> images = new HashMap<String, Object>();
        images.put("images", 2);
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values", images);

        options.setExperimentalOption("prefs", prefs);

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
