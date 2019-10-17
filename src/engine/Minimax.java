package engine;

public class Minimax implements SearchAlgorithm {
    @Override
    public int findMove(State state) {
        // TODO iterative deepening?
        return findMove(state, 5);
    }

    @Override
    public int findMove(State state, int depth) {

        return 0;
    }
}
