package anigiyan.sitescrapper.processor.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Developer: nigiyan
 * Date: 05/11/2019
 */

public class CompanyListReader {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static CompanyListDto readFromJson(String json) {
        try {
            return objectMapper.readValue(json, CompanyListDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
