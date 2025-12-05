package academy.render;

import academy.config.AffineTransform;
import academy.config.FractalConfig;
import academy.config.TransformFunction;
import academy.transform.FlameFunction;
import academy.transform.FlameFunctionFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FractalRenderer {

    private static final Logger logger = LoggerFactory.getLogger(FractalRenderer.class);

    private final FractalConfig config;

    private final byte[][][] colorBuffer;

    // Интенсивность
    private int[][] hitCount;

    private static final double X_MIN = -1;
    private static final double X_MAX = 1;
    private static final double Y_MIN = -1;
    private static final double Y_MAX = 1;
    private static final double X_RANGE = X_MAX - X_MIN;
    private static final double Y_RANGE = Y_MAX - Y_MIN;

    private static final int BURN_IN_ITERATIONS = 20;

    private final Map<Integer, byte[]> affineColors = new HashMap<>();

    public FractalRenderer(FractalConfig config) {
        this.config = config;
        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();

        this.colorBuffer = new byte[height][width][3];
        this.hitCount = new int[height][width];

        initAffineColors();
    }

    private void initAffineColors() {
        Random colorRandom = new Random(config.getSeed());
        for (int i = 0; i < config.getAffineParams().size(); i++) {
            byte r = (byte) colorRandom.nextInt(256);
            byte g = (byte) colorRandom.nextInt(256);
            byte b = (byte) colorRandom.nextInt(256);
            affineColors.put(i, new byte[] {r, g, b});
        }
    }

    public byte[][][] render() {
        if (config.getThreads() > 1) {
            renderMultithreaded();
        } else {
            renderSinglethreaded();
        }

        applyColorCorrection();
        hitCount = null;
        System.gc();

        return colorBuffer;
    }

    private void renderSinglethreaded() {
        logger.info("Начало однопоточного рендеринга");
        long startTime = System.currentTimeMillis();

        Random random = new Random(config.getSeed());
        double x = 0.0;
        double y = 0.0;

        logger.debug("Выполнение {} burn-in итераций для сходимости", BURN_IN_ITERATIONS);
        for (int i = 0; i < BURN_IN_ITERATIONS; i++) {
            int transformIndex = random.nextInt(config.getAffineParams().size());
            AffineTransform affineTransform = config.getAffineParams().get(transformIndex);
            double[] transformed = affineTransform.transform(x, y);
            x = transformed[0];
            y = transformed[1];
        }

        int totalIterations = config.getIterationCount();
        for (int iteration = 0; iteration < totalIterations; iteration++) {
            if (iteration % Math.max(1, totalIterations / 10) == 0) {
                int percent = (int) ((iteration * 100.0) / totalIterations);
                logger.info("Прогресс: {} / {} ({}%)", iteration, totalIterations, percent);
            }

            int transformIndex = random.nextInt(config.getAffineParams().size());
            AffineTransform affineTransform = config.getAffineParams().get(transformIndex);
            double[] transformed = affineTransform.transform(x, y);
            x = transformed[0];
            y = transformed[1];

            updatePixelBuffer(x, y, random, transformIndex);
        }

        long endTime = System.currentTimeMillis();
        logger.info("Однопоточный рендеринг завершен за {} мс", endTime - startTime);
    }

    private void renderMultithreaded() {
        logger.info(
            "Начало многопоточного рендеринга с {} потоками, ниже прогресс выполнения для одного и потоков",
            config.getThreads());
        long startTime = System.currentTimeMillis();

        // Burn-in в главном потоке
        logger.debug("Выполнение {} burn-in итераций для сходимости", BURN_IN_ITERATIONS);
        Random burnRandom = new Random(config.getSeed());
        double x = 0.0;
        double y = 0.0;

        for (int i = 0; i < BURN_IN_ITERATIONS; i++) {
            int transformIndex = burnRandom.nextInt(config.getAffineParams().size());
            AffineTransform affineTransform = config.getAffineParams().get(transformIndex);
            double[] transformed = affineTransform.transform(x, y);
            x = transformed[0];
            y = transformed[1];
        }

        ExecutorService executorService = Executors.newFixedThreadPool(config.getThreads());
        int iterationsPerThread = config.getIterationCount() / config.getThreads();
        int remainingIterations = config.getIterationCount() % config.getThreads();

        try {
            for (int threadId = 0; threadId < config.getThreads(); threadId++) {
                int iterations = iterationsPerThread + (threadId < remainingIterations ? 1 : 0);
                RenderTask task = new RenderTask(threadId, iterations, config, x, y);
                executorService.submit(task);
            }

            executorService.shutdown();
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                logger.error("Превышен тайм-аут при выполнении многопоточного рендеринга");
                executorService.shutdownNow();
            } else {
                logger.info("Все потоки завершили работу");
            }
        } catch (InterruptedException e) {
            logger.error("Многопоточный рендеринг прерван: {}", e.getMessage());
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        logger.debug("Многопоточный рендеринг завершен за {} мс", endTime - startTime);
    }

    private void updatePixelBuffer(double x, double y, Random random, int transformIndex) {
        TransformFunction transformFunc = selectTransformFunction(random);
        FlameFunction function = FlameFunctionFactory.createFunction(transformFunc.getName());

        double[] transformed = function.apply(x, y);
        double newX = transformed[0];
        double newY = transformed[1];

        int pixelX = (int) ((newX - X_MIN) / X_RANGE * (config.getSize().getWidth() - 1));
        int pixelY = (int) ((newY - Y_MIN) / Y_RANGE * (config.getSize().getHeight() - 1));

        byte[] color = affineColors.get(transformIndex);

        if (config.getSymmetryLevel() > 1) {
            applySymmetry(pixelX, pixelY, color);
        } else {
            if (pixelX >= 0
                && pixelX < config.getSize().getWidth()
                && pixelY >= 0
                && pixelY < config.getSize().getHeight()) {
                updatePixel(pixelX, pixelY, color);
            }
        }
    }

    private void applySymmetry(int pixelX, int pixelY, byte[] color) {
        int centerX = config.getSize().getWidth() / 2;
        int centerY = config.getSize().getHeight() / 2;

        double relX = pixelX - centerX;
        double relY = pixelY - centerY;

        for (int i = 0; i < config.getSymmetryLevel(); i++) {
            double angle = (2.0 * Math.PI * i) / config.getSymmetryLevel();

            double cosA = Math.cos(angle);
            double sinA = Math.sin(angle);
            double rotX = relX * cosA - relY * sinA;
            double rotY = relX * sinA + relY * cosA;

            int finalX = (int) (rotX + centerX);
            int finalY = (int) (rotY + centerY);

            if (finalX >= 0
                && finalX < config.getSize().getWidth()
                && finalY >= 0
                && finalY < config.getSize().getHeight()) {
                updatePixel(finalX, finalY, color);
            }
        }
    }

    private void updatePixel(int x, int y, byte[] newColor) {
        int hits = hitCount[y][x];

        if (hits == 0) {
            // Первое попадание — просто записываем цвет
            colorBuffer[y][x][0] = newColor[0];
            colorBuffer[y][x][1] = newColor[1];
            colorBuffer[y][x][2] = newColor[2];
        } else {
            // Усредняем цвета (конвертируем byte в int для правильной арифметики)
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

    private TransformFunction selectTransformFunction(Random random) {
        double totalWeight = config.getFunctions().stream()
            .mapToDouble(TransformFunction::getWeight)
            .sum();
        double value = random.nextDouble() * totalWeight;
        double sum = 0.0;

        for (TransformFunction func : config.getFunctions()) {
            sum += func.getWeight();
            if (value <= sum) {
                return func;
            }
        }

        return config.getFunctions().getLast();
    }

    private void applyColorCorrection() {
        logger.info("Применение цветовой коррекции");
        long startTime = System.currentTimeMillis();

        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();

        // Находим максимальную интенсивность
        int maxHits = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (hitCount[y][x] > maxHits) {
                    maxHits = hitCount[y][x];
                }
            }
        }

        if (maxHits == 0) {
            return;
        }

        // Применяем коррекцию яркости на основе логарифма попаданий
        double normalizedMaxIntensity = Math.log(maxHits + 1.0);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int hits = hitCount[y][x];
                if (hits == 0) {
                    continue;
                }

                // Логарифмическая нормализация
                double normalizedIntensity = Math.log(hits + 1.0) / normalizedMaxIntensity;

                // Гамма-коррекция (если включена)
                double brightness;
                if (config.isGammaCorrection()) {
                    double gamma = config.getGamma();
                    brightness = Math.pow(normalizedIntensity, 1.0 / gamma);
                } else {
                    brightness = normalizedIntensity;
                }

                // Применяем яркость к цвету
                int r = (colorBuffer[y][x][0] & 0xFF);
                int g = (colorBuffer[y][x][1] & 0xFF);
                int b = (colorBuffer[y][x][2] & 0xFF);

                colorBuffer[y][x][0] = (byte) Math.min(255, (int) (r * brightness));
                colorBuffer[y][x][1] = (byte) Math.min(255, (int) (g * brightness));
                colorBuffer[y][x][2] = (byte) Math.min(255, (int) (b * brightness));
            }
        }
        long endTime = System.currentTimeMillis();
        logger.debug("Применение цветовой коррекции завершено за {} мс", endTime - startTime);
    }

    private class RenderTask implements Runnable {
        private final int threadId;
        private final int iterations;
        private final FractalConfig config;
        private final double initialX;
        private final double initialY;

        RenderTask(int threadId, int iterations, FractalConfig config, double initialX, double initialY) {
            this.threadId = threadId;
            this.iterations = iterations;
            this.config = config;
            this.initialX = initialX;
            this.initialY = initialY;
        }

        @Override
        public void run() {
            logger.debug("Поток {} начал обработку {} итераций", threadId, iterations);
            Random random = new Random(config.getSeed() + threadId * 12345L);
            double x = initialX;
            double y = initialY;

            for (int iteration = 0; iteration < iterations; iteration++) {
                int transformIndex = random.nextInt(config.getAffineParams().size());
                AffineTransform affineTransform = config.getAffineParams().get(transformIndex);
                double[] transformed = affineTransform.transform(x, y);
                x = transformed[0];
                y = transformed[1];

                updatePixelBuffer(x, y, random, transformIndex);

                if (iteration % Math.max(1, iterations / 10) == 0) {
                    int percent = (int) ((iteration * 100.0) / iterations);
                    if (threadId == 0) {
                        logger.info("Поток {}: прогресс {} / {} ({}%)", threadId, iteration, iterations, percent);
                    } else {
                        logger.debug("Поток {}: прогресс {} / {} ({}%)", threadId, iteration, iterations, percent);
                    }
                }
            }

            if (threadId == 0) {
                logger.info("Поток {} завершил обработку", threadId);
            } else {
                logger.debug("Поток {} завершил обработку", threadId);
            }
        }
    }
}
