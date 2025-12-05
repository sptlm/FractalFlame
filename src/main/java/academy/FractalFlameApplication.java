package com.fractal.flame;

import com.fractal.flame.config.ConfigParser;
import com.fractal.flame.config.FractalConfig;
import com.fractal.flame.render.FractalRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.commandline.CommandLine;
import picocli.commandline.Option;

import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * Главное приложение для генерации фрактального пламени.
 * Точка входа в программу.
 */
public class FractalFlameApplication implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(FractalFlameApplication.class);

    // Параметры для размера изображения
    @Option(names = {"-w", "--width"}, description = "Ширина изображения (по умолчанию: 1920)")
    private Integer width;

    @Option(names = {"-h", "--height"}, description = "Высота изображения (по умолчанию: 1080)")
    private Integer height;

    // Параметры для итераций и seed
    @Option(names = {"-i", "--iteration-count"}, description = "Количество итераций (по умолчанию: 2500)")
    private Integer iterationCount;

    @Option(names = {"--seed"}, description = "Начальное значение генератора (по умолчанию: 5)")
    private Long seed;

    // Параметры для трансформаций
    @Option(names = {"-f", "--functions"}, description = "Функции трансформации формата: swirl:1.0,horseshoe:0.8")
    private String functions;

    @Option(names = {"-ap", "--affine-params"}, description = "Аффинные параметры формата: a,b,c,d,e,f/a,b,c,d,e,f")
    private String affineParams;

    // Параметры для многопоточности и вывода
    @Option(names = {"-t", "--threads"}, description = "Количество потоков (по умолчанию: 1)")
    private Integer threads;

    @Option(names = {"-o", "--output-path"}, description = "Путь для сохранения изображения (по умолчанию: result.png)")
    private String outputPath;

    // Конфигурация из JSON файла
    @Option(names = {"--config"}, description = "Путь к JSON файлу конфигурации")
    private String configPath;

    // Параметры для гамма-коррекции (бонусное задание)
    @Option(names = {"-g", "--gamma-correction"}, description = "Включить гамма-коррекцию")
    private Boolean gammaCorrection;

    @Option(names = {"--gamma"}, description = "Значение гаммы для коррекции (по умолчанию: 2.2)")
    private Double gamma;

    // Параметры для симметрии (бонусное задание)
    @Option(names = {"-s", "--symmetry-level"}, description = "Уровень симметрии (по умолчанию: 1)")
    private Integer symmetryLevel;

    /**
     * Точка входа в приложение
     */
    public static void main(String[] args) {
        logger.info("Запуск приложения Fractal Flame Generator");
        int exitCode = new CommandLine(new FractalFlameApplication()).execute(args);
        System.exit(exitCode);
    }

    /**
     * Основной метод выполнения команды
     */
    @Override
    public Integer call() {
        try {
            logger.info("Инициализация конфигурации");
            
            // Загружаем конфигурацию с учетом приоритета:
            // 1. Консольные аргументы (CLI)
            // 2. JSON файл
            // 3. Параметры по умолчанию
            FractalConfig config = loadConfiguration();
            
            // Валидируем конфигурацию
            config.validate();
            logger.info("Конфигурация успешно загружена и валидирована");
            logger.info("Параметры: размер={}x{}, итерации={}, потоков={}, путь={}",
                    config.getSize().getWidth(),
                    config.getSize().getHeight(),
                    config.getIterationCount(),
                    config.getThreads(),
                    config.getOutputPath());

            // Создаем и запускаем renderer
            long startTime = System.currentTimeMillis();
            logger.info("Начало рендеринга фрактального пламени");
            
            FractalRenderer renderer = new FractalRenderer(config);
            renderer.render();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            logger.info("Рендеринг завершен успешно за {} мс ({} сек)",
                    duration, duration / 1000.0);
            logger.info("Изображение сохранено в: {}", config.getOutputPath());
            
            return 0;
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка валидации параметров: {}", e.getMessage());
            System.err.println("Ошибка: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при выполнении", e);
            System.err.println("Ошибка при выполнении: " + e.getMessage());
            e.printStackTrace(System.err);
            return 1;
        }
    }

    /**
     * Загружает конфигурацию с учетом приоритета параметров
     */
    private FractalConfig loadConfiguration() {
        FractalConfig config = new FractalConfig();

        // Сначала загружаем конфиг из JSON файла, если он указан
        if (configPath != null && !configPath.isEmpty()) {
            logger.debug("Загрузка конфигурации из файла: {}", configPath);
            config = ConfigParser.loadFromJson(Paths.get(configPath));
        }

        // Затем переопределяем значениями из CLI параметров
        if (width != null) {
            config.getSize().setWidth(width);
        }
        if (height != null) {
            config.getSize().setHeight(height);
        }
        if (iterationCount != null) {
            config.setIterationCount(iterationCount);
        }
        if (seed != null) {
            config.setSeed(seed);
        }
        if (threads != null) {
            config.setThreads(threads);
        }
        if (outputPath != null) {
            config.setOutputPath(outputPath);
        }
        if (functions != null) {
            config.setFunctions(ConfigParser.parseFunctions(functions));
        }
        if (affineParams != null) {
            config.setAffineParams(ConfigParser.parseAffineParams(affineParams));
        }
        if (gammaCorrection != null) {
            config.setGammaCorrection(gammaCorrection);
        }
        if (gamma != null) {
            config.setGamma(gamma);
        }
        if (symmetryLevel != null) {
            config.setSymmetryLevel(symmetryLevel);
        }

        return config;
    }
}
