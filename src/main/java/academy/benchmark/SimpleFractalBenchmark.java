package academy.benchmark;

import academy.config.AffineTransform;
import academy.config.FractalConfig;
import academy.config.ImageSize;
import academy.config.TransformFunction;
import academy.render.MultiThreadedRenderer;
import academy.render.Renderer;
import academy.render.SingleThreadedRenderer;
import java.util.ArrayList;
import java.util.List;

public class SimpleFractalBenchmark {
    private static final int WARMUP_ITERATIONS = 100_000;
    private static final int TEST_ITERATIONS = 5_000_000;
    private static final int RUNS_PER_CONFIG = 5;

    public static void main(String[] args) {
        int[] threadCounts = {1, 2, 4, 8};

        System.out.println("Warming up JVM...");
        warmup();

        List<BenchmarkResult> results = new ArrayList<>();

        for (int threads : threadCounts) {
            System.out.println("Testing with " + threads + " thread(s)...");
            BenchmarkResult result = runBenchmark(threads);
            results.add(result);

            try {
                Thread.sleep(1000);
                System.gc();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        printResults(results);
    }

    private static void warmup() {
        try {
            FractalConfig warmupConfig = createConfig(1, WARMUP_ITERATIONS);
            Renderer renderer;
            if (warmupConfig.getThreads() > 1) {
                renderer = new MultiThreadedRenderer();
            } else {
                renderer = new SingleThreadedRenderer();
            }
            renderer.render(warmupConfig);
        } catch (Exception e) {
            System.out.println("Warmup error: " + e.getMessage());
        }
    }

    private static BenchmarkResult runBenchmark(int threads) {
        long[] times = new long[RUNS_PER_CONFIG];

        for (int run = 0; run < RUNS_PER_CONFIG; run++) {
            FractalConfig config = createConfig(threads, TEST_ITERATIONS);

            long startTime = System.nanoTime();
            try {
                Renderer renderer;
                if (config.getThreads() > 1) {
                    renderer = new MultiThreadedRenderer();
                } else {
                    renderer = new SingleThreadedRenderer();
                }
                renderer.render(config);
            } catch (Exception e) {
                System.out.println("Render error: " + e.getMessage());
                times[run] = -1;
                continue;
            }
            long endTime = System.nanoTime();

            times[run] = (endTime - startTime) / 1_000_000;
        }

        return new BenchmarkResult(threads, times);
    }

    private static FractalConfig createConfig(int threads, int iterations) {
        FractalConfig config = new FractalConfig();

        config.setSize(new ImageSize(1920, 1080));
        config.setIterationCount(iterations);
        config.setThreads(threads);
        config.setSeed(42L);
        config.setOutputPath("");

        List<TransformFunction> functions = new ArrayList<>();
        functions.add(new TransformFunction("sinusoidal", 1.0));
        functions.add(new TransformFunction("spherical", 0.8));
        functions.add(new TransformFunction("swirl", 0.6));
        functions.add(new TransformFunction("horseshoe", 0.5));
        config.setFunctions(functions);

        List<AffineTransform> affineParams = new ArrayList<>();
        affineParams.add(new AffineTransform(0.5, 0.1, 0.0, -0.1, 0.5, 0.0));
        affineParams.add(new AffineTransform(0.5, -0.1, 1.0, 0.1, 0.5, 0.0));
        affineParams.add(new AffineTransform(0.4, 0.0, 0.5, 0.0, 0.4, 0.5));
        config.setAffineParams(affineParams);

        config.setGammaCorrection(true);
        config.setGamma(2.2);
        config.setSymmetryLevel(1);

        return config;
    }

    private static void printResults(List<BenchmarkResult> results) {
        System.out.println("\nBenchmark Results:");
        System.out.println("Threads | Avg(sec) | Min(sec) | Max(sec) | Speedup | Efficiency(%)");
        System.out.println("--------|----------|----------|----------|---------|---------------");

        double baselineTime = results.get(0).averageTime;

        for (BenchmarkResult result : results) {
            double speedup = baselineTime / result.averageTime;
            double efficiency = speedup / result.threads * 100;

            System.out.printf(
                    "%7d | %8.2f | %8.2f | %8.2f | %7.2fx | %13.1f%n",
                    result.threads,
                    result.averageTime / 1000.0,
                    result.minTime / 1000.0,
                    result.maxTime / 1000.0,
                    speedup,
                    efficiency);
        }

        BenchmarkResult best = results.get(results.size() - 1);
        double maxSpeedup = baselineTime / best.averageTime;

        System.out.println("\nSummary:");
        System.out.printf("Baseline (1 thread): %.2f sec%n", baselineTime / 1000.0);
        System.out.printf("Max speedup: %.2fx with %d threads%n", maxSpeedup, best.threads);
        System.out.printf("Parallel efficiency: %.1f%%%n", maxSpeedup / best.threads * 100);
    }

    static class BenchmarkResult {
        final int threads;
        final long averageTime;
        final long minTime;
        final long maxTime;

        BenchmarkResult(int threads, long[] times) {
            this.threads = threads;

            List<Long> validTimes = new ArrayList<>();
            for (long time : times) {
                if (time > 0) {
                    validTimes.add(time);
                }
            }

            if (validTimes.isEmpty()) {
                this.averageTime = -1;
                this.minTime = -1;
                this.maxTime = -1;
                return;
            }

            long sum = 0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;

            for (long time : validTimes) {
                sum += time;
                min = Math.min(min, time);
                max = Math.max(max, time);
            }

            this.averageTime = sum / validTimes.size();
            this.minTime = min;
            this.maxTime = max;
        }
    }
}
