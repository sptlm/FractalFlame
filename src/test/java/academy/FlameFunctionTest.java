package academy;

import static org.junit.jupiter.api.Assertions.*;

import academy.transform.FlameFunction;
import academy.transform.FlameFunctionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Тесты функций трансформации")
public class FlameFunctionTest {

    private static final double DELTA = 0.001; // Точность сравнения

    // ========== КЛАССИЧЕСКИЕ ФУНКЦИИ ==========

    @Test
    @DisplayName("Sinusoidal: sin(x), sin(y)")
    public void testSinusoidalFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("sinusoidal");

        // Тест 1: (0, 0)
        double[] result = func.apply(0, 0);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест 2: (π/2, π)
        result = func.apply(Math.PI / 2, Math.PI);
        assertEquals(Math.sin(Math.PI / 2), result[0], DELTA); // ≈ 1.0
        assertEquals(Math.sin(Math.PI), result[1], DELTA); // ≈ 0.0

        // Тест 3: (1, 1)
        result = func.apply(1, 1);
        assertEquals(Math.sin(1), result[0], DELTA); // ≈ 0.841
        assertEquals(Math.sin(1), result[1], DELTA);
    }

    @Test
    @DisplayName("Spherical: 1/r² преобразование")
    public void testSphericalFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("spherical");

        // Тест 1: (0, 0) → (0, 0)
        double[] result = func.apply(0, 0);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест 2: (1, 0) → (1, 0)
        result = func.apply(1, 0);
        assertEquals(1.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест 3: (2, 2) r²=8, фактор=1/8
        result = func.apply(2, 2);
        double r2 = 2 * 2 + 2 * 2; // 8
        assertEquals(2.0 / r2, result[0], DELTA); // 0.25
        assertEquals(2.0 / r2, result[1], DELTA); // 0.25
    }

    @Test
    @DisplayName("Swirl: вращение зависит от расстояния")
    public void testSwirlFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("swirl");

        // Тест 1: (0, 0) → (0, 0)
        double[] result = func.apply(0, 0);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест 2: (1, 1) r²=2
        result = func.apply(1, 1);
        double r2 = 2.0;
        double expectedX = 1 * Math.sin(r2) - 1 * Math.cos(r2);
        double expectedY = 1 * Math.cos(r2) + 1 * Math.sin(r2);
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    @Test
    @DisplayName("Horseshoe: (x²-y²)/r, 2xy/r")
    public void testHorseshoeFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("horseshoe");

        // Тест 1: (0, 0) → (0, 0)
        double[] result = func.apply(0, 0);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест 2: (3, 4) r=5
        result = func.apply(3, 4);
        double r = 5.0;
        double expectedX = (3 - 4) * (3 + 4) / r; // -7/5 = -1.4
        double expectedY = 2 * 3 * 4 / r; // 24/5 = 4.8
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    @Test
    @DisplayName("Diamond: sin(θ)*cos(r), cos(θ)*sin(r)")
    public void testDiamondFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("diamond");

        // Тест: (1, 1)
        double[] result = func.apply(1, 1);
        double r = Math.sqrt(2);
        double theta = Math.atan2(1, 1); // π/4
        double expectedX = Math.sin(theta) * Math.cos(r);
        double expectedY = Math.cos(theta) * Math.sin(r);
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    // ========== ПОЛЯРНЫЕ ПРЕОБРАЗОВАНИЯ ==========

    @Test
    @DisplayName("Polar: θ/π, r-1")
    public void testPolarFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("polar");

        // Тест 1: (1, 0) → (0, 0)
        double[] result = func.apply(1, 0);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест 2: (0, 1)
        result = func.apply(0, 1);
        double theta = Math.PI / 2;
        assertEquals(theta / Math.PI, result[0], DELTA); // 0.5
        assertEquals(0.0, result[1], DELTA); // r=1, r-1=0
    }

    @Test
    @DisplayName("Waves: x + 0.25*sin(y/0.5), y + 0.25*sin(x/0.5)")
    public void testWavesFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("waves");

        // Тест: (1, 2)
        double[] result = func.apply(1, 2);
        double expectedX = 1 + 0.25 * Math.sin(2 / 0.5);
        double expectedY = 2 + 0.25 * Math.sin(1 / 0.5);
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    @Test
    @DisplayName("Spiral: (cos(θ)+sin(r))/r, (sin(θ)-cos(r))/r")
    public void testSpiralFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("spiral");

        // Тест: (1, 1)
        double[] result = func.apply(1, 1);
        double r = Math.sqrt(2);
        double theta = Math.PI / 4;
        double expectedX = (Math.cos(theta) + Math.sin(r)) / r;
        double expectedY = (Math.sin(theta) - Math.cos(r)) / r;
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    // ========== КОМПЛЕКСНЫЕ ФУНКЦИИ ==========

    @Test
    @DisplayName("Julia: случайное ±π к θ/2 (проверяем радиус)")
    public void testJuliaFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("julia");

        // Тест: (4, 0) r=4, newR=2
        double[] result = func.apply(4, 0);
        double expectedR = Math.sqrt(4); // 2.0
        double actualR = Math.sqrt(result[0] * result[0] + result[1] * result[1]);
        assertEquals(expectedR, actualR, DELTA);
    }

    @Test
    @DisplayName("Disc: θ*sin(πr)/π, θ*cos(πr)/π")
    public void testDiscFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("disc");

        // Тест: (1, 1)
        double[] result = func.apply(1, 1);
        double r = Math.sqrt(2);
        double theta = Math.PI / 4;
        double expectedX = theta * Math.sin(Math.PI * r) / Math.PI;
        double expectedY = theta * Math.cos(Math.PI * r) / Math.PI;
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    @Test
    @DisplayName("Hyperbolic: sin(θ)/r, cos(θ)*r")
    public void testHyperbolicFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("hyperbolic");

        // Тест: (3, 4) r=5, θ=atan(4/3)
        double[] result = func.apply(3, 4);
        double r = 5.0;
        double theta = Math.atan2(4, 3);
        double expectedX = Math.sin(theta) / r;
        double expectedY = Math.cos(theta) * r;
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    // ========== ТРИГОНОМЕТРИЧЕСКИЕ ФУНКЦИИ ==========

    @Test
    @DisplayName("Cosine: cos(πx)*cosh(y), -sin(πx)*sinh(y)")
    public void testCosineFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("cosine");

        // Тест: (0.5, 1)
        double[] result = func.apply(0.5, 1);
        double expectedX = Math.cos(Math.PI * 0.5) * Math.cosh(1);
        double expectedY = -Math.sin(Math.PI * 0.5) * Math.sinh(1);
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    @Test
    @DisplayName("Tangent: sin(x)/cos(y), tan(y)")
    public void testTangentFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("tangent");

        // Тест: (0.5, 0.5)
        double[] result = func.apply(0.5, 0.5);
        double expectedX = Math.sin(0.5) / Math.cos(0.5);
        double expectedY = Math.tan(0.5);
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    @Test
    @DisplayName("Power: r^cos(θ) * [cos(θ), sin(θ)]")
    public void testPowerFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("power");

        // Тест: (1, 0) θ=0, r=1, r^cos(0)=1^1=1
        double[] result = func.apply(1, 0);
        assertEquals(1.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);
    }

    @Test
    @DisplayName("Exponential: e^(x-1) * [cos(πy), sin(πy)]")
    public void testExponentialFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("exponential");

        // Тест: (1, 0) e^0 * [1, 0] = [1, 0]
        double[] result = func.apply(1, 0);
        assertEquals(1.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест: (2, 0.5)
        result = func.apply(2, 0.5);
        double exp = Math.exp(1);
        assertEquals(exp * Math.cos(Math.PI * 0.5), result[0], DELTA);
        assertEquals(exp * Math.sin(Math.PI * 0.5), result[1], DELTA);
    }

    // ========== ПРОЧИЕ ФУНКЦИИ ==========

    @Test
    @DisplayName("Heart: r*(sin(πr) + cos(5θ)) * [cos(θ), sin(θ)]")
    public void testHeartFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("heart");

        // Тест: (1, 0)
        double[] result = func.apply(1, 0);
        double r = 1.0;
        double theta = 0.0;
        double newR = r * (Math.sin(Math.PI * r) + Math.cos(5 * theta));
        assertEquals(newR, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);
    }

    @Test
    @DisplayName("Shell: (r - floor(r)) * [cos(θ+r), sin(θ+r)]")
    public void testShellFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("shell");

        // Тест: (1.5, 0)
        double[] result = func.apply(1.5, 0);
        double r = 1.5;
        double theta = 0.0;
        double newR = r - Math.floor(r); // 0.5
        double newTheta = theta + r;
        assertEquals(newR * Math.cos(newTheta), result[0], DELTA);
        assertEquals(newR * Math.sin(newTheta), result[1], DELTA);
    }

    @Test
    @DisplayName("Whirlpool: r * [cos(θ + 1/r), sin(θ + 1/r)]")
    public void testWhirlpoolFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("whirlpool");

        // Тест: (2, 0) r=2, θ=0
        double[] result = func.apply(2, 0);
        double r = 2.0;
        double newTheta = 0 + 1.0 / r; // 0.5
        assertEquals(r * Math.cos(newTheta), result[0], DELTA);
        assertEquals(r * Math.sin(newTheta), result[1], DELTA);
    }

    @Test
    @DisplayName("Radial: sin(5θ) * r * [cos(θ), sin(θ)]")
    public void testRadialFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("radial");

        // Тест: (1, 1)
        double[] result = func.apply(1, 1);
        double r = Math.sqrt(2);
        double theta = Math.PI / 4;
        double newR = Math.sin(theta * 5) * r;
        assertEquals(newR * Math.cos(theta), result[0], DELTA);
        assertEquals(newR * Math.sin(theta), result[1], DELTA);
    }

    @Test
    @DisplayName("Blob: r * (0.5 + 0.5*sin(5θ)) * [cos(θ), sin(θ)]")
    public void testBlobFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("blob");

        // Тест: (1, 0)
        double[] result = func.apply(1, 0);
        double r = 1.0;
        double theta = 0.0;
        double newR = r * (0.5 + 0.5 * Math.sin(5 * theta));
        assertEquals(newR, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);
    }

    @Test
    @DisplayName("Crosshatch: sin(x)*cos(y), cos(x)*sin(y)")
    public void testCrosshatchFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("crosshatch");

        // Тест: (π/2, π/2)
        double[] result = func.apply(Math.PI / 2, Math.PI / 2);
        double expectedX = Math.sin(Math.PI / 2) * Math.cos(Math.PI / 2);
        double expectedY = Math.cos(Math.PI / 2) * Math.sin(Math.PI / 2);
        assertEquals(expectedX, result[0], DELTA);
        assertEquals(expectedY, result[1], DELTA);
    }

    @Test
    @DisplayName("Logarithmic: ln(r) * [cos(θ), sin(θ)]")
    public void testLogarithmicFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("logarithmic");

        // Тест: (e, 0) r=e, ln(e)=1
        double[] result = func.apply(Math.E, 0);
        assertEquals(1.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);
    }

    @Test
    @DisplayName("SuperShape: проверка на отсутствие NaN")
    public void testSuperShapeFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("super_shape");

        double[] result = func.apply(1, 1);
        assertFalse(Double.isNaN(result[0]));
        assertFalse(Double.isNaN(result[1]));
        assertTrue(Math.abs(result[0]) < 10);
        assertTrue(Math.abs(result[1]) < 10);
    }

    @Test
    @DisplayName("Eyefish: 2*atan(r/2)/r * [x, y]")
    public void testEyefishFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("eyefish");

        // Тест: (0, 0) → (0, 0)
        double[] result = func.apply(0, 0);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест: (2, 0)
        result = func.apply(2, 0);
        double r = 2.0;
        double k = 2.0 * Math.atan(r / 2.0) / r;
        assertEquals(2 * k, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);
    }

    @Test
    @DisplayName("Bubble: 4/(r²+4) * [x, y]")
    public void testBubbleFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("bubble");

        // Тест: (0, 0) → (0, 0)
        double[] result = func.apply(0, 0);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);

        // Тест: (2, 0) r²=4, factor=4/8=0.5
        result = func.apply(2, 0);
        assertEquals(1.0, result[0], DELTA);
        assertEquals(0.0, result[1], DELTA);
    }

    @Test
    @DisplayName("Modulus: периодическое ограничение")
    public void testModulusFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("modulus");

        // Тест: (0.5, 0.5) должно остаться
        double[] result = func.apply(0.5, 0.5);
        assertEquals(0.5, result[0], DELTA);
        assertEquals(0.5, result[1], DELTA);
    }

    @Test
    @DisplayName("Perspective: масштаб зависит от x")
    public void testPerspectiveFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("perspective");

        // Тест: (0, 1) angle=0, scale=1
        double[] result = func.apply(0, 1);
        assertEquals(0.0, result[0], DELTA);
        assertEquals(4.526, result[1], DELTA);
    }

    @Test
    @DisplayName("Rotate: поворот на 0.5 радиан")
    public void testRotateFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("rotate");

        // Тест: (1, 0) → (cos(0.5), sin(0.5))
        double[] result = func.apply(1, 0);
        assertEquals(Math.cos(0.5), result[0], DELTA);
        assertEquals(Math.sin(0.5), result[1], DELTA);
    }

    @Test
    @DisplayName("Crackle: привязка к сетке")
    public void testCrackleFunction() {
        FlameFunction func = FlameFunctionFactory.getFunction("crackle");

        double[] result = func.apply(0.7, 0.3);
        // Должно привязаться к ближайшей ячейке 0.5
        assertTrue(result[0] % 0.5 < DELTA || Math.abs(result[0] % 0.5 - 0.5) < DELTA);
        assertTrue(result[1] % 0.5 < DELTA || Math.abs(result[1] % 0.5 - 0.5) < DELTA);
    }

    // ========== ОБЩИЕ ТЕСТЫ ==========

    @Test
    @DisplayName("Фабрика выбрасывает исключение для неизвестной функции")
    public void testUnknownFunctionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> FlameFunctionFactory.getFunction("unknown_function"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "sinusoidal",
                "spherical",
                "swirl",
                "horseshoe",
                "diamond",
                "polar",
                "waves",
                "spiral",
                "julia",
                "disc",
                "hyperbolic",
                "cosine",
                "tangent",
                "power",
                "exponential",
                "heart",
                "shell",
                "whirlpool",
                "radial",
                "blob",
                "crosshatch",
                "logarithmic",
                "super_shape",
                "eyefish",
                "bubble",
                "modulus",
                "perspective",
                "rotate",
                "crackle",
            })
    @DisplayName("Все функции корректно работают с нулевыми координатами")
    public void testAllFunctionsHandleZero(String functionName) {
        FlameFunction function = FlameFunctionFactory.getFunction(functionName);
        double[] result = function.apply(0, 0);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertFalse(Double.isNaN(result[0]), functionName + " вернула NaN для X");
        assertFalse(Double.isNaN(result[1]), functionName + " вернула NaN для Y");
        assertFalse(Double.isInfinite(result[0]), functionName + " вернула Infinity для X");
        assertFalse(Double.isInfinite(result[1]), functionName + " вернула Infinity для Y");
    }

    @Test
    @DisplayName("Кэш фабрики возвращает тот же экземпляр")
    public void testFactoryCaching() {
        FlameFunction func1 = FlameFunctionFactory.getFunction("swirl");
        FlameFunction func2 = FlameFunctionFactory.getFunction("swirl");

        // Проверяем что это один и тот же объект (кэш работает)
        assertSame(func1, func2, "Фабрика должна возвращать закэшированный экземпляр");
    }
}
