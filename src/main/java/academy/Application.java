package academy;

import academy.config.ConfigParser;
import academy.config.FractalConfig;
import academy.render.MultiThreadedRenderer;
import academy.render.Renderer;
import academy.render.SingleThreadedRenderer;
import academy.util.ImageWriter;
import academy.validator.ConfigValidator;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Application implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Option(
            names = {"-w", "--width"},
            description = "Ширина изображения (по умолчанию: 1920)")
    private Integer width;

    @Option(
            names = {"-h", "--height"},
            description = "Высота изображения (по умолчанию: 1080)")
    private Integer height;

    @Option(
            names = {"-i", "--iteration-count"},
            description = "Количество итераций (по умолчанию: 2500)")
    private Integer iterationCount;

    @Option(
            names = {"--seed"},
            description = "Начальное значение генератора (по умолчанию: 5)")
    private Long seed;

    @Option(
            names = {"-f", "--functions"},
            description = "Функции трансформации формата: swirl:1.0,horseshoe:0.8")
    private String functions;

    @Option(
            names = {"-ap", "--affine-params"},
            description = "Аффинные параметры формата: a,b,c,d,e,f/a,b,c,d,e,f")
    private String affineParams;

    @Option(
            names = {"-t", "--threads"},
            description = "Количество потоков (по умолчанию: 1)")
    private Integer threads;

    @Option(
            names = {"-o", "--output-path"},
            description = "Путь для сохранения изображения (по умолчанию: result.png)")
    private String outputPath;

    @Option(
            names = {"-c", "--config"},
            description = "Путь к JSON файлу конфигурации")
    private String configPath;

    @Option(
            names = {"-g", "--gamma-correction"},
            description = "Включить гамма-коррекцию")
    private Boolean gammaCorrection;

    @Option(
            names = {"--gamma"},
            description = "Значение гаммы для коррекции (по умолчанию: 2.2)")
    private Double gamma;

    @Option(
            names = {"-s", "--symmetry-level"},
            description = "Уровень симметрии (по умолчанию: 1)")
    private Integer symmetryLevel;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        try {
            logger.info("Инициализация конфигурации");

            FractalConfig config = loadConfiguration();

            ConfigValidator.validateConfig(config);
            logger.info(
                    "Параметры: размер={}x{}, итерации={}, потоков={}, путь={}",
                    config.getSize().getWidth(),
                    config.getSize().getHeight(),
                    config.getIterationCount(),
                    config.getThreads(),
                    config.getOutputPath());

            Renderer renderer;
            if (config.getThreads() > 1) {
                renderer = new MultiThreadedRenderer();
            } else {
                renderer = new SingleThreadedRenderer();
            }
            byte[][][] imagePixels = renderer.render(config);
            ImageWriter.saveImage(imagePixels, config.getOutputPath());

            return 0;
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка валидации параметров: {}", e.getMessage());
            System.err.println("Ошибка: " + e.getMessage());
            return 2;
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при выполнении", e);
            System.err.println("Ошибка при выполнении: " + e.getMessage());
            e.printStackTrace(System.err);
            return 1;
        }
    }

    private FractalConfig loadConfiguration() {
        FractalConfig config = new FractalConfig();

        if (configPath != null && !configPath.isEmpty()) {
            logger.debug("Загрузка конфигурации из файла: {}", configPath);
            config = ConfigParser.loadFromJson(Path.of(configPath));
        }

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
