package anigiyan.sitescrapper;

import anigiyan.sitescrapper.processor.CompanyData;
import anigiyan.sitescrapper.processor.RemoteIdLoader;
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
import java.util.stream.Collectors;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTableDataLoader.class, Configs.class, WebDriverProvider.class, Runner.class, ResourceLoader.class, RemoteIdLoader.class, ExecutorsPool.class})
public class ResourceLoadTests {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoadTests.class);

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Autowired
    private RemoteIdLoader remoteIdLoader;

    @Test
    public void mainResourceLoadTest() {
        SearchTableDataLoader searchTableDataLoader = this.searchTableDataLoader;
        searchTableDataLoader.extract(20);

        List<CompanyData> companies = searchTableDataLoader.getCompanies();
        Assert.assertFalse(companies.isEmpty());

        List<CompanyData> companiesWithImage = companies.stream().filter(CompanyData::hasImage).collect(Collectors.toList());
        remoteIdLoader.load(companiesWithImage);

        companiesWithImage.forEach(it ->
                Assert.assertTrue(it.hasImage() && it.getRemoteId() != null)
        );

        //todo: load address
    }

    @Test
    public void mainResourceLoadAllTest() {
        SearchTableDataLoader searchTableDataLoader = this.searchTableDataLoader;
        searchTableDataLoader.extractAll();

        List<CompanyData> companies = searchTableDataLoader.getCompanies();
        Assert.assertEquals("Unexpected count of collected companies, check real data for actual total or else bug in code",
                2489, companies.size());

        long hasImageCount = companies.stream().filter(CompanyData::hasImage).count();
        logger.info("Companies with image: {}, without: {}", hasImageCount, companies.size() - hasImageCount);
        Assert.assertTrue(hasImageCount > 0);
    }

    @Test
    public void addressLoaderTest() {
        Assert.assertNotNull(remoteIdLoader.load("İSKENDERUN DEMİR VE ÇELİK A.Ş."));
    }
}
