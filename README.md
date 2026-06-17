# Fractal Flame

Консольное Java-приложение для генерации изображений фрактального пламени в формате PNG. Проект строит изображения на основе Chaos Game: случайно выбирает аффинные преобразования, применяет нелинейные flame-функции, накапливает попадания в пиксели и выполняет цветовую коррекцию.

Подробнее о требованиях к проекту в [requirements.md](requirements.md).

## Возможности

- генерация RGB PNG-изображений фрактального пламени;
- запуск в однопоточном и многопоточном режиме;
- настройка размера изображения, количества итераций, seed, пути сохранения и числа потоков;
- выбор набора flame-функций и их весов;
- настройка одного или нескольких аффинных преобразований;
- загрузка параметров из JSON-файла;
- переопределение параметров конфигурации через CLI;
- логарифмическая цветовая коррекция и опциональная gamma-коррекция;
- генерация симметричных изображений через параметр уровня симметрии;
- валидация входных параметров и вывод ошибок в консоль;
- логирование хода работы приложения.

Поддерживаемые flame-функции: `sinusoidal`, `spherical`, `swirl`, `horseshoe`, `diamond`, `polar`, `waves`, `spiral`, `julia`, `disc`, `hyperbolic`, `cosine`, `tangent`, `power`, `exponential`, `heart`, `shell`, `whirlpool`, `radial`, `blob`, `crosshatch`, `logarithmic`, `super_shape`, `eyefish`, `bubble`, `modulus`, `perspective`, `rotate`, `crackle`.

## Технический стек

- Java 24;
- Maven 3.9.11+;
- Picocli для CLI-интерфейса;
- Jackson для чтения JSON-конфигурации;
- Log4j 2 и SLF4J для логирования;
- JUnit 5, AssertJ и Jacoco для тестирования и отчета о покрытии;
- Maven Surefire/Failsafe для запуска тестов;
- Spotless, PMD, SpotBugs и Modernizer для статических проверок и форматирования.

## Структура проекта

- `src/main/java/academy/Application.java` - точка входа и обработка CLI-параметров;
- `src/main/java/academy/config` - модели конфигурации и парсер;
- `src/main/java/academy/render` - однопоточный и многопоточный рендереры;
- `src/main/java/academy/transform` - реализация flame-функций;
- `src/main/java/academy/validator` - валидация входных параметров;
- `src/test/java/academy` - unit-тесты;
- `tests` - shell-сценарии для функциональных проверок;
- `random_config.json` - пример JSON-конфигурации.

## Быстрый старт

### 1. Проверьте окружение

```shell
java --version
```

Проект ожидает JDK 24 или новее. Maven можно запускать через wrapper, который уже лежит в репозитории.

### 2. Соберите проект

Для Windows:

```shell
.\mvnw.cmd clean package "-Dmaven.test.skip=true"
.\mvnw.cmd dependency:copy-dependencies "-DincludeScope=runtime"
```

Для Linux/macOS:

```shell
./mvnw clean package -Dmaven.test.skip=true
./mvnw dependency:copy-dependencies -DincludeScope=runtime
```

После сборки JAR будет находиться в директории `target`, а runtime-зависимости - в `target/dependency`.

### 3. Запустите генерацию с параметрами по умолчанию

```shell
java -cp "target\project-1.0.jar;target\dependency\*" academy.Application
```

По умолчанию приложение создаст файл `result.png`.

Для Linux/macOS используйте разделитель classpath `:`:

```shell
java -cp "target/project-1.0.jar:target/dependency/*" academy.Application
```

## Примеры запуска

Генерация небольшого изображения в 4 потока:

```shell
java -cp "target\project-1.0.jar;target\dependency\*" academy.Application -w 1200 -h 800 -i 1000000 -t 4 -o flame.png
```

Запуск с выбранными функциями:

```shell
java -cp "target\project-1.0.jar;target\dependency\*" academy.Application -f swirl:1.0,horseshoe:0.8,diamond:0.6 -i 500000 -o custom.png
```

Запуск с несколькими аффинными преобразованиями:

```shell
java -cp "target\project-1.0.jar;target\dependency\*" academy.Application -ap "0.5,0,0,0,0.5,0/0.5,0,1,0,0.5,0" -o affine.png
```

Запуск с gamma-коррекцией и симметрией:

```shell
java -cp "target\project-1.0.jar;target\dependency\*" academy.Application -g true --gamma 2.2 -s 6 -i 1000000 -o symmetric.png
```

Запуск через JSON-конфигурацию:

```shell
java -cp "target\project-1.0.jar;target\dependency\*" academy.Application --config random_config.json
```

CLI-параметры имеют приоритет над значениями из JSON-файла:

```shell
java -cp "target\project-1.0.jar;target\dependency\*" academy.Application --config random_config.json -o override.png -t 8
```

## Параметры CLI

| Параметр | Описание | Значение по умолчанию |
| --- | --- | --- |
| `-w`, `--width` | ширина изображения | `1920` |
| `-h`, `--height` | высота изображения | `1080` |
| `-i`, `--iteration-count` | количество итераций генерации | `2500` |
| `--seed` | seed генератора случайных чисел | `5` |
| `-f`, `--functions` | функции и веса в формате `name:weight,name:weight` | `swirl:1.0,horseshoe:0.5` |
| `-ap`, `--affine-params` | аффинные преобразования в формате `a,b,c,d,e,f/a,b,c,d,e,f` | два базовых преобразования |
| `-t`, `--threads` | количество потоков | `1` |
| `-o`, `--output-path` | путь к PNG-файлу результата | `result.png` |
| `-c`, `--config` | путь к JSON-конфигурации | не задан |
| `-g`, `--gamma-correction` | включает gamma-коррекцию | `false` |
| `--gamma` | значение gamma | `2.2` |
| `-s`, `--symmetry-level` | количество поворотов для симметрии | `1` |

## JSON-конфигурация

Пример:

```json
{
  "size": {
    "width": 1440,
    "height": 1440
  },
  "iteration_count": 15000000,
  "output_path": "random_flame.png",
  "threads": 16,
  "seed": 3860815953430870525,
  "gamma_correction": true,
  "gamma": 2.0,
  "symmetry_level": 1,
  "functions": [
    {
      "name": "horseshoe",
      "weight": 0.85
    },
    {
      "name": "diamond",
      "weight": 0.98
    },
    {
      "name": "swirl",
      "weight": 0.84
    }
  ],
  "affine_params": [
    {
      "a": 0.2,
      "b": 0.7,
      "c": 0.5,
      "d": 0.0,
      "e": -0.4,
      "f": -0.2
    }
  ]
}
```

## Тесты

Запуск unit-тестов:

```shell
.\mvnw.cmd test
```

Для Linux/macOS:

```shell
./mvnw test
```

Функциональные shell-тесты находятся в директории `tests`.
