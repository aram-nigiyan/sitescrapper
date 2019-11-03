package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.Configs;
import anigiyan.sitescrapper.ExecutorsPool;
import anigiyan.sitescrapper.ResourceLoader;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

@Component
public class RemoteIdLoader {

    private static final Logger logger = LoggerFactory.getLogger(RemoteIdLoader.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Configs configs;

    @Autowired
    private ExecutorsPool executorsPool;

    public void load(Collection<CompanyData> companies) {
        Collection<Future<CompanyData>> futures = new ArrayList<>(companies.size());

        for (CompanyData company : companies) {
            Future<CompanyData> future = executorsPool.getExecutorService().submit(() -> {
                company.setRemoteId(load(company.getName()));
                logger.debug("Company name: {}, resolved id: {}", company.getName(), company.getRemoteId());
                return company;
            });
            futures.add(future);
        }

        for (Future<CompanyData> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("", e);
            }
        }
        logger.info("IDs loading completed for companies: {}", futures.size());
    }

    public Long load(String name) {
        Long id = null;
        try {
            String response = new String(resourceLoader.load(configs.getCmpIdRequestUrl() + URLEncoder.encode(name, "UTF-8")));

            id = Long.parseLong(JsonPath.read(response, "$.list[0].id"));

        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return id;
    }
}
