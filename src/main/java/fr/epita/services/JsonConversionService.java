package fr.epita.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonConversionService implements StringConversionService {
    private final Logger LOGGER = LogManager.getLogger(JsonConversionService.class);

    @Override
    public List<Map<String, Object>> convert(File content) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, Object>> data = mapper.readValue(content, new TypeReference<>() {
            });
            return data;
        } catch (Exception e) {
            LOGGER.error("error while converting json", e);
        }
        return null;
    }
}
