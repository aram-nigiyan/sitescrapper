package anigiyan.sitescrapper;

import anigiyan.sitescrapper.processor.*;
import anigiyan.sitescrapper.repository.CompanyRepository;
import anigiyan.sitescrapper.service.CompaniesExportToCSVService;
import anigiyan.sitescrapper.service.CompanyPersisterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootApp.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@TestPropertySource(properties = {
        "sitescrapper.worker.count=4"
})
public class AppTests {

    private static final Logger logger = LoggerFactory.getLogger(AppTests.class);

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Autowired
    private RemoteIdLoader remoteIdLoader;

    @Autowired
    private AddressesLoader addressesLoader;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyPersisterService companyPersisterService;

    @Autowired
    private CompaniesExportToCSVService companiesExportToCSVService;

    @Test
    public void entireTest() {
        long start = System.currentTimeMillis();

        searchTableDataLoader.extract(2);
        List<CompanyData> companies = searchTableDataLoader.getCompanies();
        Assert.assertTrue(companies.stream().noneMatch(it -> it.getName().isEmpty()));
        ////// names and images load completed ///////

        List<CompanyData> companiesWithImage = companies.stream().filter(CompanyData::hasImage).collect(Collectors.toList());
        remoteIdLoader.load(companiesWithImage);
        Assert.assertTrue(companiesWithImage.stream().anyMatch(CompanyData::hasRemoteId));
        ////// companies IDs load completed ///////

        List<CompanyData> companiesWithIDs = companiesWithImage.stream().filter(CompanyData::hasRemoteId).collect(Collectors.toList());
        addressesLoader.load(companiesWithIDs);
        Assert.assertTrue(companiesWithIDs.stream().anyMatch(CompanyData::hasAddress));
        ////// addresses by IDs load completed ///////

        //PERSISTENCE check
        companyPersisterService.persist(companiesWithIDs);
        Assert.assertEquals(companyRepository.count(), companiesWithIDs.size());
        ////// Saved to DB //////

        Assert.assertTrue(companiesExportToCSVService.exportToCSV(companiesWithIDs));
        ////// Saved to CSV //////

        logger.info("---STATS--- Processing took {}secs", (System.currentTimeMillis() - start) / 1000);


        Stats.printSearchPageDataLoadStats(companies);
        Stats.printIdsFailedToResolveByName(companiesWithImage);
        Stats.printAddressesLoadStats(companiesWithIDs);
    }

}
