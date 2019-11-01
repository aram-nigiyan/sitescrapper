package anigiyan.sitescrapper;

import anigiyan.sitescrapper.model.Company;
import anigiyan.sitescrapper.model.Logo;
import anigiyan.sitescrapper.repository.CompanyRepository;
import anigiyan.sitescrapper.repository.LogoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.show_sql=true"
})
public class RepositoryTests {

    private static Logger logger = LoggerFactory.getLogger(RepositoryTests.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private LogoRepository logoRepository;

    @Test
    public void testQueryNonExistedRepository() {
        Assert.assertNotNull(companyRepository);

        Optional<Company> workbookOp = companyRepository.findById(1L);
        Assert.assertNull(workbookOp.orElse(null));
    }

    @Test
    public void testRepository() {
        Logo logo = new Logo(new byte[]{1, 2, 3});

        Company o = new Company("some company", "addr", logo);

        Company save = companyRepository.save(o);

        Assert.assertNotNull(save.getId());
        Assert.assertNotNull(companyRepository.findById(save.getId()).orElse(null));

    }
}

