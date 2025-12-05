package academy.transform;

import java.util.HashMap;
import java.util.Map;

public class FlameFunctionFactory {

    private static final Map<String, FlameFunction> CACHE = new HashMap<>();

    public static FlameFunction createFunction(String name) {
        String key = name.toLowerCase();
        return CACHE.computeIfAbsent(key, FlameFunctionFactory::createNewFunction);
    }

    private static FlameFunction createNewFunction(String name) {
        return switch (name.toLowerCase()) {
            // Классические функции
            case "sinusoidal" -> new SinusoidalFunction();
            case "spherical" -> new SphericalFunction();
            case "swirl" -> new SwirlFunction();
            case "horseshoe" -> new HorseshoeFunction();
            case "diamond" -> new DiamondFunction();

            // Полярные преобразования
            case "polar" -> new PolarFunction();
            case "waves" -> new WavesFunction();
            case "spiral" -> new SpiralFunction();

            // Комплексные функции
            case "julia" -> new JuliaFunction();
            case "disc" -> new DiscFunction();
            case "hyperbolic" -> new HyperbolicFunction();

            // Тригонометрические функции
            case "cosine" -> new CosineFunction();
            case "tangent" -> new TangentFunction();
            case "power" -> new PowerFunction();
            case "exponential" -> new ExponentialFunction();

            // Прочие функции
            case "heart" -> new HeartFunction();
            case "shell" -> new ShellFunction();
            case "whirlpool" -> new WhirlpoolFunction();
            case "radial" -> new RadialFunction();
            case "blob" -> new BlobFunction();
            case "crosshatch" -> new CrosshatchFunction();
            case "logarithmic" -> new LogarithmicFunction();
            case "super_shape" -> new SuperShapeFunction();
            case "eyefish" -> new EyefishFunction();
            case "bubble" -> new BubbleFunction();
            case "modulus" -> new ModulusFunction();
            case "perspective" -> new PerspectiveFunction();
            case "rotate" -> new RotateFunction();
            case "crackle" -> new CrackleFunction();

            default -> throw new IllegalArgumentException("Неизвестная функция трансформации: " + name);
        };
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
            double r = Math.sqrt(x * x + y * y);
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
            double k = 2.0 * Math.atan(r / 2.0) / r;
            if (r == 0) k = 1.0;
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
