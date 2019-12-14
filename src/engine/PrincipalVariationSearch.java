package engine;

import static engine.Color.*;
import static engine.Color.BLACK;

public class PrincipalVariationSearch extends SearchAlgorithm {
    public PrincipalVariationSearch() {
    }

    public PrincipalVariationSearch(int maxDepth, long milliseconds) {
        this.maxDepth = maxDepth;
        this.milliseconds = milliseconds;
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
            int mmx = -pvs(s, color.invert(), Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
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

    private int pvs(State state, Color color, int α, int β, int depth) {
        if (state.movingColor == EMPTY || depth == 0) {
            return state.heuristic(color);
        }
        boolean isFirst = true;
        for (State child : state.getChildren()) {
            int score;
            if (isFirst) {
                isFirst = false;
                score = -pvs(child, color.invert(), -β, -α, depth - 1);
            } else {
                score = -pvs(child, color.invert(), -α - 1, -α, depth - 1);
                if (α < score && score < β) {
                    score = -pvs(child, color.invert(), -β, -score, depth - 1);
                }
            }
            α = Math.max(α, score);
            if (α >= β) { // α-β
                break;
            }
        }
        return α;

    }

    public static void main(String[] args) {
        State s = new State(Board.fromBoxBoard(new Color[]{
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                WHITE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, WHITE, EMPTY, EMPTY, EMPTY, WHITE, EMPTY, EMPTY,
                EMPTY, BLACK, WHITE, WHITE, WHITE, WHITE, EMPTY, EMPTY,
                EMPTY, BLACK, EMPTY, BLACK, WHITE, WHITE, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, BLACK, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        }), BLACK);

        SearchAlgorithm mm = new Minimax(8, 5000, true);
        SearchAlgorithm pvs = new PrincipalVariationSearch(8, 5000);

        System.out.println(mm.findMove(s, BLACK));
        System.out.println(pvs.findMove(s, BLACK));
    }
}
