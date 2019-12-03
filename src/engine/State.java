package engine;

import java.io.Serializable;

import static engine.Color.*;

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
    public final Board board;
    public Color movingColor;
    public long moveMask;

    public State(long blackMask, long whiteMask, Color movingPlayer) {
        this(new Board(blackMask, whiteMask), movingPlayer);
    }

    public State(Board board, Color movingPlayer) {
        this.board = board;
        this.movingColor = movingPlayer;
        this.moveMask = calcLegalMoves();
    }


    public long getOurMask() {
        return movingColor == BLACK ? board.blackMask : board.whiteMask;
    }

    public long getTheirMask() {
        return movingColor == BLACK ? board.whiteMask : board.blackMask;
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
    public int heuristic(Color c) {
        if (movingColor == EMPTY) {
            return countColor(c) > 0 ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1;
        }
        return weightSquare(c);
    }

    public int countColor(Color c) {
        int x = Long.bitCount(board.blackMask) - Long.bitCount(board.whiteMask);
        if (c == WHITE) x *= -1;
        return x;
    }

    public int weightSquare(Color c) {
        int out = 0;
        for (int i = 0; i < 64; i++) {
            if ((board.blackMask << i & 1) == 1) {
                out += SQUARE_WEIGHTS[i];
            } else if ((board.whiteMask >>> i & 1) == 1) {
                out -= SQUARE_WEIGHTS[i];
            }
        }
        if (c == Color.WHITE)
            out *= -1;
        return out;
    }

    // util methods

    /**
     * Check if the given move is legal on this board state.
     *
     * @param move the index of the move
     * @return if the move is legal
     */
    public boolean isLegalMove(int move) {
        if (move < 0 || move > 64) return false;
        return (moveMask >>> move & 1) == 1;
    }

    /**
     * Get all successors of the current node.
     *
     * @return an array with all children of this node.
     */
    public State[] getChildren() {
        State[] c = new State[Long.bitCount(moveMask)];
        int arrIdx = 0;
        for (int i = 0; i < 64; i++) {
            if ((moveMask & 1L << i) == 0) {
                continue;
            }
            c[arrIdx++] = makeMove(i);
        }
        return c;
    }

    public State makeMove(int idx) {
        long piece = 1L << idx;
        long flipped = piece;
        long us = getOurMask();
        long them = getTheirMask();
        if ((piece & moveMask) == 0) {
            throw new RuntimeException("UR BAD");
        }
        // figure out what flips when we make a move
        {
            // diagonal up left
            long tmp = getMovesPartialRight(piece, them & 0x007E7E7E7E7E7E00L, 9);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // up
            tmp = getMovesPartialRight(piece, them & 0x00FFFFFFFFFFFF00L, 8);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // diagonal up right
            tmp = getMovesPartialRight(piece, them & 0x007E7E7E7E7E7E00L, 7);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // left
            tmp = getMovesPartialRight(piece, them & 0x7E7E7E7E7E7E7E7EL, 1);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // right
            tmp = getMovesPartialLeft(piece, them & 0x7E7E7E7E7E7E7E7EL, 1);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // diagonal down left
            tmp = getMovesPartialLeft(piece, them & 0x007E7E7E7E7E7E00L, 7);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // down
            tmp = getMovesPartialLeft(piece, them & 0x00FFFFFFFFFFFF00L, 8);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
            // diagonal down right
            tmp = getMovesPartialLeft(piece, them & 0x007E7E7E7E7E7E00L, 9);
            if ((tmp & us) != 0) {
                flipped |= tmp;
            }
        }

        // actually do the flips to their pieces
        them = getTheirMask() & ~flipped;
        us = getOurMask() | flipped;

        // figure out who's moving next
        State s = new State(movingColor == BLACK ? us : them, movingColor == WHITE ? us : them, movingColor.invert());
        if (s.moveMask == 0) {
            s.movingColor = s.movingColor.invert();
            s.moveMask = s.calcLegalMoves();
            if (s.moveMask == 0) {
                s.movingColor = EMPTY;
            }
        }
        return s;
    }


    public static State getStartingState() {
        return new State(0b00000000000000000001000000001000000000000000000000000000L, 0b00000000000000000000100000010000000000000000000000000000L, WHITE);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Moving: ").append(movingColor).append('\n');
        s.append("\033[4m"); // underline
        Color[] boxBoard = board.toBoxBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                s.append('│');
                switch (boxBoard[r * 8 + c]) {
                    case EMPTY:
                        if ((moveMask >>> (r * 8 + c) & 1) == 1) {
                            s.append('.');
                        } else {
                            s.append(' ');
                        }
                        break;
                    case BLACK:
                        s.append('@');
                        break;
                    case WHITE:
                        s.append('O');
                        break;
                }
            }
            s.append('│');
            s.append("\n");
        }
        s.append("\033[0m"); // reset
        return s.toString();
    }


    public static void main(String... args) {
        // starting board
        State s = new State(0b00000000000000000001000000001000000000000000000000000000L, 0b00000000000000000000100000010000000000000000000000000000L, BLACK);
        s = new State(Board.fromBoxBoard(new Color[]{
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, BLACK, WHITE, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, BLACK, WHITE, WHITE, WHITE, EMPTY, EMPTY,
                EMPTY, EMPTY, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK,
                BLACK, BLACK, BLACK, WHITE, BLACK, WHITE, EMPTY, EMPTY,
                WHITE, WHITE, WHITE, WHITE, WHITE, BLACK, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, BLACK, WHITE, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, WHITE, BLACK, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        }), WHITE);
        System.out.println(s);
        System.out.println();
        System.out.println(s.makeMove(38));
//        printLong(1L << 9);
//        printLong(s.getMovesPartialLeft(1L << 9, 0xFFFFFFFFFFFFFFFL, 7));
    }
}
