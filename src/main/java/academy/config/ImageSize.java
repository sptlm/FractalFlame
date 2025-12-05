package academy.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageSize {
    @JsonProperty("width")
    private int width = 1920;

    @JsonProperty("height")
    private int height = 1080;

    public ImageSize() {}

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
}
