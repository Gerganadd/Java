import bg.sofia.uni.fmi.mjt.photoalbum.ParallelMonochromeAlbumCreator;

public class Main {

    public static void main(String[] args) {
        ParallelMonochromeAlbumCreator p = new ParallelMonochromeAlbumCreator(4);

        String source = "src/bg/sofia/uni/fmi/mjt/photoalbum/resources/colorPictures";
        String destination = "src/bg/sofia/uni/fmi/mjt/photoalbum/resources/blackAndWhitePictures";

        p.processImages(source, destination);
    }
}