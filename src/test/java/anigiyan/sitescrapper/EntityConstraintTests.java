package anigiyan.sitescrapper;

import anigiyan.sitescrapper.model.Company;
import anigiyan.sitescrapper.repository.CompanyRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EntityConstraintTests {

    private static Logger logger = LoggerFactory.getLogger(EntityConstraintTests.class);


    @Autowired
    private CompanyRepository repository;


    @Test
    public void whenEntityInvalid_thenThrowsException() {
        Company o = new Company("", null, null);

        try {
            repository.saveAndFlush(o);

            Assert.fail("save should be failed because of constraints");

        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> cv = e.getConstraintViolations();

            Assert.assertEquals(3, cv.size());
        }
    }
}

