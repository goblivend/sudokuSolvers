package fr.ivan.v2.sudoku;

import fr.ivan.profiler.Profiler;

import java.awt.*;
import java.util.List;


public class BitProcedural implements Sudoku {
    private Profiler _profiler;
    private Grid _grid;

    @Override
    public void setGrid(String grid, String alphabet, int size) {
        setGrid(grid, alphabet, size, null);
    }

    @Override
    public void setGrid(String grid, String alphabet, int size, Profiler profiler) {
        _profiler = profiler;
        _grid = new Grid(size, grid, alphabet, profiler);
    }

    @Override
    public String toString(String alphabet) {
        return _grid.toString(alphabet);
    }

    @Override
    public String toString(boolean pretty) {
        return _grid.toString(pretty);
    }

    @Override
    public String toString(boolean pretty, String alphabet) {
        return _grid.toString(pretty, alphabet);
    }

    @Override
    public String toString() {
        return _grid.toString();
    }

    Grid CopyGrid() {
        if (_profiler != null)
            _profiler.start("BitProcedural.CopyGrid");
        Grid copy = new Grid(_grid);

        if (_profiler != null)
            _profiler.finish("BitProcedural.CopyGrid");
        return copy;
    }

    private boolean solveRec() {
        if (_profiler != null)
            _profiler.start("BitProcedural.SolveRec");
        Point cell = _grid.getCell();
        if (cell == null) {
            if (_profiler != null)
                _profiler.finish("BitProcedural.SolveRec");
            return true;
        }

        QuantumCell gridCell = _grid.getCell(cell.x, cell.y);
        if (!gridCell.isCompletable()) {
            if (_profiler != null)
                _profiler.finish("BitProcedural.SolveRec");
            return false;
        }

        gridCell.updateChecked();
        if (gridCell.getEntropy() == 1) {
            boolean res = _grid.propagate(cell.x, cell.y) && solveRec();
            if (!res)
                gridCell.updateChecked();
            if (_profiler != null)
                _profiler.finish("BitProcedural.SolveRec");
            return res;
        }

        List<Integer> possibilities = gridCell.getPossibilities();
        gridCell.resetPossibilities();
        for (Integer n : possibilities) {
            Grid oldGrid = CopyGrid();

            gridCell.setValue(n);
            if (_grid.propagate(cell.x, cell.y) && solveRec()) {
                if (_profiler != null)
                    _profiler.finish("BitProcedural.SolveRec");
                return true;
            }
            _grid = oldGrid;
            gridCell = _grid.getCell(cell.x, cell.y);
        }
        gridCell.setPossibilities(possibilities);
        gridCell.updateChecked();
        if (_profiler != null)
            _profiler.finish("BitProcedural.SolveRec");
        return false;
    }

    @Override
    public void solve() {
        System.out.println("Solve Res : " + solveRec());
        System.out.println(_profiler);
    }
}
