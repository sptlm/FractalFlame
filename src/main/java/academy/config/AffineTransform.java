package com.fractal.flame.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс для представления аффинного преобразования
 * Аффинное преобразование описывается матрицей:
 * | a  b  c |
 * | d  e  f |
 * | 0  0  1 |
 */
public class AffineTransform {
    // Масштаб/вращение X
    @JsonProperty("a")
    private double a;

    // Сдвиг-смешивание X от Y
    @JsonProperty("b")
    private double b;

    // Сдвиг по X
    @JsonProperty("c")
    private double c;

    // Смешивание Y от X
    @JsonProperty("d")
    private double d;

    // Масштаб/вращение Y
    @JsonProperty("e")
    private double e;

    // Сдвиг по Y
    @JsonProperty("f")
    private double f;

    public AffineTransform() {
    }

    public AffineTransform(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    /**
     * Применяет аффинное преобразование к точке (x, y)
     * Новая точка: (x', y') = (a*x + b*y + c, d*x + e*y + f)
     */
    public double[] transform(double x, double y) {
        double newX = a * x + b * y + c;
        double newY = d * x + e * y + f;
        return new double[]{newX, newY};
    }

    /**
     * Валидирует аффинное преобразование
     */
    public void validate() {
        // Проверяем, что матрица не является особой (детерминант не ноль)
        double determinant = a * e - b * d;
        if (Math.abs(determinant) < 1e-10) {
            throw new IllegalArgumentException("Аффинное преобразование является особой матрицей (детерминант близок к нулю)");
        }
    }
}
