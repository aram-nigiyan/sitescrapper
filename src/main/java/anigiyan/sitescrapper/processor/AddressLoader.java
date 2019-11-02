package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.Configs;
import anigiyan.sitescrapper.ResourceLoader;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

@Component
public class AddressLoader {

    private static final Logger logger = LoggerFactory.getLogger(AddressLoader.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Configs configs;

    public void load(List<CompanyData> companies) {
        companies.stream().parallel().forEach(company -> {
            resourceLoader.load(configs.getCmpIdRequestUrl() + company.getName());
        });
    }

    public Integer load(String name) {
        Integer id = null;
        try {
            String response = new String(resourceLoader.load(configs.getCmpIdRequestUrl() + URLEncoder.encode(name, "UTF-8")));

            id = Integer.parseInt(JsonPath.read(response, "$.list[0].id"));

        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return id;
    }
}
