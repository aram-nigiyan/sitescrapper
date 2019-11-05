package anigiyan.sitescrapper.service;

import anigiyan.sitescrapper.model.Company;
import anigiyan.sitescrapper.model.Logo;
import anigiyan.sitescrapper.processor.CompanyData;
import anigiyan.sitescrapper.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Service
public class CompanyPersisterService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyPersisterService.class);

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyPersisterService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public void persist(Collection<CompanyData> companyData) {
        logger.debug("Starting saving companies to database");
        companyData.parallelStream().forEach(it -> companyRepository.save(new Company(it.getName(), it.getAddress(), new Logo(it.getImage()), it.getRemoteId())));
        logger.debug("Database persistence completed");
    }
}
