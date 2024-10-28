package fr.ivan.v1.sudoku.backtracking;

import fr.ivan.util.Cell;

public class IntCell extends Cell<Integer> {
    IntCell(Integer size, Integer value) {
        _size = size;
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
