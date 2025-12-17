package academy.render;

import academy.config.AffineTransform;
import academy.config.FractalConfig;
import java.util.Random;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderTask implements Callable<LocalBuffer> {
    private static final Logger logger = LoggerFactory.getLogger(RenderTask.class);

    private final int threadId;
    private final int iterations;
    private final FractalConfig config;
    private final double initialX;
    private final double initialY;
    // Ссылка на рендерер для доступа к общим методам
    private final AbstractFractalRenderer renderer;

    public RenderTask(
            int threadId,
            int iterations,
            FractalConfig config,
            double initialX,
            double initialY,
            AbstractFractalRenderer renderer) {
        this.threadId = threadId;
        this.iterations = iterations;
        this.config = config;
        this.initialX = initialX;
        this.initialY = initialY;
        this.renderer = renderer;
    }

    @Override
    public LocalBuffer call() {
        logger.debug("Поток {} начал обработку {} итераций", threadId, iterations);

        // Создаем локальный буфер для этого потока
        LocalBuffer localBuffer =
                new LocalBuffer(config.getSize().getWidth(), config.getSize().getHeight());

        // Создаем Random для этого потока
        Random threadRandom = new Random(config.getSeed() + threadId);

        // Начальные координаты (результат burn-in)
        double x = initialX;
        double y = initialY;

        // Выполняем итерации рендеринга
        for (int iteration = 0; iteration < iterations; iteration++) {
            // Выбираем случайное аффинное преобразование
            int transformIndex = threadRandom.nextInt(config.getAffineParams().size());
            AffineTransform affineTransform = config.getAffineParams().get(transformIndex);

            // Применяем аффинное преобразование
            double[] affineResult = affineTransform.transform(x, y);

            // Применяем композицию вариаций (используем метод из родительского рендерера)
            double[] finalResult = renderer.applyVariationComposition(affineResult[0], affineResult[1], config);
            x = finalResult[0];
            y = finalResult[1];

            // Обновляем локальный буфер этого потока
            renderer.updatePixelBuffer(
                    x, y, transformIndex, localBuffer.getColorBuffer(), localBuffer.getHitCount(), config);

            // Логируем прогресс выполнения
            logProgress(iteration);
        }

        logger.info("Поток {} завершил обработку", threadId);
        return localBuffer;
    }

    private void logProgress(int iteration) {
        if (iteration % Math.max(1, iterations / 10) == 0) {
            int percent = (int) (iteration * 100.0 / iterations);
            if (threadId == 0) {
                logger.info("Поток {}: прогресс {} / {} ({}%)", threadId, iteration, iterations, percent);
            } else {
                logger.debug("Поток {}: прогресс {} / {} ({}%)", threadId, iteration, iterations, percent);
            }
        }
    }
}
