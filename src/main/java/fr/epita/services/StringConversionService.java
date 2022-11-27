package fr.epita.services;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface StringConversionService {
    List<Map<String, Object>> convert(File content);
}
