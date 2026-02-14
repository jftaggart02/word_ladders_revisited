import java.util.NoSuchElementException;

public class Queue<E> {

    /**
     * Default constructor. Create an empty queue.
     * */
    public Queue() {
        this.beginning = null;
        this.end = null;
    }

    /**
     * Add an item to the back of the queue.
     * */
    public void enqueue(E value) {

        var newItem = new Item(value, this.beginning);
        if (this.beginning != null) {
            this.beginning.prev = newItem;
        }
        this.beginning = newItem;
        if (this.end == null) {
            this.end = this.beginning;
        }

    }

    /**
     * Remove an item from the front of the queue.
     * */
    public E dequeue() throws NoSuchElementException {

        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty. Cannot dequeue an item.");
        }

        E data = this.end.data;

        // If there is only one element, empty the queue.
        if (this.beginning == this.end) {
            this.end = null;
            this.beginning = null;
        }
        else {
            this.end = this.end.prev;
            this.end.next = null;
        }

        return data;

    }

    /**
     * Return true if the queue is empty and false otherwise.
     * */
    public boolean isEmpty() {
        return this.beginning == null;
    }

    /**
     * Represents an item in the queue.
     * */
    private class Item {
        public E data;
        public Item next;
        public Item prev;

        /**
         * Initialize an item with data and next pointer.
         * */
        public Item(E data, Item next) {
            this.data = data;
            this.next = next;
            this.prev = null;
        }
    }

    private Item beginning;
    private Item end;

}
