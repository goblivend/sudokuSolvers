package fr.ivan.v1.sudoku;

import fr.ivan.util.Cell;
import fr.ivan.util.Utils;
import fr.ivan.profiler.*;

public abstract class Sudoku<T extends Cell> {

    protected T[][] _grid;
    protected String _alphabet;
    /**
     * Help for pretty printing
     */
    protected Integer _size;
    protected Integer _lineSize;
    protected Profiler _profiler;

    protected abstract T initT(char c);

    protected abstract void initGrid();

    public void SetGrid(String grid, String alphabet, int size) {
        SetGrid(grid, alphabet, size, null);
    }

    public void setAlphabet(String alphabet) {
        _alphabet = alphabet;
    }

    public void SetGrid(String grid, String alphabet, int size, Profiler profiler) {
        _profiler = profiler;
        if (_profiler != null)
            _profiler.start("Sudoku.SetGrid");
        _alphabet = alphabet;
        _size = size;
        _lineSize = _size * _size;
        initGrid();
        int nbElt = _lineSize * _lineSize;
        if (grid.length() != nbElt)
            throw new RuntimeException(this.getClass().getName() + ".SetGrid(): Invalid grid not containing the good number of cols: " + grid.length() + " and should be : " + nbElt);

        if (alphabet.length() != _lineSize+1)
            throw new RuntimeException(this.getClass().getName() + ".SetGrid(): Invalid alphabet not containing enough chars: " + alphabet.length() + " and should be : " + _lineSize + " + 1 for blank");

        for (int i = 0; i < grid.length(); i++) {
            char c = grid.charAt(i);
            _grid[i / _lineSize][i % _lineSize] = initT(c);
        }
        if (_profiler != null)
            _profiler.finish("Sudoku.SetGrid");
    }

    private String GetDelimiter() {
        return "-".repeat((_lineSize + _size) * 2 + 1);
    }

    public void PrintGrid() {
        if (_profiler != null)
            _profiler.start("Sudoku.PrintGrid");
        for (int y = 0; y < _lineSize; y++) {
            if (y % _size == 0)
                System.out.println(GetDelimiter());

            for (int x = 0; x < _lineSize; x++) {
                if (x % _size == 0)
                    System.out.print("| ");
                System.out.print(_grid[y][x].toString(_alphabet) + " ");
            }
            System.out.println("|");
        }
        System.out.println(GetDelimiter());
        if (_profiler != null)
            _profiler.finish("Sudoku.PrintGrid");
    }

    public abstract boolean CheckCell(int x, int y, int oldX, int oldY);

    public boolean CheckLine(int oldX, int y) {
        if (_profiler != null)
            _profiler.start("Sudoku.CheckLine");

        for (int x = 0; x < _lineSize; x++) {
            if (!CheckCell(x, y, oldX, y)) {
                if (_profiler != null)
                    _profiler.finish("Sudoku.CheckLine");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish("Sudoku.CheckLine");
        return true;
    }


    public boolean CheckCol(int x, int oldY) {
        if (_profiler != null)
            _profiler.start("Sudoku.CheckCol");

        for (int y = 0; y < _lineSize; y++) {
            if (!CheckCell(x, y, x, oldY)) {
                if (_profiler != null)
                    _profiler.finish("Sudoku.CheckCol");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish("Sudoku.CheckCol");
        return true;
    }


    public boolean CheckBlock(int x, int y) {
        if (_profiler != null)
            _profiler.start("Sudoku.CheckBlock");

        for (int dy = 0; dy < _size; dy++) {
            for (int dx = 0; dx < _size; dx++) {
                int newX = _size * x + dx;
                int newY = _size * y + dy;
                if (!CheckCell(newX, newY, x, y)) {
                    if (_profiler != null)
                        _profiler.finish("Sudoku.CheckBlock");
                    return false;
                }
            }
        }
        if (_profiler != null)
            _profiler.finish("Sudoku.CheckBlock");
        return true;
    }

    public abstract void Solve();

    @Override
    public String toString() {
        if (_profiler != null)
            _profiler.start("Sudoku.toString");
        if (_grid == null) {
            if (_profiler != null)
                _profiler.finish("Sudoku.toString");
            return "null";
        }

        StringBuilder res = new StringBuilder();

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                res.append(Utils.IntToChar(_alphabet, _grid[y][x].getValue()));
            }
        }
        if (_profiler != null)
            _profiler.finish("Sudoku.toString");
        return res.toString();
    }
}

