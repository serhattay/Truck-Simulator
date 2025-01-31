import java.util.NoSuchElementException;

public class Queue<E> {

    private Node<E> head;
    private Node<E> tail;
    public int numberOfElements;
    public Queue() {
        head = null;
        tail = null;
        numberOfElements = 0;
    }

    public Queue(E element) {
        enqueue(element);
    }

    public Queue(E[] elementList) {
        for (E element: elementList) {
            enqueue(element);
        }
    }
    private static class Node<E> {
        private E element;
        private Node<E> next;

        // <E> part need not be specified after Node in the constructor because it is already defined
        // in the class header
        private Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    public void enqueue(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
        } else if (head != null && tail != null) {
            tail.next = newNode;
            tail = tail.next;
        }

        numberOfElements += 1;
    }

    public E dequeue() {
        if (head != null) {
            E returnValue = head.element;
            head = head.next;
            if (head == null) {
                tail = null;
            }

            numberOfElements -= 1;

            return returnValue;
        } else {
            throw new NoSuchElementException("The queue is empty.");
        }
    }

    public boolean hasNext() {
        return numberOfElements > 0;
    }
    public boolean isEmpty() {
        return numberOfElements == 0;
    }
}
