package anigiyan.sitescrapper;

import anigiyan.sitescrapper.model.Company;
import anigiyan.sitescrapper.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Component
public class Runner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    @Autowired
    CompanyRepository companyRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting application : {}", args.getOptionNames());

        Optional<Company> byId = companyRepository.findById(4L);
        logger.info("logo has size: " + byId.get().getLogo().getData().length);
    }
}
