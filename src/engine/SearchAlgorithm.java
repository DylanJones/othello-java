package engine;

public interface SearchAlgorithm {
    int findMove(State state);
    int findMove(State state, int depth);
}
