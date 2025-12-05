package academy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

/**
 * Не по тз, написал для себя, чтобы перебирать быстро конфиги и искать красивые генерации:]
 */
public class ConfigGenerator {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();

    private static final String[] AVAILABLE_FUNCTIONS = {
        "sinusoidal",
        "spherical",
        "swirl",
        "horseshoe",
        "diamond",
        "polar",
        "waves",
        "spiral",
        "julia",
        "disc",
        "hyperbolic",
        "cosine",
        "tangent",
        "power",
        "exponential",
        "heart",
        "shell",
        "whirlpool",
        "radial",
        "blob",
        "crosshatch",
        "logarithmic",
        "super_shape",
        "eyefish",
        "bubble",
        "modulus",
        "perspective",
        "rotate",
        "crackle",
        "blur"
    };

    public static String generateRandomConfig(String outputPath) {
        Map<String, Object> config = new LinkedHashMap<>();

        Map<String, Integer> size = new LinkedHashMap<>();
        size.put("width", 4000);
        size.put("height", 4000);
        config.put("size", size);

        config.put("iteration_count", 50_000_000);
        config.put("output_path", outputPath);
        config.put("threads", 16);

        config.put("seed", random.nextLong());
        config.put("gamma_correction", true);
        config.put("gamma", 2.2);
        config.put("symmetry_level", 1 + random.nextInt(8));

        int functionCount = random.nextInt(8) + 2;
        List<Map<String, Object>> functions = generateRandomFunctions(functionCount);
        config.put("functions", functions);

        int affineCount = random.nextInt(6) + 2;
        List<Map<String, Double>> affineParams = generateRandomAffineParams(affineCount);

        config.put("affine_params", affineParams);

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации конфигурации: " + e.getMessage(), e);
        }
    }

    private static List<Map<String, Object>> generateRandomFunctions(int count) {
        List<Map<String, Object>> functions = new ArrayList<>();
        Set<String> usedFunctions = new HashSet<>();

        // случайные функции без повторений
        while (usedFunctions.size() < count && usedFunctions.size() < AVAILABLE_FUNCTIONS.length) {
            String func = AVAILABLE_FUNCTIONS[random.nextInt(AVAILABLE_FUNCTIONS.length)];
            usedFunctions.add(func);
        }

        for (String funcName : usedFunctions) {
            Map<String, Object> funcMap = new LinkedHashMap<>();
            funcMap.put("name", funcName);
            // Вес в диапазоне [0.1, 1.5]
            funcMap.put("weight", Math.round((0.1 + random.nextDouble() * 1.4) * 100.0) / 100.0);
            functions.add(funcMap);
        }

        return functions;
    }

    private static List<Map<String, Double>> generateRandomAffineParams(int count) {
        List<Map<String, Double>> affineParams = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Map<String, Double> params = new LinkedHashMap<>();

            double a, b, d, e;
            double scale;

            // Повторяем, пока не получим сжимающее преобразование
            do {
                a = randomCoeff();
                b = randomCoeff();
                d = randomCoeff();
                e = randomCoeff();

                scale = Math.max(
                        Math.sqrt(a * a + d * d),
                        Math.sqrt(b * b + e * e)
                );
            } while (scale >= 1.0);

            params.put("a", Math.round(a * 1000000.0) / 1000000.0);
            params.put("b", Math.round(b * 1000000.0) / 1000000.0);
            params.put("d", Math.round(d * 1000000.0) / 1000000.0);
            params.put("e", Math.round(e * 1000000.0) / 1000000.0);

            // Трансляционные параметры (в диапазоне [-1, 1])
            double c = -1.0 + random.nextDouble() * 2.0;
            double f = -1.0 + random.nextDouble() * 2.0;

            params.put("c", Math.round(c * 1000000.0) / 1000000.0);
            params.put("f", Math.round(f * 1000000.0) / 1000000.0);

            affineParams.add(params);
        }

        return affineParams;
    }

    private static double randomCoeff() {
        return -1.0 + random.nextDouble() * 2.0;
    }

    public static void saveConfigToFile(String configJson, String filePath) {
        try {
            File file = new File(filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, mapper.readValue(configJson, Object.class));
            System.out.println("Конфигурация сохранена в: " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении конфигурации: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String randomConfig = generateRandomConfig("random_flame.png");

        System.out.println("=== СГЕНЕРИРОВАННАЯ КОНФИГУРАЦИЯ ===");
        System.out.println(randomConfig);

        saveConfigToFile(randomConfig, "random_config.json");
    }
}
