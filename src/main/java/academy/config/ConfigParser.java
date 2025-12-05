package com.fractal.flame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсер для загрузки конфигурации из JSON файла и CLI параметров
 */
public class ConfigParser {
    private static final Logger logger = LoggerFactory.getLogger(ConfigParser.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Загружает конфигурацию из JSON файла
     * @param path путь к файлу конфигурации
     * @return загруженная конфигурация
     */
    public static FractalConfig loadFromJson(Path path) {
        try {
            logger.debug("Загрузка JSON конфигурации из: {}", path.toString());
            String content = Files.readString(path);
            FractalConfig config = objectMapper.readValue(content, FractalConfig.class);
            logger.debug("JSON конфигурация успешно загружена");
            return config;
        } catch (Exception e) {
            logger.error("Ошибка при загрузке JSON конфигурации: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось загрузить конфигурацию из файла " + path + ": " + e.getMessage(), e);
        }
    }

    /**
     * Парсит функции трансформации из строки формата "swirl:1.0,horseshoe:0.8"
     * @param functionsStr строка с функциями
     * @return список функций трансформации
     */
    public static List<TransformFunction> parseFunctions(String functionsStr) {
        List<TransformFunction> functions = new ArrayList<>();
        try {
            logger.debug("Парсинг функций из строки: {}", functionsStr);
            String[] parts = functionsStr.split(",");
            for (String part : parts) {
                part = part.trim();
                String[] funcParts = part.split(":");
                if (funcParts.length != 2) {
                    throw new IllegalArgumentException("Некорректный формат функции: " + part);
                }
                String name = funcParts[0].trim();
                double weight = Double.parseDouble(funcParts[1].trim());
                functions.add(new TransformFunction(name, weight));
                logger.debug("Добавлена функция: {} с весом {}", name, weight);
            }
        } catch (NumberFormatException e) {
            logger.error("Ошибка при парсинге веса функции: {}", e.getMessage());
            throw new IllegalArgumentException("Некорректный формат веса функции: " + e.getMessage(), e);
        }
        return functions;
    }

    /**
     * Парсит аффинные параметры из строки формата "a,b,c,d,e,f/a,b,c,d,e,f"
     * @param paramsStr строка с аффинными параметрами
     * @return список аффинных преобразований
     */
    public static List<AffineTransform> parseAffineParams(String paramsStr) {
        List<AffineTransform> transforms = new ArrayList<>();
        try {
            logger.debug("Парсинг аффинных параметров из строки: {}", paramsStr);
            String[] transformParts = paramsStr.split("/");
            for (String transformStr : transformParts) {
                transformStr = transformStr.trim();
                String[] params = transformStr.split(",");
                if (params.length != 6) {
                    throw new IllegalArgumentException("Аффинное преобразование должно содержать ровно 6 параметров, получено: " + params.length);
                }
                double a = Double.parseDouble(params[0].trim());
                double b = Double.parseDouble(params[1].trim());
                double c = Double.parseDouble(params[2].trim());
                double d = Double.parseDouble(params[3].trim());
                double e = Double.parseDouble(params[4].trim());
                double f = Double.parseDouble(params[5].trim());
                transforms.add(new AffineTransform(a, b, c, d, e, f));
                logger.debug("Добавлено аффинное преобразование: a={}, b={}, c={}, d={}, e={}, f={}", a, b, c, d, e, f);
            }
        } catch (NumberFormatException e) {
            logger.error("Ошибка при парсинге числового значения: {}", e.getMessage());
            throw new IllegalArgumentException("Некорректный формат аффинных параметров: " + e.getMessage(), e);
        }
        return transforms;
    }
}
