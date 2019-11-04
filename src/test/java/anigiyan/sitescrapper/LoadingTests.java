package anigiyan.sitescrapper;

import anigiyan.sitescrapper.app.Configs;
import anigiyan.sitescrapper.app.ExecutorsPool;
import anigiyan.sitescrapper.app.ResourceLoader;
import anigiyan.sitescrapper.app.Runner;
import anigiyan.sitescrapper.app.webdriver.ChromeWebDriverProvider;
import anigiyan.sitescrapper.processor.AddressesLoader;
import anigiyan.sitescrapper.processor.CompanyData;
import anigiyan.sitescrapper.processor.RemoteIdLoader;
import anigiyan.sitescrapper.processor.SearchTableDataLoader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTableDataLoader.class, Configs.class, Runner.class, ResourceLoader.class, RemoteIdLoader.class, ExecutorsPool.class, AddressesLoader.class, ChromeWebDriverProvider.class})
public class LoadingTests {

    @Autowired
    private RemoteIdLoader remoteIdLoader;

    @Autowired
    private AddressesLoader addressesLoader;

    @Test
    public void idsLoadingByNameTest() {
        List<CompanyData> listOfCompaniesWithNames = Stream.of("İSKENDERUN DEMİR VE ÇELİK A.Ş.", "MEM TEKSTİL SANAYİ VE TİCARET A.Ş.", "HACI ÖMER SABANCI HOLDİNG A.Ş.", "HEKTAŞ TİCARET T.A.Ş.", "X TRADE BROKERS MENKUL DEĞERLER A.Ş.", "ANAGOLD MADENCİLİK SANAYİ VE TİCARET A.Ş.")
                .map(s -> new CompanyData(s, null)).collect(Collectors.toList());

        remoteIdLoader.load(listOfCompaniesWithNames);

        Assert.assertTrue(listOfCompaniesWithNames.stream().allMatch(CompanyData::hasRemoteId));
    }

    @Test
    public void addressWebApiTest() {
        Assert.assertNotNull(remoteIdLoader.load("İSKENDERUN DEMİR VE ÇELİK A.Ş."));
    }

    @Test
    public void addressLoadTest() {
        List<CompanyData> companyDataList =
                Stream.of(10952, 10955, 10957, 10958, 10134, 10124, 10788, 10789, 11285, 11286, 10546, 10547, 10548, 10549, 11823, 10378, 10209, 10345, 10420, 10212, 10520, 10640, 10217, 10829, 11355, 10253, 10219, 10554, 10674, 10227, 15727, 10528, 11145, 10240, 10696)
                        .map(id -> new CompanyData((long) id)).collect(Collectors.toList());

        addressesLoader.load(companyDataList);

        // addresses may be same for different companies like for #10610 and #10611
        // address may be empty
        // so no strict assertion can be made.
        Assert.assertTrue(companyDataList.stream().anyMatch(CompanyData::hasAddress));
    }
}
