package bg.sofia.uni.fmi.mjt.photoalbum.threads;

import bg.sofia.uni.fmi.mjt.photoalbum.BlockingQueue;
import bg.sofia.uni.fmi.mjt.photoalbum.Image;
import bg.sofia.uni.fmi.mjt.photoalbum.ImageProcessor;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Consumer extends Thread {
    public static final String GROUP_NAME = "Image consumers";
    public static final String NAME = "Consumer - ";
    private static final String IMAGE_FORMAT_NAME = "jpg";

    private BlockingQueue<Image> images;
    private final Path destination;

    public Consumer(BlockingQueue<Image> images, Path destination) {
        this.images = images;
        this.destination = destination;
    }

    @Override
    public void run() {
        while (true) {
            Image currentImage = images.getNext();

            if (currentImage == null) {
                break;
            }

            Image blackAndWhite = ImageProcessor.convertToBlackAndWhite(currentImage);

            try {
                String imageName = destination + File.separator + blackAndWhite.getName();
                ImageIO.write(blackAndWhite.getData(), IMAGE_FORMAT_NAME, new File(imageName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
