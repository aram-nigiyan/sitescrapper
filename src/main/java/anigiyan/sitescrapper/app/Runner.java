package anigiyan.sitescrapper.app;

import anigiyan.sitescrapper.processor.*;
import anigiyan.sitescrapper.service.CompaniesExportToCSVService;
import anigiyan.sitescrapper.service.CompanyPersisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Component
public class Runner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    @Autowired
    private SearchTableDataLoader searchTableDataLoader;

    @Autowired
    private RemoteIdLoader remoteIdLoader;

    @Autowired
    private CompanyPersisterService companyPersisterService;

    @Autowired
    private AddressesLoader addressesLoader;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private CompaniesExportToCSVService companiesExportToCSVService;


    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting application...");

        long start = System.currentTimeMillis();

        List<CompanyData> companies = searchTableDataLoader.extractAll();

        List<CompanyData> companiesWithLogo = companies
                .stream().filter(CompanyData::hasImage).collect(Collectors.toList());

        remoteIdLoader.load(companiesWithLogo);

        addressesLoader.load(companiesWithLogo);

        companyPersisterService.persist(companiesWithLogo);

        companiesExportToCSVService.exportToCSV(companiesWithLogo);

        Stats.printSearchPageDataLoadStats(companies);
        Stats.printIdsFailedToResolveByName(companiesWithLogo);
        Stats.printAddressesLoadStats(companiesWithLogo);

        logger.info("---DONE--- Processing took {}secs", (System.currentTimeMillis() - start) / 1000);

        SpringApplication.exit(appContext, () -> 0);
    }
}
