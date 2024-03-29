package engine;

import java.util.Iterator;

/**
 * The engine.Board class represents the abstract concept of an Othello board.
 * The board is stored as two 64-bit bitmasks - one for black, one for white.  The board
 * also contains
 */
public class Board implements Iterable<Color> {
    // Bitboard representation - the only fields on this object should be black and white bitmasks and the moving player
    public final long blackMask;
    public final long whiteMask;

    public Board(long blackMask, long whiteMask) {
        this.blackMask = blackMask;
        this.whiteMask = whiteMask;
    }

    /**
     * Convert a bitboard pair into a boxed, array representation.
     *
     * @return the array representation of the board
     */
    public Color[] toBoxBoard() {
        Color[] board = new Color[64];
        for (int i = 0; i < 64; i++) {
            if (((blackMask >>> i) & 1) == 1) {
                board[i] = Color.BLACK;
            } else if (((whiteMask >>> i) & 1) == 1) {
                board[i] = Color.WHITE;
            } else {
                board[i] = Color.EMPTY;
            }
        }
        return board;
    }


    /**
     * Create a new board from an array board, automatically converting it to a bitboard.
     */
    public static Board fromBoxBoard(Color[] boxBoard) {
        long black = 0;
        long white = 0;
        for (int i = 0; i < 64; i++) {
            if (boxBoard[i] == Color.WHITE) {
                white |= 1L << i;
            } else if (boxBoard[i] == Color.BLACK) {
                black |= 1L << i;
            }
        }
        return new Board(black, white);
    }


    @Override
    public Iterator<Color> iterator() {
        return new Iterator<Color>() {
            private int idx;

            @Override
            public boolean hasNext() {
                return idx != 64;
            }

            @Override
            public Color next() {
                if ((blackMask >>> idx & 1) == 1) {
                    idx++;
                    return Color.BLACK;
                } else if ((whiteMask >>> idx & 1) == 1) {
                    idx++;
                    return Color.WHITE;
                } else {
                    idx++;
                    return Color.EMPTY;
                }
            }
        };
    }
}
