package academy.validator;

import academy.config.AffineTransform;
import academy.config.FractalConfig;
import academy.config.ImageSize;
import academy.config.TransformFunction;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigValidator {

    private static final Logger logger = LoggerFactory.getLogger(ConfigValidator.class);

    public static void validateConfig(FractalConfig config) {
        logger.debug("Валидация конфигурации");

        if (config == null) {
            throw new IllegalArgumentException("FractalConfig не может быть null");
        }

        validateImageSize(config.getSize());
        validateIterationCount(config.getIterationCount());
        validateThreadCount(config.getThreads());
        validateOutputPath(config.getOutputPath());
        validateFunctions(config.getFunctions());
        validateAffineParams(config.getAffineParams());
        validateGamma(config.getGamma());
        validateSymmetryLevel(config.getSymmetryLevel());
    }

    public static void validateImageSize(ImageSize size) {
        if (size == null) {
            throw new IllegalArgumentException("Размер изображения не может быть null");
        }
        if (size.getWidth() <= 0 || size.getHeight() <= 0) {
            throw new IllegalArgumentException(String.format(
                    "Размер изображения должен быть положительным, получено: %dx%d",
                    size.getWidth(), size.getHeight()));
        }
        if (size.getWidth() > 10000 || size.getHeight() > 10000) {
            logger.warn("Размер изображения очень большой, Возможно потребуется выделить JVM больше памяти");
        }
    }

    public static void validateIterationCount(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество итераций должно быть положительным, получено: " + count);
        }
    }

    public static void validateThreadCount(int threads) {
        if (threads <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть положительным, получено: " + threads);
        }
    }

    public static void validateOutputPath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Путь для сохранения не может быть пустым");
        }
        if (!path.endsWith(".png")) {
            throw new IllegalArgumentException("Выходной файл должен иметь расширение .png, получено: " + path);
        }
    }

    public static void validateFunctions(List<TransformFunction> functions) {
        if (functions == null || functions.isEmpty()) {
            throw new IllegalArgumentException("Должна быть хотя бы одна функция трансформации");
        }
        for (TransformFunction func : functions) {
            validateTransformFunction(func);
        }
    }

    public static void validateTransformFunction(TransformFunction func) {
        if (func == null) {
            throw new IllegalArgumentException("TransformFunction не может быть null");
        }
        if (func.getName() == null || func.getName().isEmpty()) {
            throw new IllegalArgumentException("Имя функции не может быть пустым");
        }
        if (func.getWeight() <= 0) {
            throw new IllegalArgumentException("Вес функции должен быть положительным, получено: " + func.getWeight());
        }
    }

    public static void validateAffineParams(List<AffineTransform> params) {
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("Должно быть хотя бы одно аффинное преобразование");
        }
        for (AffineTransform transform : params) {
            validateAffineTransform(transform);
        }
    }

    public static void validateAffineTransform(AffineTransform transform) {
        if (transform == null) {
            throw new IllegalArgumentException("AffineTransform не может быть null");
        }
        double determinant = transform.getA() * transform.getE() - transform.getB() * transform.getD();
        if (Math.abs(determinant) < 0.00001) {
            throw new IllegalArgumentException(
                    "Аффинное преобразование имеет околонулевой детерминант, это может привести к проблемам: det="
                            + determinant);
        }
    }

    public static void validateGamma(double gamma) {
        if (gamma <= 0) {
            throw new IllegalArgumentException("Значение гаммы должно быть положительным, получено: " + gamma);
        }
        if (gamma > 10.0) {
            logger.warn("Значение гаммы очень большое ({}), изображение может быть пересвеченным", gamma);
        }
    }

    public static void validateSymmetryLevel(int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Уровень симметрии должен быть >= 1, получено: " + level);
        }
        if (level > 360) {
            logger.warn("Уровень симметрии очень большой ({}), это может замедлить рендеринг", level);
        }
    }
}
