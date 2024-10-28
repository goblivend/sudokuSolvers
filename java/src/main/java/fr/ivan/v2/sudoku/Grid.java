package fr.ivan.v2.sudoku;

import fr.ivan.profiler.Profiler;
import fr.ivan.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static fr.ivan.util.Utils.CharToInt;

public class Grid {
    private final int _lineSize;
    private final int _size;
    private QuantumCell[][] _grid;
    private final String _alphabet;
    private final Profiler _profiler;

    private List<Integer>[][] _cols;
    private List<Integer>[][] _lines;
    private List<Integer>[][] _blocks;

    public Grid(Grid grid) {
        this._profiler = grid._profiler;
        if (_profiler != null)
            _profiler.start("Grid.Grid(Grid)");
        this._lineSize = grid._lineSize;
        this._size = grid._size;
        this._grid = grid.copyGrid();
        this._alphabet = grid._alphabet;
        this._cols = copyArray(grid._cols);
        this._lines = copyArray(grid._lines);
        this._blocks = copyArray(grid._blocks);
        if (_profiler != null)
            _profiler.finish("Grid.Grid(Grid)");
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

    private void unSetPossibility(Integer x, Integer y, int n) {
        getCell(x, y).unsetPossibility(n);
        _cols[n-1][x].remove(y);
        _lines[n-1][y].remove(x);
        int blockNb = (y/_size)*_size + x/_size;
        Integer nbInBlock = (x%_size) + (y%_size)*_size;
        _blocks[n-1][blockNb].remove(nbInBlock);
    }

    private boolean PropagateCell(int x, int y, int oldX, int oldY) {
        if (_profiler != null)
            _profiler.start( "Grid.PropagateCell");
        if (x == oldX && y == oldY) {
            if (_profiler != null)
                _profiler.finish( "Grid.PropagateCell");
            return true;
        }

        unSetPossibility(x, y, getCell(oldX, oldY).getValue());
        boolean res = getCell(x, y).isCompletable();

        if (_profiler != null)
            _profiler.finish( "Grid.PropagateCell");
        return res;
    }

    private boolean PropagateLine(int oldX, int y) {
        if (_profiler != null)
            _profiler.start( "Grid.PropagateLine");
        for (int x = 0; x < _lineSize; x++) {
            if (!PropagateCell(x, y, oldX, y)) {
                if (_profiler != null)
                    _profiler.finish( "Grid.PropagateLine");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish( "Grid.PropagateLine");
        return true;
    }

    private boolean PropagateCol(int x, int oldY) {
        if (_profiler != null)
            _profiler.start( "Grid.PropagateCol");
        for (int y = 0; y < _lineSize; y++) {
            if (!PropagateCell(x, y, x, oldY)) {
                if (_profiler != null)
                    _profiler.finish("Grid.PropagateCol");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish("Grid.PropagateCol");
        return true;
    }

    private boolean PropagateBlock(int x, int y) {
        if (_profiler != null)
            _profiler.start("Grid.PropagateBlock");
        for (int dy = 0; dy < _size; dy++) {
            for (int dx = 0; dx < _size; dx++) {
                int newX = (x / _size) * _size + dx;
                int newY = (y / _size) * _size + dy;
                if (!PropagateCell(newX, newY, x, y)) {
                    if (_profiler != null)
                        _profiler.finish("Grid.PropagateBlock");
                    return false;
                }
            }
        }
        if (_profiler != null)
            _profiler.finish("Grid.PropagateBlock");
        return true;
    }

    public boolean propagate(int x, int y) {
        return PropagateCol(x, y) && PropagateLine(x, y) && PropagateBlock(x, y);
    }

    public Point getCell() {
        if (_profiler != null)
            _profiler.start("Grid.getCell");
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
                            _profiler.finish("Grid.getCell");
                        return res;
                    }
                }
            }
        }
        if (minEntropy <= 1 || minEntropy == _lineSize+1) {
            if (_profiler != null)
                _profiler.finish("Grid.getCell");
            return res;
        }

        if (_profiler != null)
            _profiler.start("Grid.getCell.v2");

        for (int n = 1; n <= _lineSize; n++) {
            for (int i = 0; i < _lineSize; i++) {
                int y = _cols[n-1][i].get(0);
                if (_cols[n-1][i].size() == 1
                    && !getCell(i, y).isChecked()) {
                    getCell(i, y).setValue(n);
                    if (_profiler != null) {
                        _profiler.finish("Grid.getCell.v2");
                        _profiler.finish("Grid.getCell");
                    }
                    return new Point(i, y);
                }

                int x = _lines[n-1][i].get(0);
                if (_lines[n-1][i].size() == 1
                        && !getCell(x, i).isChecked()) {
                    getCell(x, i).setValue(n);
                    if (_profiler != null){
                        _profiler.finish("Grid.getCell.v2");
                        _profiler.finish("Grid.getCell");
                    }
                    return new Point(x, i);
                }

                int nbInBlock = _blocks[n-1][i].get(0);
                int blockY = i/_size*_size + nbInBlock/_size;
                int blockX = i%_size*_size + nbInBlock%_size;
                if (_blocks[n-1][i].size() == 1
                        && !getCell(blockX, blockY).isChecked()) {
                    getCell(blockX, blockY).setValue(n);
                    if (_profiler != null){
                        _profiler.finish("Grid.getCell.v2");
                        _profiler.finish("Grid.getCell");
                    }
                    return new Point(blockX, blockY);
                }
            }
        }

        if (_profiler != null){
            _profiler.finish("Grid.getCell.v2");
            _profiler.finish("Grid.getCell");
        }
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
        _cols = initEnthropy();
        _lines = initEnthropy();
        _blocks = initEnthropy();

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

                if (grid.charAt(i) != _alphabet.charAt(0)) {
                    if (!propagate(x, y))
                        throw new RuntimeException(getClass().getName() + ".SetGrid(): Unsolvable Sudoku");
                }
            }
        }
    }

    private List<Integer>[][] initEnthropy() {
        List<Integer>[][] arr = new List[_lineSize][_lineSize];

        for (int i = 0; i < _lineSize; i++) {
            for (int j = 0; j < _lineSize; j++) {
                arr[i][j] = new ArrayList<>(_lineSize);
                for (int k = 0; k < _lineSize; k++) {
                    arr[i][j].add(k);
                }
            }
        }
        return arr;
    }

    private QuantumCell[][] copyGrid() {
        if (_profiler != null)
            _profiler.start("Grid.copyGrid");
        QuantumCell[][] copy = new QuantumCell[_lineSize][_lineSize];
        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                copy[y][x] = new QuantumCell(_grid[y][x]);
            }
        }

        if (_profiler != null)
            _profiler.finish("Grid.copyGrid");
        return copy;
    }

    private List<Integer>[][] copyArray(List<Integer>[][] arr) {
        if (_profiler != null)
            _profiler.start("Grid.copyArray");
        if (arr.length == 0) {
            return new List[0][];
        }
        if (arr[0].length == 0) {
            return new List[arr.length][0];
        }

        List<Integer>[][] newArr = new List[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                newArr[i][j] = new ArrayList<>(arr[i][j]);
            }
        }

        if (_profiler != null)
            _profiler.finish("Grid.copyArray");
        return newArr;
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
