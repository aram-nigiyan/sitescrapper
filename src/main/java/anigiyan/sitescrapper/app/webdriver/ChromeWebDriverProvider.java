package anigiyan.sitescrapper.app.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Developer: nigiyan
 * Date: 04/11/2019
 */

@Component
@ConditionalOnProperty(
        value = "sitescrapper.webdriver.name",
        havingValue = "chrome",
        matchIfMissing = true)
public class ChromeWebDriverProvider implements WebDriverProvider {

    @Override
    public WebDriver newDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // +10% performance if no images rendered thanks to skip requests for "img src"
        Map<String, Object> images = new HashMap<String, Object>(1);
        images.put("images", 2);
        Map<String, Object> prefs = new HashMap<String, Object>(1);
        prefs.put("profile.default_content_setting_values", images);

        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }

    @Override
    public String emptyLocation() {
        return "about:blank";
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
