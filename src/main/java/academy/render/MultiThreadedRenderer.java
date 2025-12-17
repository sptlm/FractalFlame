package academy.render;

import academy.config.FractalConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class MultiThreadedRenderer extends AbstractFractalRenderer {

    @Override
    public byte[][][] render(FractalConfig config) {
        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();
        byte[][][] colorBuffer = new byte[height][width][3];
        int[][] hitCount = new int[height][width];

        getLogger().info("Начало многопоточного рендеринга с {} потоками", config.getThreads());
        long startTime = System.currentTimeMillis();

        // Инициализируем цвета для аффинных преобразований
        initAffineColors(config);

        // Создаем детерминированный Random для burn-in
        Random burnInRandom = new Random(config.getSeed());

        // Выполняем burn-in итерации
        getLogger().debug("Выполнение {} burn-in итераций для сходимости", BURN_IN_ITERATIONS);
        double[] initialPoint = performBurnIn(burnInRandom, config);

        // Создаем пул потоков
        ExecutorService executorService = Executors.newFixedThreadPool(config.getThreads());

        // Распределяем итерации между потоками
        int iterationsPerThread = config.getIterationCount() / config.getThreads();
        int remainingIterations = config.getIterationCount() % config.getThreads();

        // Список для хранения Future результатов
        List<Future<LocalBuffer>> futures = new ArrayList<>();

        try {
            // Запускаем задачи для каждого потока
            for (int threadId = 0; threadId < config.getThreads(); threadId++) {
                int iterations = iterationsPerThread + (threadId < remainingIterations ? 1 : 0);

                // Создаем задачу рендеринга с локальным буфером
                RenderTask task = new RenderTask(threadId, iterations, config, initialPoint[0], initialPoint[1], this);

                futures.add(executorService.submit(task));
            }

            // Ожидаем завершения всех потоков
            executorService.shutdown();
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                getLogger().error("Превышен тайм-аут при выполнении многопоточного рендеринга");
                executorService.shutdownNow();
            } else {
                getLogger().info("Все потоки завершили работу");
            }

            // Объединяем локальные буферы в финальные
            mergeLocalBuffers(futures, colorBuffer, hitCount, config);

        } catch (InterruptedException e) {
            getLogger().error("Многопоточный рендеринг прерван: {}", e.getMessage());
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        getLogger().info("Многопоточный рендеринг завершен за {} мс", endTime - startTime);

        applyColorCorrection(colorBuffer, hitCount, config);

        return colorBuffer;
    }

    private void mergeLocalBuffers(
            List<Future<LocalBuffer>> futures,
            byte[][][] finalColorBuffer,
            int[][] finalHitCount,
            FractalConfig config) {
        getLogger().info("Объединение локальных буферов потоков");
        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();

        // Временные буферы для накопления взвешенных цветов
        long[][][] weightedColorSum = new long[height][width][3];

        try {
            // Проходим по всем локальным буферам
            for (Future<LocalBuffer> future : futures) {
                LocalBuffer localBuffer = future.get();
                byte[][][] localColors = localBuffer.colorBuffer;
                int[][] localHits = localBuffer.hitCount;

                // Суммируем цвета и hitCount
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int hits = localHits[y][x];
                        if (hits > 0) {
                            // Складываем hitCount
                            finalHitCount[y][x] += hits;

                            // Складываем взвешенные цвета (цвет * количество попаданий)
                            weightedColorSum[y][x][0] += (long) (localColors[y][x][0] & 0xFF) * hits;
                            weightedColorSum[y][x][1] += (long) (localColors[y][x][1] & 0xFF) * hits;
                            weightedColorSum[y][x][2] += (long) (localColors[y][x][2] & 0xFF) * hits;
                        }
                    }
                }
            }

            // Вычисляем средние цвета на основе взвешенных сумм
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int totalHits = finalHitCount[y][x];
                    if (totalHits > 0) {
                        // Усредняем цвета пропорционально количеству попаданий
                        finalColorBuffer[y][x][0] = (byte) (weightedColorSum[y][x][0] / totalHits);
                        finalColorBuffer[y][x][1] = (byte) (weightedColorSum[y][x][1] / totalHits);
                        finalColorBuffer[y][x][2] = (byte) (weightedColorSum[y][x][2] / totalHits);
                    }
                }
            }

            getLogger().info("Локальные буферы успешно объединены");

        } catch (InterruptedException | ExecutionException e) {
            getLogger().error("Ошибка при объединении локальных буферов: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
