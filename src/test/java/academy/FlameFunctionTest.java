package com.fractal.flame.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для проверки корректности функций трансформации
 */
@DisplayName("Тесты функций трансформации")
public class FlameFunctionTest {

    /**
     * Тест функции Swirl (Вихрь)
     * Проверяет, что функция применяется к координатам без ошибок
     */
    @Test
    @DisplayName("Функция Swirl применяется корректно")
    public void testSwirlFunction() {
        FlameFunction swirl = FlameFunctionFactory.createFunction("swirl");
        assertNotNull(swirl);
        assertEquals("swirl", swirl.getName());

        // Тестируем на известных значениях
        double[] result = swirl.apply(0, 0);
        assertNotNull(result);
        assertEquals(2, result.length);
        // На (0,0) результат должен быть (0,0)
        assertEquals(0.0, result[0], 0.001);
        assertEquals(0.0, result[1], 0.001);

        // Тестируем на ненулевых значениях
        result = swirl.apply(1, 1);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
    }

    /**
     * Тест функции Horseshoe (Подкова)
     * Проверяет корректность формулы преобразования
     */
    @Test
    @DisplayName("Функция Horseshoe применяется корректно")
    public void testHorseshoeFunction() {
        FlameFunction horseshoe = FlameFunctionFactory.createFunction("horseshoe");
        assertNotNull(horseshoe);
        assertEquals("horseshoe", horseshoe.getName());

        // Тестируем на известных значениях
        double[] result = horseshoe.apply(0, 0);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(0.0, result[0], 0.001);
        assertEquals(0.0, result[1], 0.001);

        // Тестируем на ненулевых значениях
        result = horseshoe.apply(2, 2);
        assertNotNull(result);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
    }

    /**
     * Тест функции Julia (Множество Джулии)
     * Проверяет, что функция генерирует различные значения
     */
    @Test
    @DisplayName("Функция Julia работает без ошибок")
    public void testJuliaFunction() {
        FlameFunction julia = FlameFunctionFactory.createFunction("julia");
        assertNotNull(julia);
        assertEquals("julia", julia.getName());

        double[] result = julia.apply(1, 1);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
    }

    /**
     * Тест функции Diamond (Алмаз)
     * Проверяет корректность вычисления
     */
    @Test
    @DisplayName("Функция Diamond применяется корректно")
    public void testDiamondFunction() {
        FlameFunction diamond = FlameFunctionFactory.createFunction("diamond");
        assertNotNull(diamond);
        assertEquals("diamond", diamond.getName());

        double[] result = diamond.apply(0.5, 0.5);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
        // Результат должен быть нормализован
        assertTrue(Math.abs(result[0]) <= 1.1);
        assertTrue(Math.abs(result[1]) <= 1.1);
    }

    /**
     * Тест функции Polar (Полярная)
     * Проверяет преобразование в полярные координаты
     */
    @Test
    @DisplayName("Функция Polar применяется корректно")
    public void testPolarFunction() {
        FlameFunction polar = FlameFunctionFactory.createFunction("polar");
        assertNotNull(polar);
        assertEquals("polar", polar.getName());

        double[] result = polar.apply(1, 1);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
    }

    /**
     * Тест функции Waves (Волны)
     * Проверяет волновое преобразование
     */
    @Test
    @DisplayName("Функция Waves применяется корректно")
    public void testWavesFunction() {
        FlameFunction waves = FlameFunctionFactory.createFunction("waves");
        assertNotNull(waves);
        assertEquals("waves", waves.getName());

        double[] result = waves.apply(1, 1);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
    }

    /**
     * Тест функции Spiral (Спираль)
     */
    @Test
    @DisplayName("Функция Spiral применяется корректно")
    public void testSpiralFunction() {
        FlameFunction spiral = FlameFunctionFactory.createFunction("spiral");
        assertNotNull(spiral);
        assertEquals("spiral", spiral.getName());

        double[] result = spiral.apply(1, 1);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
    }

    /**
     * Тест функции Sinusoidal (Синусоидальная)
     */
    @Test
    @DisplayName("Функция Sinusoidal применяется корректно")
    public void testSinusoidalFunction() {
        FlameFunction sinusoidal = FlameFunctionFactory.createFunction("sinusoidal");
        assertNotNull(sinusoidal);
        assertEquals("sinusoidal", sinusoidal.getName());

        double[] result = sinusoidal.apply(1, 1);
        assertNotNull(result);
        assertEquals(2, result.length);
        // sin(1) ≈ 0.841
        assertEquals(Math.sin(1), result[0], 0.001);
        assertEquals(Math.sin(1), result[1], 0.001);
    }

    /**
     * Тест фабрики на выброс исключения для неизвестной функции
     */
    @Test
    @DisplayName("Фабрика выбрасывает исключение для неизвестной функции")
    public void testUnknownFunctionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            FlameFunctionFactory.createFunction("unknown_function");
        });
    }

    /**
     * Тест, что все функции имеют корректное имя
     */
    @ParameterizedTest
    @ValueSource(strings = {"swirl", "horseshoe", "julia", "diamond", "polar", "waves", "spiral", "sinusoidal"})
    @DisplayName("Все функции имеют правильное имя")
    public void testFunctionNames(String functionName) {
        FlameFunction function = FlameFunctionFactory.createFunction(functionName);
        assertEquals(functionName, function.getName());
    }
}
