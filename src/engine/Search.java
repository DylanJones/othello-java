package engine;

public interface Search {
    // In the engine, we are representing boards with bitboard pairs (for efficiency).
    long findMove(long black, long white);
}
