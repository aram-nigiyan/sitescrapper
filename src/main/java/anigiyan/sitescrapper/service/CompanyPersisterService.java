package anigiyan.sitescrapper.service;

import anigiyan.sitescrapper.model.Company;
import anigiyan.sitescrapper.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Developer: nigiyan
 * Date: 01/11/2019
 */

@Service
public class CompanyPersisterService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyPersisterService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    void persist(Company company) {
        companyRepository.saveAndFlush(company);
    }
}
