package engine;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Board implements Iterable<Color> {
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

    /* HEURISTIC FUNCTIONS */
    public int weightSquare(Color c) {
        int out = 0;
        for (int i = 0; i < 64; i++) {
            if ((blackMask >>> i & 1) == 1) {
                out += SQUARE_WEIGHTS[i];
            } else if ((whiteMask >>> i & 1) == 1) {
                out -= SQUARE_WEIGHTS[i];
            }
        }
        if (c == Color.WHITE)
            out *= -1;
        return out;
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

    @Override
    public void forEach(Consumer<? super Color> action) {
        for (Color c : this) {
            action.accept(c);
        }
    }

    @Override
    public Spliterator<Color> spliterator() {
        throw new RuntimeException("spliterator not implemented");
    }
}
