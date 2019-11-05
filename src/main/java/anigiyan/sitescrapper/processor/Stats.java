package anigiyan.sitescrapper.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Developer: nigiyan
 * Date: 05/11/2019
 */

public class Stats {

    private static final Logger logger = LoggerFactory.getLogger(Stats.class);

    public static void printSearchPageDataLoadStats(Collection<CompanyData> companyData) {
        logger.info("---STATS--- Total companies fetched: {}, Companies with logo: {}", companyData.size(), companyData.stream().filter(CompanyData::hasImage).count());
    }

    public static void printIdsFailedToResolveByName(Collection<CompanyData> companyData) {
        List<String> namesForWhichFailedToResolveId = companyData.stream().filter(it -> !it.hasRemoteId()).map(CompanyData::getName).collect(Collectors.toList());
        if (namesForWhichFailedToResolveId.isEmpty()) {
            logger.info("---STATS--- All companies IDs has been resolved by name.");
        } else {
            logger.warn("---STATS WARN--- Failed to resolve IDs for companies:\n{}", namesForWhichFailedToResolveId);
        }
    }

    public static void printAddressesLoadStats(Collection<CompanyData> companyData) {
        List<Long> companiesForWhichAddressDoNotExist = companyData.stream().filter(it -> !it.hasAddress()).map(CompanyData::getRemoteId).collect(Collectors.toList());
        if (companiesForWhichAddressDoNotExist.isEmpty()) {
            logger.info("---STATS--- For all companies addresses exists");
        } else {
            logger.info("---STATS--- For the following companies address does not exist:\n{}", companiesForWhichAddressDoNotExist);
        }
    }
}
