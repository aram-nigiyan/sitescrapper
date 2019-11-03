package anigiyan.sitescrapper;

import anigiyan.sitescrapper.processor.AddressLoader;
import anigiyan.sitescrapper.processor.CompanyData;
import anigiyan.sitescrapper.processor.SearchTableDataLoader;
import anigiyan.sitescrapper.processor.WebDriverProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTableDataLoader.class, Configs.class, WebDriverProvider.class, Runner.class, ResourceLoader.class, AddressLoader.class})
public class ResourceLoadTests {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoadTests.class);

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Autowired
    private AddressLoader addressLoader;

    @Test
    public void mainResourceLoadTest() {
        SearchTableDataLoader searchTableDataLoader = this.searchTableDataLoader;
        searchTableDataLoader.extract(1, 3);

        List<CompanyData> companies = searchTableDataLoader.getCompanies();
        Assert.assertFalse(companies.isEmpty());
        Assert.assertNotNull(companies.get(0).getImage());
    }

    @Test
    public void mainResourceLoadAllTest() {
        SearchTableDataLoader searchTableDataLoader = this.searchTableDataLoader;
        searchTableDataLoader.extract(16, -1);

        Assert.assertEquals("Unexpected count of collected companies, check real data for actual total or bug in code",
                2489, searchTableDataLoader.getCompanies().size());

        long noImageCount = searchTableDataLoader.getCompanies().stream().filter(c -> c.getImage() == null).count();
        logger.info("Companies without image: {}", noImageCount);
        Assert.assertTrue(noImageCount > 0);
    }

    @Test
    public void addressLoaderTest() {
        Assert.assertNotNull(addressLoader.load("İSKENDERUN DEMİR VE ÇELİK A.Ş."));
    }
}
