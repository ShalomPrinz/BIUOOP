
/**
 * Draws multiple balls and animates their movement.
 */
public class Main {
    /**
     *
     * @param args cmd input args
     */
    public static void main(String[] args) {
        Game game = new Game(800, 600);
        game.initialize();
        game.run();
    }
}