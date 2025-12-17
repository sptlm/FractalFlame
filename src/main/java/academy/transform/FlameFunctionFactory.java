package academy.transform;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FlameFunctionFactory {

    private static final Map<String, FlameFunction> FUNCTIONS;

    static {
        Map<String, FlameFunction> map = new HashMap<>();

        // Классические функции
        map.put("sinusoidal", new SinusoidalFunction());
        map.put("spherical", new SphericalFunction());
        map.put("swirl", new SwirlFunction());
        map.put("horseshoe", new HorseshoeFunction());
        map.put("diamond", new DiamondFunction());

        // Полярные преобразования
        map.put("polar", new PolarFunction());
        map.put("waves", new WavesFunction());
        map.put("spiral", new SpiralFunction());

        // Комплексные функции
        map.put("julia", new JuliaFunction());
        map.put("disc", new DiscFunction());
        map.put("hyperbolic", new HyperbolicFunction());

        // Тригонометрические функции
        map.put("cosine", new CosineFunction());
        map.put("tangent", new TangentFunction());
        map.put("power", new PowerFunction());
        map.put("exponential", new ExponentialFunction());

        // Прочие функции
        map.put("heart", new HeartFunction());
        map.put("shell", new ShellFunction());
        map.put("whirlpool", new WhirlpoolFunction());
        map.put("radial", new RadialFunction());
        map.put("blob", new BlobFunction());
        map.put("crosshatch", new CrosshatchFunction());
        map.put("logarithmic", new LogarithmicFunction());
        map.put("super_shape", new SuperShapeFunction());
        map.put("eyefish", new EyefishFunction());
        map.put("bubble", new BubbleFunction());
        map.put("modulus", new ModulusFunction());
        map.put("perspective", new PerspectiveFunction());
        map.put("rotate", new RotateFunction());
        map.put("crackle", new CrackleFunction());

        FUNCTIONS = Collections.unmodifiableMap(map);
    }

    public static FlameFunction getFunction(String name) {
        FlameFunction function = FUNCTIONS.get(name.toLowerCase());
        if (function == null) {
            throw new IllegalArgumentException("Неизвестная функция трансформации: " + name);
        }
        return function;
    }

    // ========== КЛАССИЧЕСКИЕ ФУНКЦИИ ==========

    static class SinusoidalFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            return new double[] {Math.sin(x), Math.sin(y)};
        }
    }

    static class SphericalFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r2 = x * x + y * y;
            if (r2 == 0) return new double[] {0, 0};
            double factor = 1.0 / r2;
            return new double[] {x * factor, y * factor};
        }
    }

    static class SwirlFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r2 = x * x + y * y;
            double newX = x * Math.sin(r2) - y * Math.cos(r2);
            double newY = x * Math.cos(r2) + y * Math.sin(r2);
            return new double[] {newX, newY};
        }
    }

    static class HorseshoeFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            if (r == 0) return new double[] {0, 0};
            double newX = (x - y) * (x + y) / r;
            double newY = 2 * x * y / r;
            return new double[] {newX, newY};
        }
    }

    static class DiamondFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = Math.sin(theta) * Math.cos(r);
            double newY = Math.cos(theta) * Math.sin(r);
            return new double[] {newX, newY};
        }
    }

    // ========== ПОЛЯРНЫЕ ПРЕОБРАЗОВАНИЯ ==========

    static class PolarFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = theta / Math.PI;
            double newY = r - 1.0;
            return new double[] {newX, newY};
        }
    }

    static class WavesFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double newX = x + 0.25 * Math.sin(y / 0.5);
            double newY = y + 0.25 * Math.sin(x / 0.5);
            return new double[] {newX, newY};
        }
    }

    static class SpiralFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            if (r == 0) return new double[] {0, 0}; // ← Добавить проверку

            double theta = Math.atan2(y, x);
            double newX = (Math.cos(theta) + Math.sin(r)) / r;
            double newY = (Math.sin(theta) - Math.cos(r)) / r;
            return new double[] {newX, newY};
        }
    }

    // ========== КОМПЛЕКСНЫЕ ФУНКЦИИ ==========

    static class JuliaFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newR = Math.sqrt(r);
            double newTheta = theta / 2.0 + Math.PI;
            double newX = newR * Math.cos(newTheta);
            double newY = newR * Math.sin(newTheta);
            return new double[] {newX, newY};
        }
    }

    static class DiscFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = theta * Math.sin(Math.PI * r) / Math.PI;
            double newY = theta * Math.cos(Math.PI * r) / Math.PI;
            return new double[] {newX, newY};
        }
    }

    static class HyperbolicFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            if (r == 0) return new double[] {0, 0};
            double theta = Math.atan2(y, x);
            double newX = Math.sin(theta) / r;
            double newY = Math.cos(theta) * r;
            return new double[] {newX, newY};
        }
    }

    // ========== ТРИГОНОМЕТРИЧЕСКИЕ ФУНКЦИИ ==========

    static class CosineFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            return new double[] {Math.cos(Math.PI * x) * Math.cosh(y), -Math.sin(Math.PI * x) * Math.sinh(y)};
        }
    }

    static class TangentFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            return new double[] {Math.sin(x) / Math.cos(y), Math.tan(y)};
        }
    }

    static class PowerFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newR = Math.pow(r, Math.cos(theta));
            double newX = newR * Math.cos(theta);
            double newY = newR * Math.sin(theta);
            return new double[] {newX, newY};
        }
    }

    static class ExponentialFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double exp = Math.exp(x - 1);
            double newX = exp * Math.cos(Math.PI * y);
            double newY = exp * Math.sin(Math.PI * y);
            return new double[] {newX, newY};
        }
    }

    // ========== ПРОЧИЕ ФУНКЦИИ ==========

    static class HeartFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newR = r * (Math.sin(Math.PI * r) + Math.cos(5 * theta));
            double newX = newR * Math.cos(theta);
            double newY = newR * Math.sin(theta);
            return new double[] {newX, newY};
        }
    }

    static class ShellFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newR = r - Math.floor(r);
            double newX = newR * Math.cos(theta + r);
            double newY = newR * Math.sin(theta + r);
            return new double[] {newX, newY};
        }
    }

    static class WhirlpoolFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            if (r == 0) return new double[] {0, 0}; // ← Добавить проверку

            double theta = Math.atan2(y, x);
            double newTheta = theta + 1.0 / r;
            double newX = r * Math.cos(newTheta);
            double newY = r * Math.sin(newTheta);
            return new double[] {newX, newY};
        }
    }

    static class RadialFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newR = Math.sin(theta * 5) * r;
            double newX = newR * Math.cos(theta);
            double newY = newR * Math.sin(theta);
            return new double[] {newX, newY};
        }
    }

    static class BlobFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newR = r * (0.5 + 0.5 * Math.sin(5 * theta));
            double newX = newR * Math.cos(theta);
            double newY = newR * Math.sin(theta);
            return new double[] {newX, newY};
        }
    }

    static class CrosshatchFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double newX = Math.sin(x) * Math.cos(y);
            double newY = Math.cos(x) * Math.sin(y);
            return new double[] {newX, newY};
        }
    }

    static class LogarithmicFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            if (r <= 0) r = 0.00001;
            double theta = Math.atan2(y, x);
            double newR = Math.log(r);
            double newX = newR * Math.cos(theta);
            double newY = newR * Math.sin(theta);
            return new double[] {newX, newY};
        }
    }

    static class SuperShapeFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double theta = Math.atan2(y, x);
            double n = 4.0;
            double m = 3.0;
            double a = 1.0;
            double b = 1.0;
            double rho = Math.pow(
                    Math.pow(Math.abs(Math.cos(m * theta / 4.0) / a), n)
                            + Math.pow(Math.abs(Math.sin(m * theta / 4.0) / b), n),
                    -1.0 / n);
            double newX = rho * Math.cos(theta);
            double newY = rho * Math.sin(theta);
            return new double[] {newX, newY};
        }
    }

    static class EyefishFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double k;
            if (r == 0) {
                k = 1.0;
            } else {
                k = 2.0 * Math.atan(r / 2.0) / r;
            }

            double newX = x * k;
            double newY = y * k;
            return new double[] {newX, newY};
        }
    }

    static class BubbleFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r2 = x * x + y * y;
            double factor = 4.0 / (r2 + 4.0);
            double newX = x * factor;
            double newY = y * factor;
            return new double[] {newX, newY};
        }
    }

    static class ModulusFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double m = 1.0;
            double newX = x - m * Math.floor((x + m) / (2.0 * m));
            double newY = y - m * Math.floor((y + m) / (2.0 * m));
            return new double[] {newX, newY};
        }
    }

    static class PerspectiveFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double angle = 30.0;
            double dist = 15.0;
            double depth = Math.cos(angle);
            double scale = dist / (dist * depth + Math.sin(angle) * x + 1);
            double newX = scale * x;
            double newY = scale * y;
            return new double[] {newX, newY};
        }
    }

    static class RotateFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double angle = 0.5;
            double cos_a = Math.cos(angle);
            double sin_a = Math.sin(angle);
            double newX = x * cos_a - y * sin_a;
            double newY = x * sin_a + y * cos_a;
            return new double[] {newX, newY};
        }
    }

    static class CrackleFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double cellSize = 0.5;
            double dx = x / cellSize;
            double dy = y / cellSize;
            double fx = dx - Math.floor(dx);
            double fy = dy - Math.floor(dy);
            double newX = (Math.floor(dx) + (fx > 0.5 ? 1 : 0)) * cellSize;
            double newY = (Math.floor(dy) + (fy > 0.5 ? 1 : 0)) * cellSize;
            return new double[] {newX, newY};
        }
    }
}
