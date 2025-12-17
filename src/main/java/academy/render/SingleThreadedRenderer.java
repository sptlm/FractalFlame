package academy.render;

import academy.config.AffineTransform;
import academy.config.FractalConfig;
import java.util.Random;

public class SingleThreadedRenderer extends AbstractFractalRenderer {

    private final Random random = new Random();

    @Override
    public byte[][][] render(FractalConfig config) {

        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();
        byte[][][] colorBuffer = new byte[height][width][3];
        int[][] hitCount = new int[height][width];
        getLogger().info("Начало однопоточного рендеринга");
        long startTime = System.currentTimeMillis();

        // Инициализируем цвета для аффинных преобразований
        initAffineColors(config);

        // Создаем Random на основе seed
        random.setSeed(config.getSeed());

        // Выполняем burn-in итерации для сходимости
        getLogger().debug("Выполнение {} burn-in итераций для сходимости", BURN_IN_ITERATIONS);
        double[] initialPoint = performBurnIn(random, config);
        double x = initialPoint[0];
        double y = initialPoint[1];

        // Основной цикл рендеринга
        int totalIterations = config.getIterationCount();
        for (int iteration = 0; iteration < totalIterations; iteration++) {
            // Логируем прогресс каждые 10%
            if (iteration % Math.max(1, totalIterations / 10) == 0) {
                int percent = (int) (iteration * 100.0 / totalIterations);
                getLogger().info("Прогресс: {} / {} ({}%)", iteration, totalIterations, percent);
            }

            // Выбираем случайное аффинное преобразование
            int transformIndex = random.nextInt(config.getAffineParams().size());
            AffineTransform affineTransform = config.getAffineParams().get(transformIndex);

            // Применяем аффинное преобразование
            double[] affineResult = affineTransform.transform(x, y);

            // Применяем композицию вариаций
            double[] finalResult = applyVariationComposition(affineResult[0], affineResult[1], config);
            x = finalResult[0];
            y = finalResult[1];

            // Обновляем пиксель в буферах
            updatePixelBuffer(x, y, transformIndex, colorBuffer, hitCount, config);
        }

        long endTime = System.currentTimeMillis();
        getLogger().info("Однопоточный рендеринг завершен за {} мс", endTime - startTime);

        applyColorCorrection(colorBuffer, hitCount, config);

        return colorBuffer;
    }
}
