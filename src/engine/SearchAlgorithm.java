package engine;

/**
 * An abstract class that represents a search algorithm.  The findMove(State, Color) method uses iterative deepening
 * to find the best move that it can within the object's given limits.
 */
public abstract class SearchAlgorithm {
    protected int maxDepth;
    protected long milliseconds;
    protected boolean alphaBeta;

    protected long cutoffTime;

    /**
     * Use iterative deepening to find the best move for the given color.
     * @param state the board state to start the search from
     * @param color the color to maximize the score of
     * @return the integer index of the best move
     */
    public int findMove(State state, Color color) {
        setCutoffTime(System.currentTimeMillis() + getMilliseconds());
        int depth = 1;
        int move = -1;
        while (System.currentTimeMillis() < getCutoffTime() && depth < getMaxDepth()) {
//            System.out.println("t: " + (milliseconds - (cutoffTime - System.currentTimeMillis())));
//            System.out.println("d: " + (depth));
            int tmpMove = findMove(state, color, depth++);
            if (tmpMove != -1) {
                move = tmpMove;
            }
        }
        return move;
    }

    /**
     * Recursively find the best move for the given color from the given board state, searching up to a maximum depth of
     * `depth` nodes into the future.
     * @param state the board state to start the search from
     * @param color the color to maximize the score of
     * @param depth the depth to search to
     * @return the integer index of the best move
     */
    public abstract int findMove(State state, Color color, int depth);


    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        if (maxDepth < 1)
            maxDepth = 1;
        this.maxDepth = maxDepth;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long ms) {
        milliseconds = ms;
    }

    public long getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(long epoch) {
        cutoffTime = epoch;
    }
}
