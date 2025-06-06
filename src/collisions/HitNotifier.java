package collisions;

/**
 * Represents a hit notifier.
 */
public interface HitNotifier {

    /**
     * Adds given hit listener to notifier's listeners.
     * @param hl hit listener
     */
    void addHitListener(HitListener hl);

    /**
     * Removes given hit listener from notifier's listeners.
     * @param hl hit listener
     */
    void removeHitListener(HitListener hl);
}
