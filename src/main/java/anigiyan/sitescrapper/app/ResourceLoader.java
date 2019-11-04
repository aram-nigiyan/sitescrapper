package anigiyan.sitescrapper.app;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Developer: nigiyan
 * Date: 02/11/2019
 */


public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    private CloseableHttpClient httpClient;

    ResourceLoader(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public byte[] load(String uri) {
        logger.debug("Loading resource for uri: {}", StringUtils.abbreviate(uri, 60));
        HttpUriRequest request = new HttpGet(uri);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            response.getEntity().writeTo(outstream);
            outstream.flush();
        } catch (IOException e) {
            logger.error("", e);
        }

        return outstream.size() == 0 ? null : outstream.toByteArray();
    }

    @PreDestroy
    private void destroy() throws IOException {
        logger.info("Closing HTTP Client");
        httpClient.close();
    }
}
