package engine;

import static engine.Color.*;

public class Minimax implements SearchAlgorithm {
    protected int maxDepth;
    protected long milliseconds;
    protected boolean alphaBeta;

    protected long cutoffTime;

    public Minimax() {
        this(15, 2000, true);
    }

    public Minimax(int maxDepth, long milliseconds, boolean alphaBeta) {
        this.maxDepth = maxDepth;
        this.milliseconds = milliseconds;
        this.alphaBeta = alphaBeta;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        if (maxDepth < 1)
            maxDepth = 1;
        this.maxDepth = maxDepth;
    }

    public boolean getAlphaBeta() {
        return alphaBeta;
    }

    public void setAlphaBeta(boolean alphaBeta) {
        this.alphaBeta = alphaBeta;
    }

    @Override
    public int findMove(State state, Color color) {
        cutoffTime = System.currentTimeMillis() + this.milliseconds;
        int depth = 1;
        int move = -1;
        while (System.currentTimeMillis() < cutoffTime && depth < this.maxDepth) {
//            System.out.println("t: " + (milliseconds - (cutoffTime - System.currentTimeMillis())));
//            System.out.println("d: " + (depth));
            int tmpMove = findMove(state, color, depth++);
            if (tmpMove != -1) {
                move = tmpMove;
            }
        }
        return move;
    }

    @Override
    public int findMove(State state, Color color, int depth) {
        int bestMove = 0;
        int bestMmx = Integer.MIN_VALUE; // TODO figure out why this is so _bad_
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
