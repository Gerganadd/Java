package bg.sofia.uni.fmi.mjt.photoalbum.threads;

import bg.sofia.uni.fmi.mjt.photoalbum.BlockingQueue;
import bg.sofia.uni.fmi.mjt.photoalbum.Image;
import bg.sofia.uni.fmi.mjt.photoalbum.ImageProcessor;

import java.nio.file.Path;

public class Producer extends Thread {
    public static final String GROUP_NAME = "Image producers";
    public static final String NAME = "Producer - ";

    private Path imagePath;
    private BlockingQueue<Image> images;

    public Producer(Path imagePath, BlockingQueue<Image> images) {
        this.imagePath = imagePath;
        this.images = images;
    }

    @Override
    public void run() {
        Image currentImage = ImageProcessor.loadImage(imagePath);

        this.images.add(currentImage);
    }
}
