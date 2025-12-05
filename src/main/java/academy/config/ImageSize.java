package com.fractal.flame.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс для представления размера изображения
 */
public class ImageSize {
    // Ширина изображения в пиксельях
    @JsonProperty("width")
    private int width = 1920;

    // Высота изображения в пиксельях
    @JsonProperty("height")
    private int height = 1080;

    public ImageSize() {
    }

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Валидирует размер изображения
     */
    public void validate() {
        if (width <= 0 || width > 8192) {
            throw new IllegalArgumentException("Ширина должна быть между 1 и 8192, получено: " + width);
        }
        if (height <= 0 || height > 8192) {
            throw new IllegalArgumentException("Высота должна быть между 1 и 8192, получено: " + height);
        }
    }
}
