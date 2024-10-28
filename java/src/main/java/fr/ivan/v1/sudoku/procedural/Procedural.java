package fr.ivan.v1.sudoku.procedural;

import fr.ivan.profiler.Profiler;
import fr.ivan.v1.sudoku.Sudoku;
import fr.ivan.v1.sudoku.backtracking.Backtracking;

import fr.ivan.util.Utils;

import java.awt.Point;


public class Procedural extends Sudoku<QuantumCell> {

    @Override
    protected QuantumCell initT(char c) {
        return new QuantumCell(_profiler, _lineSize, Utils.CharToInt(_alphabet, c));
    }

    @Override
    protected void initGrid() {
        _grid = new QuantumCell[_lineSize][_lineSize];
    }


    @Override

    public void SetGrid(String grid, String alphabet, int size) {
        SetGrid(grid, alphabet, size, null);
    }

    @Override
    public void SetGrid(String grid, String alphabet, int size, Profiler profiler) {
        super.SetGrid(grid, alphabet, size, profiler);
        if (_profiler != null)
            _profiler.start("Procedural.SetGrid");

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                int i = x + _lineSize * y;
                if (grid.charAt(i) != ' ' && grid.charAt(i) != '.') {
                    _grid[y][x].updateChecked();
                }
            }
        }

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                int i = x + _lineSize * y;
                if (grid.charAt(i) != ' ' && grid.charAt(i) != '.') {
                    if (!Propagate(x, y))

                        throw new RuntimeException(getClass().getName() + ".SetGrid(): Unsolvable Sudoku");
                }
            }
        }

        if (_profiler != null)
            _profiler.finish("Procedural.SetGrid");
    }

    @Override
    public boolean CheckCell(int x, int y, int oldX, int oldY) {
        return false;
    }


    public boolean PropagateCell(int x, int y, int oldX, int oldY) {
        if (_profiler != null)
            _profiler.start("Procedural.PropagateCell");
        if (x == oldX && y == oldY) {
            if (_profiler != null)
                _profiler.finish("Procedural.PropagateCell");
            return true;
        }

        _grid[y][x].unsetPossibility(_grid[oldY][oldX].getValue());
        boolean res = _grid[y][x].isCompletable();

        if (_profiler != null)
            _profiler.finish("Procedural.PropagateCell");
        return res;
    }

    public boolean PropagateLine(int oldX, int y) {
        if (_profiler != null)
            _profiler.start("Procedural.PropagateLine");
        for (int x = 0; x < _lineSize; x++) {
            if (!PropagateCell(x, y, oldX, y)) {
                if (_profiler != null)
                    _profiler.finish("Procedural.PropagateLine");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish("Procedural.PropagateLine");
        return true;
    }

    public boolean PropagateCol(int x, int oldY) {
        if (_profiler != null)
            _profiler.start("Procedural.PropagateCol");
        for (int y = 0; y < _lineSize; y++) {
            if (!PropagateCell(x, y, x, oldY)) {
                if (_profiler != null)
                    _profiler.finish("Procedural.PropagateCol");
                return false;
            }
        }
        if (_profiler != null)
            _profiler.finish("Procedural.PropagateCol");
        return true;
    }

    public boolean PropagateBlock(int x, int y) {
        if (_profiler != null)
            _profiler.start("Procedural.PropagateBlock");
        for (int dy = 0; dy < _size; dy++) {
            for (int dx = 0; dx < _size; dx++) {
                int newX = (x / _size) * _size + dx;
                int newY = (y / _size) * _size + dy;
                if (!PropagateCell(newX, newY, x, y)) {
                    if (_profiler != null)
                        _profiler.finish("Procedural.PropagateBlock");
                    return false;
                }
            }
        }
        if (_profiler != null)
            _profiler.finish("Procedural.PropagateBlock");
        return true;
    }

    public boolean Propagate(int x, int y) {
        return PropagateCol(x, y) && PropagateLine(x, y) && PropagateBlock(x, y);
    }

    Point getCell() {
        if (_profiler != null)
            _profiler.start("Procedural.getCell");
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
                            _profiler.finish("Procedural.getCell");
                        return res;
                    }
                }
            }
        }
        if (_profiler != null)
            _profiler.finish("Procedural.getCell");
      
        return res;
    }

    QuantumCell[][] CopyGrid() {
        if (_profiler != null)
            _profiler.start("Procedural.CopyGrid");

        QuantumCell[][] copy = new QuantumCell[_lineSize][_lineSize];

        for (int x = 0; x < _lineSize; x++) {
            for (int y = 0; y < _lineSize; y++) {

                copy[y][x] = new QuantumCell(_profiler, _grid[y][x]);
            }
        }
        if (_profiler != null)
            _profiler.finish("Procedural.CopyGrid");

        return copy;
    }

    boolean SolveRec() {
        if (_profiler != null)
            _profiler.start("Procedural.SolveRec");
        Point cell = getCell();
        if (cell == null) {
            if (_profiler != null)
                _profiler.finish("Procedural.SolveRec");
            return true;
        }

        QuantumCell gridCell = _grid[cell.y][cell.x];
        if (!gridCell.isCompletable()) {
            if (_profiler != null)
                _profiler.finish("Procedural.SolveRec");
            return false;
        }

        gridCell.updateChecked();
        if (gridCell.getEntropy() == 1) {
            boolean res = Propagate(cell.x, cell.y) && SolveRec();
            if (!res)
                gridCell.updateChecked();
            if (_profiler != null)
                _profiler.finish("Procedural.SolveRec");

            return res;
        }

        boolean[] currPoss = gridCell.getPossibilities();
        gridCell.resetPossibilities();
        for (int n = 1; n <= _lineSize; n++) {
            if (!currPoss[n-1])
                continue;
            QuantumCell[][] oldGrid = CopyGrid();

            gridCell.setPossibility(n);
            if (Propagate(cell.x, cell.y) && SolveRec()) {
                if (_profiler != null)
                    _profiler.finish("Procedural.SolveRec");
                return true;
            }
            _grid = oldGrid;
            gridCell = _grid[cell.y][cell.x];
        }
        gridCell.setPossibilities(currPoss);
        gridCell.updateChecked();
        if (_profiler != null)
            _profiler.finish("Procedural.SolveRec");

        return false;
    }

    @Override
    public void Solve() {
//        System.out.println("Validity : " + CheckValidity());
        System.out.println("Solve Res : " + SolveRec());
        System.out.println(_profiler);
    }

    public boolean CheckValidity() {
        if (_profiler != null)
            _profiler.start("Procedural.CheckValidity");
      
        Backtracking bt = new Backtracking();
        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                boolean[] currPoss = _grid[y][x].getPossibilities();
                _grid[y][x].resetPossibilities();
                for (int n = 1; n <= _lineSize; n++) {
                    if (!currPoss[n-1])
                        continue;
                    QuantumCell[][] oldGrid = CopyGrid();
                    _grid[y][x].setPossibility(n);

                    bt.SetGrid(this.toString(), _alphabet, _size);

                    if (!bt.isValid()) {
                        _profiler.finish("Procedural.CheckValidity");
                        return false;
                    }

                    _grid = oldGrid;
                }
                _grid[y][x].setPossibilities(currPoss);

            }
        }
        if (_profiler != null)
            _profiler.finish("Procedural.CheckValidity");
        return true;
    }
}
