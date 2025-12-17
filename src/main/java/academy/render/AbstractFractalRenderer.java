package academy.render;

import academy.config.AffineTransform;
import academy.config.FractalConfig;
import academy.config.TransformFunction;
import academy.transform.FlameFunction;
import academy.transform.FlameFunctionFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFractalRenderer implements Renderer {
    private static final Logger logger = LoggerFactory.getLogger(AbstractFractalRenderer.class);

    protected static final double X_MIN = -1;
    protected static final double X_MAX = 1;
    protected static final double Y_MIN = -1;
    protected static final double Y_MAX = 1;
    protected static final double X_RANGE = X_MAX - X_MIN;
    protected static final double Y_RANGE = Y_MAX - Y_MIN;

    protected static final int BURN_IN_ITERATIONS = 20;

    protected final Map<Integer, byte[]> affineColors = new HashMap<>();

    protected void initAffineColors(FractalConfig config) {
        // Создаем детерминированный Random для цветов на основе seed
        Random colorRandom = new Random(config.getSeed());

        for (int i = 0; i < config.getAffineParams().size(); i++) {
            byte r = (byte) colorRandom.nextInt(256);
            byte g = (byte) colorRandom.nextInt(256);
            byte b = (byte) colorRandom.nextInt(256);
            affineColors.put(i, new byte[] {r, g, b});
        }

        logger.debug("Инициализировано {} цветов для аффинных преобразований", affineColors.size());
    }

    protected double[] applyVariationComposition(double x, double y, FractalConfig config) {
        double totalWeight = 0.0;
        double xAccum = 0.0;
        double yAccum = 0.0;

        // Применяем каждую вариацию с её весом и складываем результаты
        for (TransformFunction transformFunc : config.getFunctions()) {
            FlameFunction variation = FlameFunctionFactory.getFunction(transformFunc.getName());
            double[] varResult = variation.apply(x, y);
            double weight = transformFunc.getWeight();

            xAccum += varResult[0] * weight;
            yAccum += varResult[1] * weight;
            totalWeight += weight;
        }

        // Нормализуем на сумму весов
        if (totalWeight > 0) {
            xAccum /= totalWeight;
            yAccum /= totalWeight;
        }

        return new double[] {xAccum, yAccum};
    }

    protected void updatePixelBuffer(
            double x, double y, int transformIndex, byte[][][] colorBuffer, int[][] hitCount, FractalConfig config) {
        // Преобразуем координаты из пространства фрактала в пиксельные координаты
        int pixelX = (int) ((x - X_MIN) / X_RANGE * (config.getSize().getWidth() - 1));
        int pixelY = (int) ((y - Y_MIN) / Y_RANGE * (config.getSize().getHeight() - 1));

        byte[] color = affineColors.get(transformIndex);

        // Применяем симметрию если требуется
        if (config.getSymmetryLevel() > 1) {
            applySymmetry(pixelX, pixelY, color, colorBuffer, hitCount, config);
        } else {
            if (isValidPixel(pixelX, pixelY, config)) {
                updatePixel(pixelX, pixelY, color, colorBuffer, hitCount);
            }
        }
    }

    protected void applySymmetry(
            int pixelX, int pixelY, byte[] color, byte[][][] colorBuffer, int[][] hitCount, FractalConfig config) {
        int centerX = config.getSize().getWidth() / 2;
        int centerY = config.getSize().getHeight() / 2;

        // Координаты относительно центра
        double relX = pixelX - centerX;
        double relY = pixelY - centerY;

        // Применяем поворот для каждого уровня симметрии
        for (int i = 0; i < config.getSymmetryLevel(); i++) {
            double angle = 2.0 * Math.PI * i / config.getSymmetryLevel();
            double cosA = Math.cos(angle);
            double sinA = Math.sin(angle);

            // Поворачиваем точку вокруг центра
            double rotX = relX * cosA - relY * sinA;
            double rotY = relX * sinA + relY * cosA;

            int finalX = (int) (rotX + centerX);
            int finalY = (int) (rotY + centerY);

            if (isValidPixel(finalX, finalY, config)) {
                updatePixel(finalX, finalY, color, colorBuffer, hitCount);
            }
        }
    }

    protected boolean isValidPixel(int x, int y, FractalConfig config) {
        return x >= 0
                && x < config.getSize().getWidth()
                && y >= 0
                && y < config.getSize().getHeight();
    }

    protected void updatePixel(int x, int y, byte[] newColor, byte[][][] colorBuffer, int[][] hitCount) {
        int hits = hitCount[y][x];

        if (hits == 0) {
            // Первое попадание - просто записываем цвет
            colorBuffer[y][x][0] = newColor[0];
            colorBuffer[y][x][1] = newColor[1];
            colorBuffer[y][x][2] = newColor[2];
        } else {
            // Усредняем с существующим цветом
            int oldR = colorBuffer[y][x][0] & 0xFF;
            int oldG = colorBuffer[y][x][1] & 0xFF;
            int oldB = colorBuffer[y][x][2] & 0xFF;

            int newR = newColor[0] & 0xFF;
            int newG = newColor[1] & 0xFF;
            int newB = newColor[2] & 0xFF;

            colorBuffer[y][x][0] = (byte) ((oldR + newR) / 2);
            colorBuffer[y][x][1] = (byte) ((oldG + newG) / 2);
            colorBuffer[y][x][2] = (byte) ((oldB + newB) / 2);
        }

        hitCount[y][x]++;
    }

    protected double[] performBurnIn(Random random, FractalConfig config) {
        double x = 0.0;
        double y = 0.0;

        for (int i = 0; i < BURN_IN_ITERATIONS; i++) {
            int transformIndex = random.nextInt(config.getAffineParams().size());
            AffineTransform affineTransform = config.getAffineParams().get(transformIndex);
            double[] affineResult = affineTransform.transform(x, y);
            double[] finalResult = applyVariationComposition(affineResult[0], affineResult[1], config);
            x = finalResult[0];
            y = finalResult[1];
        }

        return new double[] {x, y};
    }

    protected void applyColorCorrection(byte[][][] colorBuffer, int[][] hitCount, FractalConfig config) {
        logger.info("Применение цветовой коррекции");
        long startTime = System.currentTimeMillis();
        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();

        int maxHits = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (hitCount[y][x] > maxHits) {
                    maxHits = hitCount[y][x];
                }
            }
        }

        if (maxHits == 0) {
            logger.warn("Максимальное число попаданий равно 0, изображение пусто!");
            return;
        }

        double normalizedMaxIntensity = Math.log(maxHits);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int hits = hitCount[y][x];
                if (hits == 0) {
                    continue;
                }

                double normalizedIntensity = Math.log(hits) / normalizedMaxIntensity;

                double brightness;
                if (config.isGammaCorrection()) {
                    double gamma = config.getGamma();
                    brightness = Math.pow(normalizedIntensity, 1.0 / gamma);
                } else {
                    brightness = normalizedIntensity;
                }

                int r = colorBuffer[y][x][0] & 0xFF;
                int g = colorBuffer[y][x][1] & 0xFF;
                int b = colorBuffer[y][x][2] & 0xFF;

                colorBuffer[y][x][0] = (byte) Math.min(255, (int) (r * brightness));
                colorBuffer[y][x][1] = (byte) Math.min(255, (int) (g * brightness));
                colorBuffer[y][x][2] = (byte) Math.min(255, (int) (b * brightness));
            }
        }

        long endTime = System.currentTimeMillis();
        logger.debug("Применение цветовой коррекции завершено за {} мс", endTime - startTime);
    }

    protected Logger getLogger() {
        return logger;
    }
}
