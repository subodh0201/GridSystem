package grid;

/**
 * interface Event represents processable events
 * that occurred during the execution of the system.
 * The events can be processed by calling consume()
 */
public interface Event {
    public abstract void consume();
}
