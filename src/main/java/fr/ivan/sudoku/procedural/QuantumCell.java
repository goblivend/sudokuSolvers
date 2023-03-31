package fr.ivan.sudoku.procedural;

import fr.ivan.sudoku.util.Cell;

public class QuantumCell extends Cell<boolean[]> {

    private boolean _checked = false;
    private Integer _finalValue;

    QuantumCell(int size, Integer n) {
        _value = new boolean[size];
        if (n != null) {
            _finalValue = n-1;
            _value[n - 1] = true;
        }
        else {
            for (int i = 1; i <= _value.length; i++) {
                _value[i-1] = true;
            }
        }
    }

    QuantumCell(QuantumCell c) {
        _value = new boolean[c._value.length];
        _finalValue = null;
        for (int i = 0; i < _value.length; i++) {
            _value[i] = c._value[i];
        }
        _checked = c._checked;
    }

//    public boolean isValid(int i) {
//        return _value[i-1];
//    }

    public boolean isCompletable() {
        for (boolean b: _value) {
            if (b)
                return true;
        }
        return false;
    }

    public int getEntropy() {
        if (_finalValue != null)
            return 1;

        int entropy = 0;
        for (boolean b: _value) {
            if (b)
                entropy++;
        }
        return entropy;
    }

    @Override
    public Integer getValue() {
        if (_finalValue != null)
            return _finalValue;

        if (getEntropy() != 1)
            return null;

        for (int i = 0; i < _value.length; i++) {
            if (_value[i]) {
                _finalValue = i;
                return i;
            }
        }
        return null;
    }

    @Override
    public void setValue(Integer value) {

    }

    public boolean[] getPossibilities(){
        return _value.clone();
    }

    public void setPossibilities(boolean[] poss) {
        _value = poss.clone();
        _finalValue = null;
    }

    public void resetPossibilities() {
        _finalValue = null;
        for (int i = 0; i < _value.length; i++) {
            _value[i] = false;
        }
    }

    public void setPossibility(Integer i) {
        if (i == null)
            return;
        _finalValue = null;
        _value[i] = true;
    }

    public void unsetPossibility(Integer i) {
        if (i == null)
            return;
        _finalValue = null;
        _value[i] = false;
    }

    public void updateChecked() {
        _checked = !_checked;
    }

    public boolean isChecked() {
        return _checked;
    }
}
