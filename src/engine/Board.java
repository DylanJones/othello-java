package engine;

public class Board {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    // Bitboard representation - the only fields on this object should be black and white bitmasks
    public long black;
    public long white;

    public Board() {
    }

    public Board(long black, long white) {
        this.black = black;
        this.white = white;
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
     * Convert an array board into a bitboard.
     * @return the newly created bitboard
     */
    public static Board toBitBoard(int[] board) {
        long black = 0;
        long white = 0;
        for (int i = 0; i < 64; i++) {
            if (board[i] == WHITE) {
                white |= 1 << i;
            } else if (board[i] == BLACK) {
                black |= 1 << i;
            }
        }
        return new Board(black, white);
    }
}
