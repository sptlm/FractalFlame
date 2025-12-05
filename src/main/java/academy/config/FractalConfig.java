package academy.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class FractalConfig {
    @JsonProperty("size")
    private ImageSize size = new ImageSize(1920, 1080);

    @JsonProperty("iteration_count")
    private int iterationCount = 2500;

    @JsonProperty("output_path")
    private String outputPath = "result.png";

    @JsonProperty("threads")
    private int threads = 1;

    @JsonProperty("seed")
    private long seed = 5L;

    @JsonProperty("functions")
    private List<TransformFunction> functions = new ArrayList<>();

    @JsonProperty("affine_params")
    private List<AffineTransform> affineParams = new ArrayList<>();

    @JsonProperty("gamma_correction")
    private boolean gammaCorrection = false;

    @JsonProperty("gamma")
    private double gamma = 2.2;

    @JsonProperty("symmetry_level")
    private int symmetryLevel = 1;

    public FractalConfig() {
        initializeDefaults();
    }

    private void initializeDefaults() {
        if (functions.isEmpty()) {
            functions.add(new TransformFunction("swirl", 1.0));
            functions.add(new TransformFunction("horseshoe", 0.5));
        }

        if (affineParams.isEmpty()) {
            affineParams.add(new AffineTransform(0.5, 0.0, 0.0, 0.0, 0.5, 0.0));
            affineParams.add(new AffineTransform(0.5, 0.0, 1.0, 0.0, 0.5, 0.0));
        }
    }

    public ImageSize getSize() {
        return size;
    }

    public void setSize(ImageSize size) {
        this.size = size;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public List<TransformFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<TransformFunction> functions) {
        this.functions = functions;
    }

    public List<AffineTransform> getAffineParams() {
        return affineParams;
    }

    public void setAffineParams(List<AffineTransform> affineParams) {
        this.affineParams = affineParams;
    }

    public boolean isGammaCorrection() {
        return gammaCorrection;
    }

    public void setGammaCorrection(boolean gammaCorrection) {
        this.gammaCorrection = gammaCorrection;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getSymmetryLevel() {
        return symmetryLevel;
    }

    public void setSymmetryLevel(int symmetryLevel) {
        this.symmetryLevel = symmetryLevel;
    }
}
