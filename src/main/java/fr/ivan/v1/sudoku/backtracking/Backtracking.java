package fr.ivan.v1.sudoku.backtracking;

import fr.ivan.v1.sudoku.Sudoku;
import fr.ivan.util.Utils;

public class Backtracking extends Sudoku<IntCell> {


    @Override
    protected IntCell initT(char c) {
        return new IntCell(_lineSize, Utils.CharToInt(_alphabet, c));
    }

    @Override
    protected void initGrid() {
        _grid = new IntCell[_lineSize][_lineSize];
    }

    @Override
    public boolean CheckCell(int x, int y, int oldX, int oldY) {
        if(x == oldX && y == oldY)
            return true;

        if (_grid[y][x].getValue() == null)
            return true;

        return !_grid[y][x].getValue().equals(_grid[oldY][oldX].getValue());
    }

    @Override
    public boolean CheckCol(int oldX, int y)
    {
//        boolean[] found = new boolean[_lineSize];

        for (int x = 0; x < _lineSize; x++) {
            if (!CheckCell(x, y, oldX, y))
                return false;
//            if (_grid[y][x].getValue() == null)
//                continue;
//            if (found[_grid[y][x].getValue()-1])
//                return false;
//            found[_grid[y][x].getValue() -1] = true;
        }
        return  true;
    }

    @Override
    public boolean CheckLine(int x, int oldY)
    {
//        boolean[] found = new boolean[_lineSize];

        for (int y = 0; y < _lineSize; y++) {
            if (!CheckCell(x, y, x, oldY))
                return false;
//            if (_grid[y][x].getValue() == null)
//                continue;
//            if (found[_grid[y][x].getValue()-1])
//                return false;
//            found[_grid[y][x].getValue() -1] = true;
        }
        return true;
    }

    @Override
    public boolean CheckBlock(int x, int y)
    {
        boolean[] found = new boolean[_lineSize];
        for (int dy = 0; dy < _size; dy++) {
            for (int dx = 0; dx < _size; dx++) {
                int newX = _size * x + dx;
                int newY = _size * y + dy;
//                if (!CheckCell(newX, newY, x, y))
//                    return false;
                if (_grid[newY][newX].getValue() == null)
                    continue;
                if (found[_grid[newY][newX].getValue()-1])
                    return false;
                found[_grid[newY][newX].getValue() -1] = true;
            }
        }
        return true;
    }

    public boolean isValid() {
        for (int x = 0; x < _lineSize; x++) {
            for (int y = 0; y < _lineSize; y++) {
                if (!CheckCol(x, y) || !CheckLine(x, y) || !CheckBlock(x, y))
                    return false;
            }
        }
        return true;
    }

    private boolean SolveRec(int x, int y)
    {
        if (y >= _lineSize)
            return true;

        if (x >= _lineSize)
            return SolveRec(0, y+1);

        if (_grid[y][x].getValue() != null)
            return SolveRec(x+1, y);

        for (int i = 1; i <= _lineSize; i++) {
            _grid[y][x].setValue(i);
            if (!CheckCol(x, y) || !CheckLine(x, y) || !CheckBlock(x / _size, y / _size))
                continue;

            if (SolveRec(x + 1, y))
                return true;
        }
        _grid[y][x].setValue(null);
        return false;
    }


    @Override
    public void Solve() {
        SolveRec(0, 0);
    }


}
