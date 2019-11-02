package anigiyan.sitescrapper;

import anigiyan.sitescrapper.processor.SearchTableDataLoader;
import anigiyan.sitescrapper.processor.WebDriverProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTableDataLoader.class, Configs.class, WebDriverProvider.class})
public class ResourceLoadTests {

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Test
    public void mainResourceLoadTest() {
        SearchTableDataLoader searchTableDataLoader = this.searchTableDataLoader;
        searchTableDataLoader.extract(10);

        Assert.assertEquals("Unexpected count of collected companies, check real data for actual total or bug in code",
                2489, searchTableDataLoader.getCompanies().size());
    }

}
