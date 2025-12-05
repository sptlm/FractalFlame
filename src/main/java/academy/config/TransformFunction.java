package academy.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransformFunction {
    @JsonProperty("name")
    private String name;

    @JsonProperty("weight")
    private double weight = 1.0;

    public TransformFunction() {}

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
}
