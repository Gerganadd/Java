package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.photoalbum.threads.Consumer;
import bg.sofia.uni.fmi.mjt.photoalbum.threads.Producer;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {
    private static final String MATCH_ALL_PICTURES = "*.{jpg, jpeg, png}";

    private int imageProcessorsCount;
    private BlockingQueue<Image> imagesToConvert;
    private List<Thread> consumers;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        if (imageProcessorsCount < 1) {
            throw new IllegalArgumentException("ImageProcessorsCount must be greater than 0");
        }

        this.imageProcessorsCount = imageProcessorsCount;
        this.imagesToConvert = new BlockingQueue<>(imageProcessorsCount);
        this.consumers = new ArrayList<>();
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        validateDirectoryName(sourceDirectory);
        validateDirectoryName(outputDirectory);

        checkIfDirectoryExists(sourceDirectory);

        ThreadFactory producerFactory = configThreadFactory(Producer.GROUP_NAME, Producer.NAME);
        ThreadFactory consumerFactory = configThreadFactory(Consumer.GROUP_NAME, Consumer.NAME);

        Path sourceDir = Path.of(sourceDirectory);
        Path destinationDir = Path.of(outputDirectory);

        try (DirectoryStream<Path> imagesStream = Files.newDirectoryStream(sourceDir, MATCH_ALL_PICTURES)) {
            imagesStream.forEach(x -> producerFactory
                        .newThread(new Producer(x, imagesToConvert))
                        .start());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < imageProcessorsCount; i++) {
            Thread currentConsumer = consumerFactory
                    .newThread(new Consumer(imagesToConvert, destinationDir));

            consumers.add(currentConsumer);
            currentConsumer.start();
        }
    }

    private void validateDirectoryName(String directoryName) {
        if (directoryName == null || directoryName.isBlank()) {
            throw new IllegalArgumentException("Directory name can't be null or empty");
        }
    }

    private void checkIfDirectoryExists(String directoryName) {
        Path dir = Path.of(directoryName);

        if (!dir.toFile().exists()) {
            throw new IllegalArgumentException("Directory doesn't exists");
        }
    }

    private ThreadFactory configThreadFactory(String groupName, String threadName) {
        return Thread
                .ofPlatform()
                .group(new ThreadGroup(groupName))
                .name(threadName, 0)
                .factory();
    }
}
