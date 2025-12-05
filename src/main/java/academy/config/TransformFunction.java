package com.fractal.flame.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс для представления функции трансформации
 */
public class TransformFunction {
    // Название функции (swirl, horseshoe, julia, и т.д.)
    @JsonProperty("name")
    private String name;

    // Вес применяемой функции
    @JsonProperty("weight")
    private double weight = 1.0;

    public TransformFunction() {
    }

    public TransformFunction(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Валидирует функцию трансформации
     */
    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Название функции не может быть пустым");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Вес функции не может быть отрицательным: " + weight);
        }
    }
}
