package academy.render;

/**
 * Интерфейс для всех функций трансформации
 * Каждая функция принимает координаты (x, y) и возвращает трансформированные координаты
 */
public interface FlameFunction {
    /**
     * Применяет трансформацию к точке (x, y)
     * @param x координата X
     * @param y координата Y
     * @return массив [newX, newY] - трансформированные координаты
     */
    double[] apply(double x, double y);

    /**
     * Возвращает имя функции
     */
    String getName();
}
