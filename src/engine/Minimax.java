package engine;

import static engine.Color.*;

public class Minimax extends SearchAlgorithm {

    public Minimax() {
        this(15, 2000, true);
    }

    public Minimax(int maxDepth, long milliseconds, boolean alphaBeta) {
        this.maxDepth = maxDepth;
        this.milliseconds = milliseconds;
        this.alphaBeta = alphaBeta;
    }

    @Override
    public int findMove(State state, Color color, int depth) {
        int bestMove = 0;
        int bestMmx = Integer.MIN_VALUE;
        for (int i = 0; i < 64; i++) {
            if ((1L << i & state.moveMask) == 0) {
                continue;
            }
            State s = state.makeMove(i);
            int mmx = alphaBeta(s, color, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
            if (mmx > bestMmx) {
                bestMmx = mmx;
                bestMove = i;
            }
            if (System.currentTimeMillis() > cutoffTime) {
                return -1;
            }
        }
        return bestMove;
    }

    public boolean getAlphaBeta() {
        return alphaBeta;
    }

    public void setAlphaBeta(boolean alphaBeta) {
        this.alphaBeta = alphaBeta;
    }

    /**
     * Using alpha-beta or minimax, recursively search for the optimal move for the given player.
     * Return value is an indication of how "good" each move is, with higher values being better for the
     * player and lower values being worse.
     * @return the minimax value
     */
    private int alphaBeta(State state, Color color, int alpha, int beta, int depth) {
        if (state.movingColor == EMPTY || depth == 0) {
            return state.heuristic(color);
        }
        int best = state.movingColor == color ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (var child : state.getChildren()) {
            int mmx = alphaBeta(child, color, alpha, beta, depth - 1);
            if (state.movingColor == color) {
                best = Math.max(best, mmx);
                alpha = Math.max(best, alpha);
            } else {
                best = Math.min(best, mmx);
                beta = Math.min(best, beta);
            }
            if (alpha >= beta && alphaBeta) { // alpha-beta cutoff
                break;
            }
        }
        return best;
    }

    public static void main(String... args) {
//        State s = new State(Board.fromBoxBoard(new Color[]{
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                WHITE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, WHITE, EMPTY, EMPTY, EMPTY, WHITE, EMPTY, EMPTY,
//                EMPTY, BLACK, WHITE, WHITE, WHITE, WHITE, EMPTY, EMPTY,
//                EMPTY, BLACK, EMPTY, BLACK, WHITE, WHITE, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, BLACK, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//        }), BLACK);
        State s = new State(Board.fromBoxBoard(new Color[]{
                EMPTY, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, EMPTY,
                EMPTY, EMPTY, BLACK, BLACK, WHITE, BLACK, EMPTY, EMPTY,
                BLACK, BLACK, BLACK, BLACK, BLACK, WHITE, BLACK, BLACK,
                BLACK, BLACK, WHITE, BLACK, BLACK, BLACK, BLACK, BLACK,
                BLACK, WHITE, BLACK, WHITE, BLACK, BLACK, BLACK, BLACK,
                BLACK, BLACK, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK,
                EMPTY, EMPTY, EMPTY, WHITE, BLACK, BLACK, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, WHITE, WHITE, WHITE, WHITE, EMPTY
        }), BLACK);
        Minimax mm = new Minimax();
        s = s.makeMove(mm.findMove(s, BLACK));
        System.out.println("\n\n\n");
        System.out.println(s);
    }
}
