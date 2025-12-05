package academy.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageWriter {
    private static final Logger logger = LoggerFactory.getLogger(ImageWriter.class);

    public static void saveImage(byte[][][] colorBuffer, String outputPath) {
        try {
            logger.info("Сохранение изображения в: {}", outputPath);
            long startTime = System.currentTimeMillis();

            int height = colorBuffer.length;
            int width = colorBuffer[0].length;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int r = colorBuffer[y][x][0] & 0xFF;
                    int g = colorBuffer[y][x][1] & 0xFF;
                    int b = colorBuffer[y][x][2] & 0xFF;

                    int rgb = (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, rgb);
                }
            }

            File outputFile = new File(outputPath);
            ImageIO.write(image, "PNG", outputFile);
            long endTime = System.currentTimeMillis();
            logger.debug("Сохранение завершено за {} мс", endTime - startTime);
            logger.info("Изображение сохранено по пути: {}", outputFile.getAbsolutePath());

        } catch (Exception e) {
            logger.error("Ошибка при сохранении изображения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении изображения: " + e.getMessage(), e);
        }
    }
}
