package anigiyan.sitescrapper;

import anigiyan.sitescrapper.app.Configs;
import anigiyan.sitescrapper.app.ExecutorsPool;
import anigiyan.sitescrapper.app.ResourceLoader;
import anigiyan.sitescrapper.app.Runner;
import anigiyan.sitescrapper.processor.AddressesLoader;
import anigiyan.sitescrapper.processor.CompanyData;
import anigiyan.sitescrapper.processor.RemoteIdLoader;
import anigiyan.sitescrapper.processor.SearchTableDataLoader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTableDataLoader.class, Configs.class, Runner.class, ResourceLoader.class, RemoteIdLoader.class, ExecutorsPool.class, AddressesLoader.class})
public class ResourceLoadTests {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoadTests.class);

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Autowired
    private RemoteIdLoader remoteIdLoader;

    @Autowired
    private AddressesLoader addressesLoader;

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
    public void addressWebApiTest() {
        Assert.assertNotNull(remoteIdLoader.load("İSKENDERUN DEMİR VE ÇELİK A.Ş."));
    }

    @Test
    public void addressLoadTest() throws ExecutionException, InterruptedException {
        long id = 10958L;
        String text = addressesLoader.load(id).get();
        logger.info("Address for company with id {} resolved to {}", id, text);
        Assert.assertNotNull(text);
    }

    @Test
    public void WebDriverPoolTest() throws ExecutionException, InterruptedException {
        long id = 10958L;
        String text = addressesLoader.load(id).get();
        logger.info("Address for company with id {} resolved to {}", id, text);
        Assert.assertNotNull(text);
    }
}
