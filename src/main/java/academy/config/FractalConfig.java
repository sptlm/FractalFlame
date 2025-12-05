package com.fractal.flame.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс конфигурации для фрактального пламени
 * Содержит все параметры, необходимые для генерации изображения
 */
public class FractalConfig {
    // Размер изображения
    @JsonProperty("size")
    private ImageSize size = new ImageSize(1920, 1080);

    // Количество итераций для генерации
    @JsonProperty("iteration_count")
    private int iterationCount = 2500;

    // Путь для сохранения результирующего изображения
    @JsonProperty("output_path")
    private String outputPath = "result.png";

    // Количество потоков для многопоточной обработки
    @JsonProperty("threads")
    private int threads = 1;

    // Начальное значение для генератора случайных чисел
    @JsonProperty("seed")
    private long seed = 5L;

    // Список функций трансформации
    @JsonProperty("functions")
    private List<TransformFunction> functions = new ArrayList<>();

    // Список аффинных преобразований
    @JsonProperty("affine_params")
    private List<AffineTransform> affineParams = new ArrayList<>();

    // Флаг для включения гамма-коррекции (бонусное задание)
    @JsonProperty("gamma_correction")
    private boolean gammaCorrection = false;

    // Значение гаммы для коррекции яркости (бонусное задание)
    @JsonProperty("gamma")
    private double gamma = 2.2;

    // Уровень симметрии (бонусное задание)
    @JsonProperty("symmetry_level")
    private int symmetryLevel = 1;

    public FractalConfig() {
        // Инициализируем значения по умолчанию с базовыми функциями и преобразованиями
        initializeDefaults();
    }

    /**
     * Инициализирует значения по умолчанию
     */
    private void initializeDefaults() {
        if (functions.isEmpty()) {
            functions.add(new TransformFunction("swirl", 1.0));
            functions.add(new TransformFunction("horseshoe", 0.5));
        }

        if (affineParams.isEmpty()) {
            affineParams.add(new AffineTransform(0.5, 0.0, 0.0, 0.0, 0.5, 0.0));
            affineParams.add(new AffineTransform(0.5, 0.0, 1.0, 0.0, 0.5, 0.0));
        }
    }

    // Getters and Setters

    public ImageSize getSize() {
        return size;
    }

    public void setSize(ImageSize size) {
        this.size = size;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public List<TransformFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<TransformFunction> functions) {
        this.functions = functions;
    }

    public List<AffineTransform> getAffineParams() {
        return affineParams;
    }

    public void setAffineParams(List<AffineTransform> affineParams) {
        this.affineParams = affineParams;
    }

    public boolean isGammaCorrection() {
        return gammaCorrection;
    }

    public void setGammaCorrection(boolean gammaCorrection) {
        this.gammaCorrection = gammaCorrection;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getSymmetryLevel() {
        return symmetryLevel;
    }

    public void setSymmetryLevel(int symmetryLevel) {
        this.symmetryLevel = symmetryLevel;
    }

    /**
     * Валидирует всю конфигурацию
     */
    public void validate() {
        if (size == null) {
            throw new IllegalArgumentException("Размер изображения не может быть null");
        }
        size.validate();

        if (iterationCount <= 0) {
            throw new IllegalArgumentException("Количество итераций должно быть положительным, получено: " + iterationCount);
        }

        if (threads <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть положительным, получено: " + threads);
        }

        if (outputPath == null || outputPath.isEmpty()) {
            throw new IllegalArgumentException("Путь для сохранения не может быть пустым");
        }

        if (functions == null || functions.isEmpty()) {
            throw new IllegalArgumentException("Должна быть хотя бы одна функция трансформации");
        }

        // Валидируем каждую функцию
        for (TransformFunction func : functions) {
            func.validate();
        }

        if (affineParams == null || affineParams.isEmpty()) {
            throw new IllegalArgumentException("Должно быть хотя бы одно аффинное преобразование");
        }

        // Валидируем каждое аффинное преобразование
        for (AffineTransform transform : affineParams) {
            transform.validate();
        }

        if (gamma <= 0) {
            throw new IllegalArgumentException("Значение гаммы должно быть положительным, получено: " + gamma);
        }

        if (symmetryLevel < 1) {
            throw new IllegalArgumentException("Уровень симметрии должен быть >= 1, получено: " + symmetryLevel);
        }
    }
}
