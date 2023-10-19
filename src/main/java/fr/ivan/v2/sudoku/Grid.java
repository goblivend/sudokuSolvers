package fr.ivan.v2.sudoku;

import fr.ivan.profiler.Profiler;
import fr.ivan.util.Utils;

import java.awt.*;

import static fr.ivan.util.Utils.CharToInt;

public class Grid {
    private final int _lineSize;
    private final int _size;
    private QuantumCell[][] _grid;
    private String _alphabet;
    private final Profiler _profiler;

    public Grid(Grid grid) {
        this._lineSize = grid._lineSize;
        this._size = grid._size;
        this._grid = grid.copyGrid();
        this._alphabet = grid._alphabet;
        this._profiler = grid._profiler;
    }
    public Grid(int size, String grid) {
        this(size, grid, Utils.getAlphabet(size), null);
    }

    public Grid(int size, String grid, String alphabet) {
        this(size, grid, alphabet, null);
    }

    public Grid(int size, String grid, Profiler profiler) {
        this(size, grid, Utils.getAlphabet(size), profiler);
    }

    public Grid(int size, String grid, String alphabet, Profiler profiler) {
        this._size = size;
        this._lineSize = _size * _size;
        this._alphabet = alphabet;
        this._profiler = profiler;
        setGrid(grid);
    }

    public int getLineSize() {
        return _lineSize;
    }
    public int getSize() {
        return _size;
    }
    public QuantumCell getCell(int x, int y) {
        return _grid[y][x];
    }

    private boolean PropagateCell(int x, int y, int oldX, int oldY) {
        if (_profiler != null)
            _profiler.start( "BitProcedural.PropagateCell");
        if (x == oldX && y == oldY) {
            if (_profiler != null)
                _profiler.finish( "BitProcedural.PropagateCell");
            return true;
        }

        _grid[y][x].unsetPossibility(_grid[oldY][oldX].getValue());
        boolean res = _grid[y][x].isCompletable();

        if (_profiler != null)
            _profiler.finish( "BitProcedural.PropagateCell");
        return res;
    }

    private boolean PropagateLine(int oldX, int y) {
        if (_profiler != null)
            _profiler.start( "BitProcedural.PropagateLine");
        for (int x = 0; x < _lineSize; x++) {
            if (!PropagateCell(x, y, oldX, y)) {
                if (_profiler != null)
                    _profiler.finish( "BitProcedural.PropagateLine");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish( "BitProcedural.PropagateLine");
        return true;
    }

    private boolean PropagateCol(int x, int oldY) {
        if (_profiler != null)
            _profiler.start( "BitProcedural.PropagateCol");
        for (int y = 0; y < _lineSize; y++) {
            if (!PropagateCell(x, y, x, oldY)) {
                if (_profiler != null)
                    _profiler.finish("BitProcedural.PropagateCol");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish("BitProcedural.PropagateCol");
        return true;
    }

    private boolean PropagateBlock(int x, int y) {
        if (_profiler != null)
            _profiler.start("BitProcedural.PropagateBlock");
        for (int dy = 0; dy < _size; dy++) {
            for (int dx = 0; dx < _size; dx++) {
                int newX = (x / _size) * _size + dx;
                int newY = (y / _size) * _size + dy;
                if (!PropagateCell(newX, newY, x, y)) {
                    if (_profiler != null)
                        _profiler.finish("BitProcedural.PropagateBlock");
                    return false;
                }
            }
        }
        if (_profiler != null)
            _profiler.finish("BitProcedural.PropagateBlock");
        return true;
    }

    public boolean propagate(int x, int y) {
        return PropagateCol(x, y) && PropagateLine(x, y) && PropagateBlock(x, y);
    }

    public Point getCell() {
        if (_profiler != null)
            _profiler.start("BitProcedural.getCell");
        int minEntropy = _lineSize + 1;
        Point res = null;
        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                // If checked no need to calculate entropy (even though should be fast)
                if (_grid[y][x].isChecked())
                    continue;
                int entropy = _grid[y][x].getEntropy();
                if (entropy < minEntropy) {
                    minEntropy = entropy;
                    res = new Point(x, y);
                    if (entropy == 1) {
                        if (_profiler != null)
                            _profiler.finish("BitProcedural.getCell");
                        return res;
                    }
                }
            }
        }
        if (_profiler != null)
            _profiler.finish("BitProcedural.getCell");
        return res;
    }

    private void setGrid(String grid) {
        if (_profiler != null)
            _profiler.start("Sudoku.SetGrid");
        int nbElt = _lineSize * _lineSize;
        if (grid.length() != nbElt)
            throw new RuntimeException(this.getClass().getName() + ".SetGrid(): Invalid grid not containing the good number of cols: " + grid.length() + " and should be : " + nbElt);

        if (_alphabet.length() != _lineSize+1)
            throw new RuntimeException(this.getClass().getName() + ".SetGrid(): Invalid alphabet not containing enough chars: " + _alphabet.length() + " and should be : " + _lineSize + " + 1 for blank");

        _grid = new QuantumCell[_lineSize][_lineSize];
        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                int i = x + _lineSize * y;
                char c = grid.charAt(i);
                _grid[y][x] = new QuantumCell(_profiler, _lineSize, CharToInt(_alphabet, c));
                if (grid.charAt(i) != _alphabet.charAt(0)) {
                    _grid[y][x].updateChecked();
                }
            }
        }

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                int i = x + _lineSize * y;

                if (grid.charAt(i) != ' ' && grid.charAt(i) != '.') {
                    if (!propagate(x, y))
                        throw new RuntimeException(getClass().getName() + ".SetGrid(): Unsolvable Sudoku");
                }
            }
        }
//        do {
//            Point p = getCell();
//            // if Solved :
//            if (p == null)
//                break;
//            if (_grid[p.y][p.x].getEntropy() != 1)
//                break;
//            _grid[p.y][p.x].updateChecked();
//            if (!Propagate(p.x, p.y))
//                throw new RuntimeException(getClass().getName() + ".SetGrid(): Unsolvable Sudoku");
//
//        } while (true);
    }

    private QuantumCell[][] copyGrid() {
        QuantumCell[][] copy = new QuantumCell[_lineSize][_lineSize];
        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                copy[y][x] = new QuantumCell(_grid[y][x]);
            }
        }
        return copy;
    }

    @Override
    public String toString() {
        return toString(_alphabet);
    }

    public String toString(String alphabet) {
        return toString(false, alphabet);
    }

    public String toString(boolean pretty) {
        return toString(pretty, _alphabet);
    }
    public String toString(boolean pretty, String alphabet) {
        String str = "";
        for (int y = 0; y < getLineSize(); y++) {
            if (pretty && y % getSize() == 0)
                str += getDelimiter();

            for (int x = 0; x < getLineSize(); x++) {
                if (pretty && x % getSize() == 0)
                    str += "| ";
                str += getCell(x, y).toString(alphabet);
                if (pretty)
                    str += " ";
            }
            if (pretty)
                str += "|\n";

        }
        if (pretty)
            str += getDelimiter();

        return str;
    }
    private String getDelimiter() {
        return "-".repeat((getLineSize() + getSize()) * 2 + 1) + "\n";
    }

}
