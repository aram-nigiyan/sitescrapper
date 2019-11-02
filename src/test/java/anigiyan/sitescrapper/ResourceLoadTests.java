package anigiyan.sitescrapper;

import anigiyan.sitescrapper.processor.CompanySearchPageData;
import anigiyan.sitescrapper.processor.SearchTableDataLoader;
import anigiyan.sitescrapper.processor.WebDriverProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTableDataLoader.class, Configs.class, WebDriverProvider.class, Runner.class, ResourceLoader.class})
public class ResourceLoadTests {

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Test
    public void mainResourceLoadTest() {
        SearchTableDataLoader searchTableDataLoader = this.searchTableDataLoader;
        searchTableDataLoader.extract(1, 1);

        List<CompanySearchPageData> companies = searchTableDataLoader.getCompanies();
        Assert.assertFalse(companies.isEmpty());
        Assert.assertNotNull(companies.get(0).getImage());
    }

    @Test
    public void mainResourceLoadAllTest() {
        SearchTableDataLoader searchTableDataLoader = this.searchTableDataLoader;
        searchTableDataLoader.extract(16, -1);

        Assert.assertEquals("Unexpected count of collected companies, check real data for actual total or bug in code",
                2489, searchTableDataLoader.getCompanies().size());
    }
}
