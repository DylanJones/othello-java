package engine;

import java.io.Serializable;
import java.util.Arrays;

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

    public static final byte EMPTY = 0;
    public static final byte BLACK = 1;
    public static final byte WHITE = 2;

    // Bitboard representation - the only fields on this object should be black and white bitmasks and the moving player
    public long blackMask;
    public long whiteMask;
    public byte movingColor;
    public long moveMask;

    public State(long blackMask, long whiteMask, byte lastPlayer) {
        this.blackMask = blackMask;
        this.whiteMask = whiteMask;
        this.movingColor = invert(lastPlayer);
        moveMask = calcLegalMoves();
        if (moveMask == 0) {
            movingColor = lastPlayer;
            moveMask = calcLegalMoves();
            if (moveMask == 0) {
                movingColor = 0;
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
            if (((blackMask >>> i) & 1) == 1) {
                board[i] = BLACK;
            } else if (((whiteMask >>> i) & 1) == 1) {
                board[i] = WHITE;
            } else {
                board[i] = EMPTY;
            }
        }
        return board;
    }

    public long getOurMask() {
        return movingColor == BLACK ? blackMask : whiteMask;
    }

    public long getTheirMask() {
        return movingColor == BLACK ? whiteMask : blackMask;
    }


    /**
     * Get the locations of all legal moves for the given player from this board.
     *
     * @return a bitmask with valid moves as ones.
     */
    public long calcLegalMoves() {
        long us = getOurMask();
        long them = getTheirMask();
        return (getMovesPartial(us, them & 0x7E7E7E7E7E7E7E7EL, 1) | // horizontal
                getMovesPartial(us, them & 0x00FFFFFFFFFFFF00L, 8) | // vertical
                getMovesPartial(us, them & 0x007E7E7E7E7E7E00L, 7) | // diagonal like /
                getMovesPartial(us, them & 0x007E7E7E7E7E7E00L, 9)) & // diagonal like \
                ~(us | them); // mask out to only the empty squares
    }

    /**
     * Assuming p1 is moving, smear p1 in the specified direction and find legal moves.
     * This method uses the naive "sequential algorithm" because I can actually understand it.
     * Based on Edax's implementation here https://github.com/abulmo/edax-reversi/blob/master/src/board.c#L571
     */
    private long getMovesPartial(long p1, long mask, int dir) {
        long flip = ((p1 << dir) | (p1 >>> dir)) & mask;
        for (int i = 0; i < 6; i++) {
            flip |= (((flip << dir) | (flip >>> dir)) & mask); // smear 6 times _inside_ the bounds
        }
        return (flip << dir) | (flip >>> dir); // final smear isn't masked to allow it to go outside the bounds
    }

    /**
     * Same as getMovesPartial, but only does left shifts.
     *
     * @param p1   the piece to start at
     * @param mask the opponent's pieces
     * @param dir  the direction to fill in
     * @return the flooded board
     */
    private long getMovesPartialLeft(long p1, long mask, int dir) {
        long flip = p1 << dir & mask;
        for (int i = 0; i < 6; i++) {
            flip |= flip << dir & mask; // smear 6 times _inside_ the bounds
        }
        return flip | flip << dir; // final smear isn't masked to allow it to go outside the bounds
    }

    /**
     * Same as getMovesPartial, but only does right shifts.
     *
     * @param p1   the piece to start at
     * @param mask the opponent's pieces
     * @param dir  the direction to fill in
     * @return the flooded board
     */
    private long getMovesPartialRight(long p1, long mask, int dir) {
        long flip = p1 >>> dir & mask;
        for (int i = 0; i < 6; i++) {
            flip |= flip >>> dir & mask; // smear 6 times _inside_ the bounds
        }
        return flip | flip >>> dir; // final smear isn't masked to allow it to go outside the bounds
    }

    /* HEURISTIC FUNCTIONS */
    public int weightSquare() {
        int out = 0;
        long us = getOurMask();
        long them = getTheirMask();
        for (int i = 0; i < 64; i++) {
            if ((us << i & 1) == 1) {
                out += SQUARE_WEIGHTS[i];
            } else if ((them << i & 1) == 1) {
                out -= SQUARE_WEIGHTS[i];
            }
        }
        return out;
    }

    // util methods

    /**
     * @return the inverse of the given color.
     */
    public static byte invert(byte color) {
        return color == BLACK ? WHITE : BLACK;
    }

    /**
     * Get all successors of the current node.
     *
     * @return an array with all children of this node.
     */
    public State[] getChildren() {
        State[] c = new State[Long.bitCount(moveMask)];
        long moves = moveMask;
        int moveIdx = 0;
        for (int i = 0; moves != 0; i++) {
            while ((moves & 1) == 0) {
                moves >>>= 1;
                moveIdx++;
            }
            moves >>>= 1;
            c[i] = makeMove(moveIdx);
        }
        if (true) {
            throw new RuntimeException();
        }
        return c;
    }

    public State makeMove(int idx) {
        long piece = 1L << idx;
        long flipped = piece;
        long us = getOurMask();
        long them = getTheirMask();
        System.out.println(this);
        printLong(getTheirMask());
        printLong(blackMask);
        // figure out what flips when we make a move
        {
            // diagonal up left
            long tmp = getMovesPartialRight(piece, them, 9);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // up
            tmp = getMovesPartialRight(piece, them, 8);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // diagonal up right
            tmp = getMovesPartialRight(piece, them, 7);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // left
            tmp = getMovesPartialRight(piece, them, 1);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // right
            tmp = getMovesPartialLeft(piece, them, 1);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // diagonal down left
            tmp = getMovesPartialLeft(piece, them, 7);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // down
            System.out.println("B R U H");
            tmp = getMovesPartialLeft(piece, them, 8);
            System.out.println("B R U H");
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // diagonal down right
            tmp = getMovesPartialLeft(piece, them, 9);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
        }

        // actually do the flips to their pieces
        them = them & ~flipped;
        us = getOurMask() | flipped;
        return new State(movingColor == BLACK ? us : them, movingColor == WHITE ? us : them, movingColor);
    }

    /**
     * Create a new board from an array board, automatically converting it to a bitboard.
     */
    public static State fromBoxBoard(int[] intBoard, byte lastPlayer) {
        long black = 0;
        long white = 0;
        for (int i = 0; i < 64; i++) {
            if (intBoard[i] == WHITE) {
                white |= 1L << i;
            } else if (intBoard[i] == BLACK) {
                black |= 1L << i;
            }
        }
        return new State(black, white, lastPlayer);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\033[4m"); // underline
        int[] board = toBoxBoard();
        for (int r = 0; r < 8; r++) {
            s.append("|");
            for (int c = 0; c < 8; c++) {
                switch (board[r * 8 + c]) {
                    case EMPTY:
                        s.append(" ").append("|");
                        break;
                    case BLACK:
                        s.append("@").append("|");
                        break;
                    case WHITE:
                        s.append("O").append("|");
                        break;
                }
            }
            s.append("\n");
        }
        s.append("\033[0m"); // reset
        return s.toString();
    }

    // TESTING ONLY

    private static void printLong(long l) {
        State s = new State(l, l, WHITE);
        s.movingColor = BLACK;
        System.out.println(l);
//        System.out.println(Arrays.toString(s.toBoxBoard()));
        System.out.println(s);
        System.out.println();
    }

    public static void main(String... args) {
        // starting board
        State s = new State(0b00000000000000000001000000001000000000000000000000000000L, 0b00000000000000000000100000010000000000000000000000000000L, BLACK);
//        s.blackMask = 0b00000000000000000001000000001000000000000000000000000000L;
//        s.whiteMask = 0b00000000000000000000100000010000000000000000000000000000L;
//        s.movingColor = BLACK;
//        System.out.println(s);
//        System.out.println(Long.toBinaryString(s.black));
        s = State.fromBoxBoard(new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 1, 2, 0, 0, 0,
                0, 0, 0, 2, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
        }, WHITE);
        System.out.println(s);
        System.out.println();
        System.out.println(s.makeMove(20));
//        printLong(1L << 9);
//        printLong(s.getMovesPartialLeft(1L << 9, 0xFFFFFFFFFFFFFFFL, 7));
    }
}
