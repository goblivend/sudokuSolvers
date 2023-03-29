package fr.ivan.sudoku;

import fr.ivan.sudoku.backtracking.IntCell;
import fr.ivan.sudoku.util.Cell;
import fr.ivan.sudoku.util.Utils;

public abstract class Sudoku<T extends Cell> {

    protected T[][] _grid;
    protected Integer _size;
    protected Integer _lineSize;

    protected abstract T initT(char c);

    protected abstract void initGrid();

    public void SetGrid(String grid, int size) {
        _size = size;
        _lineSize = _size*_size;
        initGrid();
        int nbElt = _lineSize*_lineSize;
        if (grid.length() != nbElt)
            throw new RuntimeException(this.getClass().getName() + ".SetGrid(): Invalid grid not containing 81 cols: " + grid.length() + " and should be : " + nbElt);

        for (int i = 0; i < grid.length(); i++) {
            char c = grid.charAt(i);
            _grid[i / _lineSize][ i % _lineSize] = initT(c);
        }
    }

    private String GetDelimiter()
    {
        return "-".repeat((_lineSize + _size) * 2 + 1);
    }

    public void PrintGrid() {
        for (int y = 0; y < _lineSize; y++) {
            if (y % _size == 0)
                System.out.println(GetDelimiter());

            for (int x = 0; x < _lineSize; x++) {
                if (x % _size == 0)
                    System.out.print("| ");
                System.out.print(_grid[y][x] + " ");
            }
            System.out.println("|");
        }
        System.out.println(GetDelimiter());
    }
    public abstract void Solve();

    @Override
    public String toString() {
        if (_grid == null)
            return "null";

        String res = "";

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                res += Utils.IntToChar(((Cell)_grid[y][x]).getValue());
            }
        }
        return res;
    }
}
