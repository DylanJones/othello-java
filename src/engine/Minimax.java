package engine;

import static engine.Color.*;

public class Minimax implements SearchAlgorithm {
    @Override
    public int findMove(State state, Color color) {
        // TODO iterative deepening?
        return findMove(state, color, 8);
    }

    @Override
    public int findMove(State state, Color color, int depth) {
        int bestMove = 0;
        int bestMmx = Integer.MIN_VALUE; // TODO figure out why this is so _bad_
        System.out.println("Minimax input color: " + color);
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
            System.out.println(s);
            System.out.println(s.heuristic(color));
            System.out.println(mmx);
        }
        return bestMove;
    }

    private int alphaBeta(State state, Color color, int alpha, int beta, int depth) {
//        System.out.println("\t".repeat(2-depth) + state.toString().replace("\n", "\n" + "\t".repeat(2-depth)));
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
            if (beta <= alpha) { // alpha-beta cutoff
                break;
            }
        }
        return best;
    }

    public static void main(String... args) {
//        State s = new State(Board.fromBoxBoard(new Color[]{
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, WHITE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, WHITE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, WHITE, BLACK, WHITE, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, WHITE, BLACK, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
//        }), WHITE);
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
        Minimax mm = new Minimax();
        s = s.makeMove(mm.findMove(s, BLACK));
        System.out.println("\n\n\n");
        System.out.println(s);
    }
}
