package game;

/**
 * Simple counter.
 */
public class Counter {
    private int count;

    /**
     * Constructs a counter.
     */
    public Counter() {
        this.count = 0;
    }

    /**
     * Add number to current count.
     * @param number number to add
     */
    public void increase(int number) {
        this.count += number;
    }

    /**
     * Subtract number from current count.
     * @param number number to subtract
     */
    public void decrease(int number) {
        this.count -= number;
    }

    /**
     *
     * @return current counter value
     */
    public int getValue() {
        return this.count;
    }
}
