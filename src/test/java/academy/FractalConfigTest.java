package com.fractal.flame.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для проверки корректности конфигурации
 */
@DisplayName("Тесты конфигурации фрактального пламени")
public class FractalConfigTest {

    /**
     * Тест создания конфигурации с параметрами по умолчанию
     */
    @Test
    @DisplayName("Конфигурация создается с параметрами по умолчанию")
    public void testDefaultConfiguration() {
        FractalConfig config = new FractalConfig();

        assertNotNull(config);
        assertEquals(1920, config.getSize().getWidth());
        assertEquals(1080, config.getSize().getHeight());
        assertEquals(2500, config.getIterationCount());
        assertEquals(5, config.getSeed());
        assertEquals(1, config.getThreads());
        assertEquals("result.png", config.getOutputPath());
        assertFalse(config.isGammaCorrection());
        assertEquals(2.2, config.getGamma(), 0.001);
        assertEquals(1, config.getSymmetryLevel());
    }

    /**
     * Тест валидации конфигурации
     */
    @Test
    @DisplayName("Конфигурация валидируется успешно")
    public void testConfigurationValidation() {
        FractalConfig config = new FractalConfig();
        // Не должно выброситься исключение
        assertDoesNotThrow(config::validate);
    }

    /**
     * Тест валидации размера изображения
     */
    @Test
    @DisplayName("Валидация отклоняет некорректный размер")
    public void testInvalidImageSize() {
        FractalConfig config = new FractalConfig();
        config.getSize().setWidth(0);
        
        assertThrows(IllegalArgumentException.class, config::validate);
    }

    /**
     * Тест валидации количества итераций
     */
    @Test
    @DisplayName("Валидация отклоняет некорректное количество итераций")
    public void testInvalidIterationCount() {
        FractalConfig config = new FractalConfig();
        config.setIterationCount(0);
        
        assertThrows(IllegalArgumentException.class, config::validate);
    }

    /**
     * Тест валидации количества потоков
     */
    @Test
    @DisplayName("Валидация отклоняет некорректное количество потоков")
    public void testInvalidThreadCount() {
        FractalConfig config = new FractalConfig();
        config.setThreads(-1);
        
        assertThrows(IllegalArgumentException.class, config::validate);
    }

    /**
     * Тест валидации пути вывода
     */
    @Test
    @DisplayName("Валидация отклоняет пустой путь вывода")
    public void testInvalidOutputPath() {
        FractalConfig config = new FractalConfig();
        config.setOutputPath("");
        
        assertThrows(IllegalArgumentException.class, config::validate);
    }

    /**
     * Тест валидации гаммы
     */
    @Test
    @DisplayName("Валидация отклоняет некорректное значение гаммы")
    public void testInvalidGamma() {
        FractalConfig config = new FractalConfig();
        config.setGamma(0);
        
        assertThrows(IllegalArgumentException.class, config::validate);
    }

    /**
     * Тест валидации уровня симметрии
     */
    @Test
    @DisplayName("Валидация отклоняет некорректный уровень симметрии")
    public void testInvalidSymmetryLevel() {
        FractalConfig config = new FractalConfig();
        config.setSymmetryLevel(0);
        
        assertThrows(IllegalArgumentException.class, config::validate);
    }
}
