package fr.ivan.sudoku.backtracking;

import fr.ivan.sudoku.util.Cell;
import fr.ivan.sudoku.util.Utils;

public class IntCell extends Cell<Integer> {

    private Integer _lineSize;

    IntCell(Integer lineSize, Integer value) {
        _value = value;
    }
    @Override
    public Integer getValue() {
        return _value;
    }

    @Override
    public void setValue(Integer value) {
        _value = value;
    }


}
