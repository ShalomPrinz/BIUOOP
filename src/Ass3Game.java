/**
 * Main class for running assignment3 game.
 */
public class Ass3Game {
    /**
     * Runs the game.
     * @param args cmd input args
     */
    public static void main(String[] args) {
        Game game = new Game(800, 600);
        game.initialize();
        game.run();
    }
}
