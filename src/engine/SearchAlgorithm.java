package engine;

public interface SearchAlgorithm {
    int findMove(State state, Color color);
    int findMove(State state, Color color, int depth);
}
