package gc.apiClient.webclient;
import java.util.Arrays;

public class TokenQue {

	private String[] queue;
	private int front; // Index of the front element
	private int rear; // Index of the rear element
	private int size; // Current size of the queue
	private int capacity; // Maximum capacity of the queue

	// Constructor to initialize the queue with a given capacity
	public TokenQue(int capacity) {
		this.capacity = capacity;
		queue = new String[capacity];
		front = 0;
		rear = -1;
		size = 0;
	}
	
	// Method to add an element to the rear of the queue
    public void enqueue(String item) {
        if (isFull()) {
            System.out.println("Queue is full. Cannot enqueue.");
            return;
        }
        
        rear = (rear + 1) % capacity;
        queue[rear] = item;
        size++;
        System.out.println("Enqueued: " + item);
    }

    // Method to remove and return the front element of the queue
    public String dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            return "";
        }
        String item = queue[front];
        front = (front + 1) % capacity;
        size--;
        return item;
    }

    // Method to check if the queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Method to check if the queue is full
    public boolean isFull() {
        return size == capacity;
    }

    // Method to get the size of the queue
    public int size() {
        return size;
    }

    // Method to display the elements of the queue
    public void display() {
        System.out.println("Queue: " + Arrays.toString(queue));
    }

}
