package engine;

public enum Color {
    EMPTY, BLACK, WHITE;

    /**
     * @return the inverse of the given color.
     */
    public Color invert() {
        if (this == EMPTY) return EMPTY;
        return this == BLACK ? WHITE : BLACK;
    }
}
