package anigiyan.sitescrapper.service;

import anigiyan.sitescrapper.app.config.Configs;
import anigiyan.sitescrapper.processor.CompanyData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Developer: nigiyan
 * Date: 05/11/2019
 */

@Service
public class CompaniesExportToCSVService {

    private static Logger logger = LoggerFactory.getLogger(CompaniesExportToCSVService.class);

    @Autowired
    private Configs configs;

    public boolean exportToCSV(Iterable<CompanyData> companyData) {
        logger.info("Exporting companies to CSV file: ", configs.getCsvFileName());

        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(configs.getCsvFileName()));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("Name", "Address", "Logo"))
        ) {

            for (CompanyData company : companyData) {
                String imageAsString = Base64.getEncoder().encodeToString(company.getImage());
                csvPrinter.printRecord(company.getName(), company.getAddress(), imageAsString);
                logger.trace("{}, {}, {}", company.getName(), company.getAddress(), imageAsString);
            }
            csvPrinter.flush();
            logger.info("Exporting completed");

            return true;
        } catch (IOException io) {
            logger.error("", io);
            removeFile();
        }
        return false;
    }

    public boolean readCSV() {
        logger.info("Reading companies from CSV file: ", configs.getCsvFileName());

        try (
                Reader reader = Files.newBufferedReader(Paths.get(configs.getCsvFileName()));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            for (CSVRecord csvRecord : csvParser) {
                String name = csvRecord.get("Name");
                String address = csvRecord.get("Address");
                String logo = csvRecord.get("Logo");

                logger.trace("{}, {}, {}", name, address, logo);
            }
            logger.info("Reading completed");

            return true;
        } catch (IOException e) {
            logger.error("", e);
        }

        return false;
    }

    private void removeFile() {
        File file = new File(configs.getCsvFileName());
        if (file.isFile()) {
            if (file.delete()) {
                logger.info("CSV File deleted");
            }
        }
    }
}
