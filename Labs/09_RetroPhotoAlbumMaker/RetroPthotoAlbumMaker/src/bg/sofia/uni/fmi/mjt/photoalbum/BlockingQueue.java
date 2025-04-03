package bg.sofia.uni.fmi.mjt.photoalbum;

import java.util.LinkedList;
import java.util.List;

public class BlockingQueue<T> {
    private static int addedElementsCount = 0;
    private static int removedElementsCount = 0;

    private List<T> elements;
    private int capacity;

    public BlockingQueue(int capacity) {
        elements = new LinkedList<>();
        this.capacity = capacity;
    }

    public synchronized void add(T element) {
        addedElementsCount++;

        while (elements.size() > capacity) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        elements.add(element);

        this.notifyAll();
    }

    public synchronized T getNext() {
        while (elements.isEmpty()) {
            if (addedElementsCount == removedElementsCount && addedElementsCount != 0) {
                return null;
            }

            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        T element = elements.getFirst();
        elements.removeFirst();

        removedElementsCount++;

        this.notify();
        return element;
    }
}
