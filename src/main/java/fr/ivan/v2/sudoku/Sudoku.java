package fr.ivan.v2.sudoku;

import fr.ivan.profiler.Profiler;

public interface Sudoku {
    void solve();

    void setGrid(String grid, String alphabet, int size);
    void setGrid(String grid, String alphabet, int size, Profiler profiler);
    String toString(String alphabet);
    String toString(boolean pretty);
    String toString(boolean pretty, String alphabet);
}
