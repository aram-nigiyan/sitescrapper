package anigiyan.sitescrapper.processor;

import anigiyan.sitescrapper.app.Configs;
import anigiyan.sitescrapper.app.ExecutorsPool;
import anigiyan.sitescrapper.app.ResourceLoader;
import anigiyan.sitescrapper.processor.webapi.CompanyDto;
import anigiyan.sitescrapper.processor.webapi.CompanyListReader;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */

@Component
public class RemoteIdLoader {

    private static final Logger logger = LoggerFactory.getLogger(RemoteIdLoader.class);

    private static final Pattern MULTIPLE_SPACE_PATTERN = Pattern.compile(" +");

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
        String response = "";
        Long id = null;
        try {
            response = new String(resourceLoader.load(configs.getCmpIdRequestUrl() + URLEncoder.encode(name, "UTF-8")));

            id = Long.parseLong(JsonPath.read(response, "$.list[0].id"));

        } catch (UnsupportedEncodingException | PathNotFoundException e) {
            logger.warn("Failed getting ID by name: {} by response: {}, falling back to batch query", name, response);
            try {
                id = fallbackResolve(name);
            } catch (UnsupportedEncodingException | IllegalStateException e1) {
                logger.error("Unable to fallback resolve ID by name: " + name, e1);
            }

        }
        return id;
    }

    // search by longest word in name, then trim spaces in response and extract ID matching name
    private Long fallbackResolve(String name) throws UnsupportedEncodingException {
        String longestName = Arrays.stream(name.split(" ")).max(Comparator.comparingInt(String::length)).orElseThrow(IllegalStateException::new);

        String response = new String(resourceLoader.load(configs.getCmpIdRequestUrl() + URLEncoder.encode(longestName, "UTF-8")));

        response = MULTIPLE_SPACE_PATTERN.matcher(response.trim()).replaceAll(" ");
        Optional<CompanyDto> companyDto = CompanyListReader.readFromJson(response).getList().stream().filter(it -> it.getName().equals(name)).findFirst();
        return companyDto.map(CompanyDto::getId).orElse(null);
    }
}
