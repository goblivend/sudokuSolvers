package fr.ivan.sudoku.procedural;

import fr.ivan.sudoku.Sudoku;
import fr.ivan.sudoku.backtracking.Backtracking;
import fr.ivan.sudoku.backtracking.IntCell;
import fr.ivan.sudoku.util.Utils;

import java.awt.Point;


public class Procedural extends Sudoku<QuantumCell> {

    @Override
    protected QuantumCell initT(char c) {
        return new QuantumCell(_lineSize, Utils.HexToInt(c));
    }

    @Override
    protected void initGrid() {
        _grid = new QuantumCell[_lineSize][_lineSize];
    }


    @Override
    public void SetGrid(String grid, int size) {
        super.SetGrid(grid, size);

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                int i = x + _lineSize * y;
                if (grid.charAt(i) != ' ' && grid.charAt(i) != '.')
                {
                    _grid[y][x].updateChecked();
                }
            }
        }

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                int i = x + _lineSize * y;
                if (grid.charAt(i) != ' ' && grid.charAt(i) != '.')
                {
                    if (!Propagate(x, y, _grid[y][x].getValue()))
                        throw new RuntimeException(getClass().getName() + ".SetGrid(): Unsolvable Sudoku");
                }
            }
        }
        do {
            Point p = getCell();
            if (_grid[p.y][p.x].getEntropy() != 1)
                break;
            _grid[p.y][p.x].updateChecked();
            if (!Propagate(p.x, p.y, _grid[p.y][p.x].getValue()))
                throw new RuntimeException(getClass().getName() + ".SetGrid(): Unsolvable Sudoku");

        } while (true);
    }


    public boolean PropagateCell(int x, int y, int oldX, int oldY, int i) {
        if(x == oldX && y == oldY)
            return true;

        _grid[y][x].unsetPossibility(i);
        return _grid[y][x].isCompletable();
    }

    public boolean PropagateCol(int oldX, int y, int i) {
        for (int x = 0; x < _lineSize; x++) {
            if (!PropagateCell(x, y, oldX, y, i))
                return false;
        }
        return true;
    }

    public boolean PropagateLine(int x, int oldY, int i) {
        for (int y = 0; y < _lineSize; y++) {
            if (!PropagateCell(x, y, x, oldY, i))
                return false;
        }
        return true;
    }

    public boolean PropagateBlock(int x, int y, int i) {
        for (int dy = 0; dy < _size; dy++) {
            for (int dx = 0; dx < _size; dx++) {
                int newX = (x/_size) * _size + dx;
                int newY = (y/_size) * _size + dy;
                if (!PropagateCell(newX, newY, x, y, i))
                    return false;
            }
        }
        return true;
    }

    public boolean Propagate(int x, int y, int i) {
        return PropagateCol(x, y, i) && PropagateLine(x, y, i) && PropagateBlock(x, y, i);
    }

    Point getCell(){
        int minEntropy = _lineSize+1;
        Point res = null;
        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                int entropy = _grid[y][x].getEntropy();
                if (!_grid[y][x].isChecked() && entropy < minEntropy) {
                    minEntropy = entropy;
                    res = new Point(x, y);
                }
            }
        }
        return res;
    }

    QuantumCell[][] CopyGrid() {
        QuantumCell[][] copy = new QuantumCell[_lineSize][_lineSize];

        for (int x = 0; x < _lineSize; x++) {
            for (int y = 0; y < _lineSize; y++) {
                copy[y][x] = new QuantumCell(_grid[y][x]);
            }
        }
        return copy;
    }

    boolean SolveRec() {
        Point cell = getCell();
        if (cell == null)
            return true;

        QuantumCell gridCell =  _grid[cell.y][cell.x];
        if (!gridCell.isCompletable())
            return false;

        gridCell.updateChecked();
        if (gridCell.getEntropy() == 1)
        {
:            boolean res = Propagate(cell.x, cell.y, gridCell.getValue()) && SolveRec();
            if (!res)
                gridCell.updateChecked();
            return res;
        }

        boolean[] currPoss = gridCell.getPossibilities();
        gridCell.resetPossibilities();
        for (int i = 0; i < _lineSize; i++) {
            if (!currPoss[i])
                continue;
            QuantumCell[][] oldGrid = CopyGrid();

            gridCell.setPossibility(i);
            if (Propagate(cell.x, cell.y, i) && SolveRec())
                return true;
//            gridCell.unsetPossibility(i);
            _grid = oldGrid;
            gridCell =  _grid[cell.y][cell.x];
        }
        gridCell.setPossibilities(currPoss);
        gridCell.updateChecked();
        return false;
    }

    @Override
    public void Solve() {
        System.out.println("Validity : " + CheckValidity());
        System.out.println("Solve Res : " + SolveRec());
    }

    public boolean CheckValidity() {
        Backtracking bt = new Backtracking();
        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                boolean[] currPoss = _grid[y][x].getPossibilities();
                _grid[y][x].resetPossibilities();
                for (int i = 0; i < _lineSize; i++) {
                    if (!currPoss[i])
                        continue;
                    QuantumCell[][] oldGrid = CopyGrid();
                    _grid[y][x].setPossibility(i);

                    bt.SetGrid(this.toString(), _size);

                    if (!bt.isValid())
                        return false;

                    _grid = oldGrid;
                }
                _grid[y][x].setPossibilities(currPoss);

            }
        }
        return true;
    }

    @Override
    public String toString() {
        String res = "";

        for (int y = 0; y < _lineSize; y++) {
            for (int x = 0; x < _lineSize; x++) {
                    res += _grid[y][x];
            }
        }
        return res;
    }
}
