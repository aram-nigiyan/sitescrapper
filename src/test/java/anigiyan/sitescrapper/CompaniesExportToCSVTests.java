package anigiyan.sitescrapper;

import anigiyan.sitescrapper.processor.CompanyData;
import anigiyan.sitescrapper.service.CompaniesExportToCSVService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

/**
 * Developer: nigiyan
 * Date: 05/11/2019
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootApp.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class CompaniesExportToCSVTests {

    @Autowired
    CompaniesExportToCSVService companiesExporterService;

    @Test
    public void exportToCSVTest() {
        List<CompanyData> companyData = new ArrayList<>();
        LongStream.rangeClosed(1, 10).forEach(it -> companyData.add(new CompanyData("Aram Nigiyan", "here must be companiy's logo".getBytes(), "Yerevan Artsakh ave, 18/" + it + ", \\'Armenia\\')")));

        Assert.assertTrue(companiesExporterService.exportToCSV(companyData));

        Assert.assertTrue(companiesExporterService.readCSV());
    }

}
