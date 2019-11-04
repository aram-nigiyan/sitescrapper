package anigiyan.sitescrapper;

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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

    private static final Logger logger = LoggerFactory.getLogger(AppTests.class);

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Autowired
    private RemoteIdLoader remoteIdLoader;

    @Autowired
    private AddressesLoader addressesLoader;

    @Test
    public void entireTest() {
        long start = System.currentTimeMillis();

        searchTableDataLoader.extractAll();
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

        logger.info("---STATS--- Processing took {}secs", (System.currentTimeMillis() - start) / 1000);


        printSearchPageDataLoadStats(companies);
        printIdsFailedToResolveByName(companiesWithImage);
        printAddressesLoadStats(companiesWithIDs);
    }

    private static void printSearchPageDataLoadStats(Collection<CompanyData> companyData) {
        logger.info("---STATS--- Total companies fetched: {}, Companies with logo: {}", companyData.size(), companyData.stream().filter(CompanyData::hasImage).count());
    }

    private static void printIdsFailedToResolveByName(Collection<CompanyData> companyData) {
        List<String> namesForWhichFailedToResolveId = companyData.stream().filter(it -> !it.hasRemoteId()).map(CompanyData::getName).collect(Collectors.toList());
        if (namesForWhichFailedToResolveId.isEmpty()) {
            logger.info("---STATS--- All companies IDs has been resolved by name.");
        } else {
            logger.warn("---STATS WARN--- Failed to resolve IDs for companies:\n{}", namesForWhichFailedToResolveId);
        }
    }

    private static void printAddressesLoadStats(Collection<CompanyData> companyData) {
        List<Long> companiesForWhichAddressDoNotExist = companyData.stream().filter(it -> !it.hasAddress()).map(CompanyData::getRemoteId).collect(Collectors.toList());
        if (companiesForWhichAddressDoNotExist.isEmpty()) {
            logger.info("---STATS--- For all companies addresses exists");
        } else {
            logger.info("---STATS--- For the following companies address does not exist:\n{}", companiesForWhichAddressDoNotExist);
        }
    }
}
