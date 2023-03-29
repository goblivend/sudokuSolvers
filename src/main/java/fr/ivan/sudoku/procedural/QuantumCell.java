package fr.ivan.sudoku.procedural;

import fr.ivan.sudoku.util.Cell;

public class QuantumCell extends Cell<boolean[]> {

    private boolean _checked = false;

    QuantumCell(int size, Integer n) {
        _value = new boolean[size];
        if (n != null)
            _value[n-1] = true;
        else {
            for (int i = 1; i <= _value.length; i++) {
                _value[i-1] = true;
            }
        }
    }

    QuantumCell(int size) {
        _value = new boolean[size];
        for (int i = 1; i <= _value.length; i++) {
            _value[i-1] = true;
        }
    }

    QuantumCell(QuantumCell c) {
        _value = new boolean[c._value.length];
        for (int i = 0; i < _value.length; i++) {
            _value[i] = c._value[i];
        }
        _checked = c._checked;
    }

    public boolean isValid(int i) {
        return _value[i-1];
    }

    public boolean isCompletable() {
        for (boolean b: _value) {
            if (b)
                return true;
        }
        return false;
    }

    public int getEntropy() {
        int entropy = 0;
        for (boolean b: _value) {
            if (b)
                entropy++;
        }
        return entropy;
    }

    @Override
    public Integer getValue() {
        if (getEntropy() != 1)
            return null;

        for (int i = 0; i < _value.length; i++) {
            if (_value[i])
                return i;
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
    }

    public void resetPossibilities() {
        for (int i = 0; i < _value.length; i++) {
            _value[i] = false;
        }
    }

    public void setPossibility(int i) {
        _value[i] = true;
    }

    public void unsetPossibility(int i) {
        _value[i] = false;
    }

    public void updateChecked() {
        _checked = !_checked;
    }

    public boolean isChecked() {
        return _checked;
    }
}
