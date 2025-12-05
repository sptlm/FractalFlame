package com.fractal.flame.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для проверки корректности аффинного преобразования
 */
@DisplayName("Тесты аффинного преобразования")
public class AffineTransformTest {

    /**
     * Тест создания аффинного преобразования
     */
    @Test
    @DisplayName("Аффинное преобразование создается с правильными параметрами")
    public void testAffineTransformCreation() {
        AffineTransform transform = new AffineTransform(1, 2, 3, 4, 5, 6);

        assertEquals(1, transform.getA(), 0.001);
        assertEquals(2, transform.getB(), 0.001);
        assertEquals(3, transform.getC(), 0.001);
        assertEquals(4, transform.getD(), 0.001);
        assertEquals(5, transform.getE(), 0.001);
        assertEquals(6, transform.getF(), 0.001);
    }

    /**
     * Тест применения аффинного преобразования
     */
    @Test
    @DisplayName("Аффинное преобразование применяется корректно")
    public void testAffineTransformApplication() {
        // Преобразование: x' = 2*x + 0*y + 1, y' = 0*x + 3*y + 2
        AffineTransform transform = new AffineTransform(2, 0, 1, 0, 3, 2);
        double[] result = transform.transform(1, 1);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(3, result[0], 0.001);  // 2*1 + 0*1 + 1 = 3
        assertEquals(5, result[1], 0.001);  // 0*1 + 3*1 + 2 = 5
    }

    /**
     * Тест применения аффинного преобразования к нулевой точке
     */
    @Test
    @DisplayName("Аффинное преобразование на нулевой точке")
    public void testAffineTransformOnZeroPoint() {
        AffineTransform transform = new AffineTransform(2, 3, 4, 5, 6, 7);
        double[] result = transform.transform(0, 0);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(4, result[0], 0.001);  // 2*0 + 3*0 + 4 = 4
        assertEquals(7, result[1], 0.001);  // 5*0 + 6*0 + 7 = 7
    }

    /**
     * Тест валидации аффинного преобразования с особой матрицей
     */
    @Test
    @DisplayName("Валидация отклоняет особую матрицу")
    public void testAffineTransformValidationWithSingularMatrix() {
        // Матрица с детерминантом = 0 (особая матрица)
        // det = a*e - b*d = 1*2 - 2*1 = 0
        AffineTransform transform = new AffineTransform(1, 2, 0, 1, 2, 0);

        assertThrows(IllegalArgumentException.class, transform::validate);
    }

    /**
     * Тест валидации корректного аффинного преобразования
     */
    @Test
    @DisplayName("Валидация принимает корректную матрицу")
    public void testAffineTransformValidation() {
        // Матрица с ненулевым детерминантом
        // det = 0.5*0.5 - 0*0 = 0.25
        AffineTransform transform = new AffineTransform(0.5, 0, 1, 0, 0.5, 2);

        assertDoesNotThrow(transform::validate);
    }

    /**
     * Тест применения последовательных преобразований
     */
    @Test
    @DisplayName("Последовательные преобразования работают корректно")
    public void testSequentialTransforms() {
        AffineTransform transform1 = new AffineTransform(2, 0, 1, 0, 2, 1);
        AffineTransform transform2 = new AffineTransform(0.5, 0, 0, 0, 0.5, 0);

        // Применяем первое преобразование
        double[] result1 = transform1.transform(1, 1);
        // Применяем второе преобразование к результату первого
        double[] result2 = transform2.transform(result1[0], result1[1]);

        assertNotNull(result2);
        assertEquals(2, result2.length);
        // (1,1) -> (3, 3) -> (1.5, 1.5)
        assertEquals(1.5, result2[0], 0.001);
        assertEquals(1.5, result2[1], 0.001);
    }
}
