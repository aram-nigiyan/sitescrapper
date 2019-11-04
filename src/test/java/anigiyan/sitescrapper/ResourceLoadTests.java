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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTableDataLoader.class, Configs.class, Runner.class, ResourceLoader.class, RemoteIdLoader.class, ExecutorsPool.class, AddressesLoader.class, ChromeWebDriverProvider.class})
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
//        todo: solve space in names
//        companiesWithImage.forEach(it ->
//                Assert.assertTrue(it.hasImage() && it.getRemoteId() != null)
//        );
    }

    @Test
    public void addressWebApiTest() {
        Assert.assertNotNull(remoteIdLoader.load("İSKENDERUN DEMİR VE ÇELİK A.Ş."));
    }

    @Test
    public void addressLoadTest() {
        long start = System.currentTimeMillis();
        List<CompanyData> companyDataList = new ArrayList<>();
        long[] ids = {10952, 10955, 10957, 10958, 10134, 10124, 10788, 10789, 11285, 11286, 10546, 10547, 10548, 10549, 11823, 10378, 10209, 10345, 10420, 10212, 10520, 10640, 10217, 10829, 11355, 10253, 10219, 10554, 10674, 10227, 15727, 10528, 11145, 10240, 10696, 10198, 11268, 10237, 10252, 11189, 10925, 10946, 11253, 11274, 11275, 10945, 10377, 15527, 10598, 10610, 10611, 10574, 10383, 10322, 10325, 10350, 10380, 11027, 11458, 10258, 10387, 10559, 10392, 10347, 10263, 10265, 10713, 11071, 10224, 10264, 10448, 11138, 10267, 10269, 10538, 10724, 10838, 10983, 10458, 12302, 13663, 10276, 10370, 10525, 10916, 11172, 10386, 10353, 10354, 10359, 10361, 10364, 10439, 10440, 10441, 10442, 10645, 10652, 17927, 17805, 17823, 17826, 17827, 10653, 10657, 10709, 10348, 10393, 10329, 10376, 11179, 11182, 11420, 10797, 12326, 10286, 10284, 14663, 14684, 10437, 10282, 10487, 10290, 10291, 11058, 10854, 10292, 16926, 16930, 16943, 16944, 10293, 10285, 11434, 11457, 12032, 11250, 10301, 10815, 10614, 15251, 15305, 15254, 10741, 11301, 11205, 10410, 10606, 10607, 10615, 10616, 10977, 10982, 10435, 10436, 10422, 11046, 10662, 12211, 12212, 12213, 10444, 10450, 10453, 10587};
        for (int i = 0; i < ids.length; i++) {
            companyDataList.add(new CompanyData(ids[i]));
        }

        addressesLoader.load(companyDataList);

        // addresses may be same for different companies like for #10610 and #10611
        // address may be empty
        // so no strict assertion can be made.
        Assert.assertTrue(companyDataList.stream().anyMatch(CompanyData::hasAddress));
        logger.info("Test took {}sec", (System.currentTimeMillis() - start) / 1000);
    }
}
