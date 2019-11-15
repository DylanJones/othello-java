package engine;

import java.io.Serializable;

public class State implements Serializable {
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
    public final long ourMask;
    public final long theirMask;
    public int movingColor;
    public long moveMask;

    public State(long ourMask, long theirMask, int lastPlayer) {
        this.ourMask = ourMask;
        this.theirMask = theirMask;
        this.movingColor = invert(lastPlayer);
        moveMask = calcLegalMoves(movingColor);
        if (moveMask == 0) {
            movingColor = lastPlayer;
            moveMask = calcLegalMoves(movingColor);
            if (moveMask == 0) {
                movingColor = EMPTY;
            }
        }
    }


    /**
     * Convert a bitboard pair into a boxed, array representation.
     *
     * @return the array representation of the board
     */
    public int[] toBoxBoard() {
        int[] board = new int[64];
        for (int i = 0; i < 64; i++) {
            if ((ourMask >>> i & 1) == 1) {
                board[i] = movingColor;
            } else if ((theirMask >>> i & 1) == 1) {
                board[i] = invert(movingColor);
            } else {
                board[i] = EMPTY;
            }
        }
        return board;
    }


    /**
     * Get the locations of all legal moves for the given player from this board.
     *
     * @return a bitmask with valid moves as ones.
     */
    public long calcLegalMoves(int player) {
        return (getMovesPartial(ourMask, theirMask, 1) |  // horizontal
                getMovesPartial(ourMask, theirMask, 8) | // vertical
                getMovesPartial(ourMask, theirMask, 7) | // diagonal like /
                getMovesPartial(ourMask, theirMask, 9)) & // diagonal like \
                ~(ourMask | theirMask); // mask out to only the empty squares
    }

    /**
     * Assuming p1 is moving, smear p1 in the specified direction and find legal moves.
     * This method uses the naive "sequential algorithm" because I can actually understand it.
     * Based on Edax's implementation here https://github.com/abulmo/edax-reversi/blob/master/src/board.c#L571
     */
    private long getMovesPartial(long p1, long mask, int dir) {
        long flip;
        flip = ((p1 << dir | p1 >>> dir) & mask); // initialize w/ one smear
        // manually unrolled loop bc "efficiency"
        flip |= (((flip << dir) | (flip >>> dir)) & mask); // do another 5 smears here
        flip |= (((flip << dir) | (flip >>> dir)) & mask);
        flip |= (((flip << dir) | (flip >>> dir)) & mask);
        flip |= (((flip << dir) | (flip >>> dir)) & mask);
        flip |= (((flip << dir) | (flip >>> dir)) & mask);
        return (flip << dir) | (flip >>> dir); // final smear isn't masked to allow it to go outside the bounds
    }

    /* HEURISTIC FUNCTIONS */
    public int weightSquare() {
        int out = 0;
        for (int i = 0; i < 64; i++) {
            if ((ourMask << i & 1) == 1) {
                out += SQUARE_WEIGHTS[i];
            } else if ((theirMask << i & 1) == 1) {
                out -= SQUARE_WEIGHTS[i];
            }
        }
        return out;
    }


    // util methods

    /**
     * @return the inverse of the given color.
     */
    public static int invert(int color) {
        return color == BLACK ? WHITE : BLACK;
    }

    public State[] getChildren() {
        State[] c = new State[Long.bitCount(moveMask)];
        long moves = moveMask;
        for (int i = 0; moves != 0; ) {
            while ((moves & 1) == 0) {
                moves >>>= 1;
                i++;
            }
            moves >>>= 1;
            // i is the index of the move
            long piece = 1L << i;
            long toFlip = (getMovesPartial(piece, theirMask, 1) |  // horizontal
                    getMovesPartial(piece, theirMask, 8) | // vertical
                    getMovesPartial(piece, theirMask, 7) | // diagonal like /
                    getMovesPartial(piece, theirMask, 9)) & // diagonal like \
                    (piece | theirMask); // mask out to only the other player's squares and our move
            long them = theirMask & ~toFlip;
            long us = ourMask | toFlip;

        }
        if (true) {
            throw new RuntimeException();
        }
        return c;
    }

    /**
     * Create a new board from an array board, automatically converting it to a bitboard.
     */
    public static State fromBoxBoard(int[] intBoard, int lastPlayer) {
        long black = 0;
        long white = 0;
        for (int i = 0; i < 64; i++) {
            if (intBoard[i] == WHITE) {
                white |= 1L << i;
            } else if (intBoard[i] == BLACK) {
                black |= 1L << i;
            }
        }
        return new State(lastPlayer == BLACK ? black : white, lastPlayer == BLACK ? white : black, lastPlayer);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\033[4m"); // underline
        int[] board = toBoxBoard();
        for (int r = 0; r < 8; r++) {
            s.append("|");
            for (int c = 0; c < 8; c++) {
                s.append(board[r * 8 + c] == EMPTY ? " " : board[r * 8 + c] == BLACK ? "@" : "O").append("|");
            }
            s.append("\n");
        }
        s.append("\033[0m"); // reset
        return s.toString();
    }

    // TESTING ONLY
    public static void main(String... args) {
        // starting board
        State s = new State(0b00000000000000000001000000001000000000000000000000000000L, 0b00000000000000000000100000010000000000000000000000000000L, BLACK);
//        System.out.println(s);
//        System.out.println(Long.toBinaryString(s.black));
        s = State.fromBoxBoard(new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 1, 2, 2, 2, 2,
                0, 0, 0, 2, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
        }, BLACK);
        System.out.println(s);
        System.out.println();
        System.out.println(new State(s.calcLegalMoves(BLACK), 0, WHITE));
//        System.out.println();
//        System.out.println(Arrays.toString(s.getChildren()));
    }
}
