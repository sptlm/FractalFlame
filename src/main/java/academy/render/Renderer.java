package academy.render;

import academy.config.FractalConfig;

public interface Renderer {
    byte[][][] render(FractalConfig config);
}
