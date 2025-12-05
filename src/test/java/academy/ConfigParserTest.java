package academy;

import static org.junit.jupiter.api.Assertions.*;

import academy.config.AffineTransform;
import academy.config.ConfigParser;
import academy.config.TransformFunction;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тесты парсинга конфигурации")
public class ConfigParserTest {

    @Test
    @DisplayName("Парсинг функций трансформации из строки")
    public void testParseFunctions() {
        String functionsStr = "swirl:1.0,horseshoe:0.8,julia:0.5";
        List<TransformFunction> functions = ConfigParser.parseFunctions(functionsStr);

        assertNotNull(functions);
        assertEquals(3, functions.size());

        assertEquals("swirl", functions.get(0).getName());
        assertEquals(1.0, functions.get(0).getWeight(), 0.001);

        assertEquals("horseshoe", functions.get(1).getName());
        assertEquals(0.8, functions.get(1).getWeight(), 0.001);

        assertEquals("julia", functions.get(2).getName());
        assertEquals(0.5, functions.get(2).getWeight(), 0.001);
    }

    @Test
    @DisplayName("Парсинг одной функции трансформации")
    public void testParseSingleFunction() {
        String functionsStr = "swirl:1.5";
        List<TransformFunction> functions = ConfigParser.parseFunctions(functionsStr);

        assertNotNull(functions);
        assertEquals(1, functions.size());
        assertEquals("swirl", functions.get(0).getName());
        assertEquals(1.5, functions.get(0).getWeight(), 0.001);
    }

    @Test
    @DisplayName("Парсинг функций с пробелами")
    public void testParseFunctionsWithSpaces() {
        String functionsStr = "swirl : 1.0 , horseshoe : 0.8";
        List<TransformFunction> functions = ConfigParser.parseFunctions(functionsStr);

        assertNotNull(functions);
        assertEquals(2, functions.size());
        assertEquals("swirl", functions.get(0).getName());
        assertEquals(1.0, functions.get(0).getWeight(), 0.001);
    }

    @Test
    @DisplayName("Парсинг некорректной строки функций выбрасывает исключение")
    public void testParseInvalidFunctions() {
        String functionsStr = "swirl:invalid";
        assertThrows(IllegalArgumentException.class, () -> {
            ConfigParser.parseFunctions(functionsStr);
        });
    }

    @Test
    @DisplayName("Парсинг аффинных параметров из строки")
    public void testParseAffineParams() {
        String paramsStr = "0.5,0.0,0.0,0.0,0.5,0.0/0.5,0.0,1.0,0.0,0.5,0.0";
        List<AffineTransform> transforms = ConfigParser.parseAffineParams(paramsStr);

        assertNotNull(transforms);
        assertEquals(2, transforms.size());

        // Первое преобразование
        assertEquals(0.5, transforms.get(0).getA(), 0.001);
        assertEquals(0.0, transforms.get(0).getB(), 0.001);
        assertEquals(0.0, transforms.get(0).getC(), 0.001);
        assertEquals(0.0, transforms.get(0).getD(), 0.001);
        assertEquals(0.5, transforms.get(0).getE(), 0.001);
        assertEquals(0.0, transforms.get(0).getF(), 0.001);

        // Второе преобразование
        assertEquals(0.5, transforms.get(1).getA(), 0.001);
        assertEquals(0.0, transforms.get(1).getB(), 0.001);
        assertEquals(1.0, transforms.get(1).getC(), 0.001);
        assertEquals(0.0, transforms.get(1).getD(), 0.001);
        assertEquals(0.5, transforms.get(1).getE(), 0.001);
        assertEquals(0.0, transforms.get(1).getF(), 0.001);
    }

    @Test
    @DisplayName("Парсинг одного аффинного преобразования")
    public void testParseSingleAffineTransform() {
        String paramsStr = "1.0,1.0,1.0,1.0,1.0,1.0";
        List<AffineTransform> transforms = ConfigParser.parseAffineParams(paramsStr);

        assertNotNull(transforms);
        assertEquals(1, transforms.size());
        assertEquals(1.0, transforms.get(0).getA(), 0.001);
        assertEquals(1.0, transforms.get(0).getB(), 0.001);
        assertEquals(1.0, transforms.get(0).getC(), 0.001);
        assertEquals(1.0, transforms.get(0).getD(), 0.001);
        assertEquals(1.0, transforms.get(0).getE(), 0.001);
        assertEquals(1.0, transforms.get(0).getF(), 0.001);
    }

    @Test
    @DisplayName("Парсинг некорректных аффинных параметров выбрасывает исключение")
    public void testParseInvalidAffineParams() {
        // Неправильное количество параметров
        String paramsStr = "0.5,0.0,0.0,0.0,0.5";
        assertThrows(IllegalArgumentException.class, () -> {
            ConfigParser.parseAffineParams(paramsStr);
        });
    }

    @Test
    @DisplayName("Парсинг аффинных параметров с нечисловыми значениями выбрасывает исключение")
    public void testParseInvalidAffineParamsNonNumeric() {
        String paramsStr = "invalid,0.0,0.0,0.0,0.5,0.0";
        assertThrows(IllegalArgumentException.class, () -> {
            ConfigParser.parseAffineParams(paramsStr);
        });
    }
}
