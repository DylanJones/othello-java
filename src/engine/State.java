package engine;

public class State {
    // Weight matrix for a basic heuristic function.
    private final static int[] SQUARE_WEIGHTS = new int[]{
            120, -20, 20, 5, 5, 20, -20, 120,
            -20, -40, -5, -5, -5, -5, -40, -20,
            20, -5, 15, 3, 3, 15, -5, 20,
            5, -5, 3, 3, 3, 3, -5, 5,
            5, -5, 3, 3, 3, 3, -5, 5,
            20, -5, 15, 3, 3, 15, -5, 20,
            -20, -40, -5, -5, -5, -5, -40, -20,
            120, -20, 20, 5, 5, 20, -20, 120
    };

    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    // Bitboard representation - the only fields on this object should be black and white bitmasks and the moving player
    public final long black;
    public final long white;
    public final int movingPlayer;

    public State(long black, long white, int movingPlayer) {
        this.black = black;
        this.white = white;
        this.movingPlayer = movingPlayer;
    }

    /**
     * Create a new board from an array board, automatically converting it to a bitboard.
     */
    public State(int[] intBoard, int movingPlayer) {
        long black = 0;
        long white = 0;
        for (int i = 0; i < 64; i++) {
            if (intBoard[i] == WHITE) {
                white |= 1 << i;
            } else if (intBoard[i] == BLACK) {
                black |= 1 << i;
            }
        }
        this.black = black;
        this.white = white;
        this.movingPlayer = movingPlayer;
    }

    /**
     * Convert a bitboard pair into a boxed, array representation.
     *
     * @return the array representation of the board
     */
    public int[] toBoxBoard() {

        int[] board = new int[64];
        for (int i = 0; i < 64; i++) {
            if ((black << i & 1) == 1) {
                board[i] = BLACK;
            } else if ((white << i & 1) == 1) {
                board[i] = WHITE;
            } else {
                board[i] = EMPTY;
            }
        }
        return board;
    }


    /**
     * Get the locations of all legal moves from the current state.
     */
    public long legalMoves() {
        return 0;
    }

    /* HEURISTIC FUNCTIONS */
    public int weightSquare() {
        int out = 0;
        // First, calculate it from black's perspective
        for (int i = 0; i < 64; i++) {
            if ((black << i & 1) == 1) {
                out += SQUARE_WEIGHTS[i];
            } else if ((white << i & 1) == 1) {
                out -= SQUARE_WEIGHTS[i];
            }
        }
        // Then, flip it around if we're white
        if (movingPlayer == WHITE) {
            out *= -1;
        }
        return out;
    }
}
