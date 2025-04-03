package bg.sofia.uni.fmi.mjt.photoalbum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class ImageProcessor {
    private ImageProcessor() {
        // don't want to make instances of this class
    }

    public static Image loadImage(Path imagePath) {
        try {
            BufferedImage imageData = ImageIO.read(imagePath.toFile());
            return new Image(imagePath.getFileName().toString(), imageData);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    String.format("Failed to load image %s", imagePath.toString()), e);
        }
    }

    public static Image convertToBlackAndWhite(Image image) {
        BufferedImage processedData = new BufferedImage(
                image.data.getWidth(), image.data.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        processedData.getGraphics().drawImage(image.data, 0, 0, null);

        return new Image(image.name, processedData);
    }
}
