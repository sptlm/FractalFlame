package academy.render;

/**
 * Реализация различных функций трансформации для фрактального пламени
 * На основе описания из https://flam3.com/flame_draves.pdf
 */
public class FlameFunctionFactory {

    /**
     * Создает функцию трансформации по имени
     * @param name имя функции (swirl, horseshoe, julia, diamond, polar, waves)
     * @return реализация функции
     */
    public static FlameFunction createFunction(String name) {
        switch (name.toLowerCase()) {
            case "swirl":
                return new SwirlFunction();
            case "horseshoe":
                return new HorseshoeFunction();
            case "julia":
                return new JuliaFunction();
            case "diamond":
                return new DiamondFunction();
            case "polar":
                return new PolarFunction();
            case "waves":
                return new WavesFunction();
            case "spiral":
                return new SpiralFunction();
            case "sinusoidal":
                return new SinusoidalFunction();
            default:
                throw new IllegalArgumentException("Неизвестная функция трансформации: " + name);
        }
    }

    /**
     * Функция Swirl (Вихрь)
     * Применяет логарифмическую спираль
     */
    static class SwirlFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r2 = x * x + y * y;
            double newX = x * Math.sin(r2) - y * Math.cos(r2);
            double newY = x * Math.cos(r2) + y * Math.sin(r2);
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "swirl";
        }
    }

    /**
     * Функция Horseshoe (Подкова)
     * Создает форму подковы
     */
    static class HorseshoeFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            if (r == 0) return new double[]{0, 0};
            double newX = (x - y) * (x + y) / r;
            double newY = 2 * x * y / r;
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "horseshoe";
        }
    }

    /**
     * Функция Julia (Множество Джулии)
     * Применяет комплексное преобразование для создания фракталов типа Джулии
     */
    static class JuliaFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newR = Math.sqrt(r);
            double newTheta = theta / 2.0 + (Math.random() > 0.5 ? Math.PI : 0);
            double newX = newR * Math.cos(newTheta);
            double newY = newR * Math.sin(newTheta);
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "julia";
        }
    }

    /**
     * Функция Diamond (Алмаз)
     * Создает форму алмаза через преобразование в полярные координаты
     */
    static class DiamondFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = Math.sin(theta) * Math.cos(r);
            double newY = Math.cos(theta) * Math.sin(r);
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "diamond";
        }
    }

    /**
     * Функция Polar (Полярная)
     * Преобразует декартовы координаты в полярные и применяет преобразование
     */
    static class PolarFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            // Преобразуем в полярные координаты
            double newX = theta / Math.PI;
            double newY = r - 1.0;
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "polar";
        }
    }

    /**
     * Функция Waves (Волны)
     * Применяет волновое преобразование через синусоидальные функции
     */
    static class WavesFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double newX = x + 0.25 * Math.sin(y / 0.5);
            double newY = y + 0.25 * Math.sin(x / 0.5);
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "waves";
        }
    }

    /**
     * Функция Spiral (Спираль)
     * Создает спиральный паттерн
     */
    static class SpiralFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            // Логарифмическая спираль
            double newR = r / (theta + 1.0);
            double newX = newR * Math.cos(theta);
            double newY = newR * Math.sin(theta);
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "spiral";
        }
    }

    /**
     * Функция Sinusoidal (Синусоидальная)
     * Применяет синусоидальное преобразование координат
     */
    static class SinusoidalFunction implements FlameFunction {
        @Override
        public double[] apply(double x, double y) {
            double newX = Math.sin(x);
            double newY = Math.sin(y);
            return new double[]{newX, newY};
        }

        @Override
        public String getName() {
            return "sinusoidal";
        }
    }
}
