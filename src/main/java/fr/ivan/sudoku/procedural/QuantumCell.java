package fr.ivan.sudoku.procedural;

import fr.ivan.profiler.Profiler;
import fr.ivan.sudoku.util.Cell;

import java.util.Arrays;

public class QuantumCell extends Cell<boolean[]> {

    private boolean _checked = false;
    private Integer _entropy;
    private Integer _finalValue;
    private final Profiler _profiler;

    QuantumCell(Profiler profiler, int size, Integer n) {
        _profiler = profiler;
        if (_profiler != null)
            _profiler.start("QuantumCell.QuantumCell");
        _value = new boolean[size];
        if (n != null) {
            _entropy = 1;
            _finalValue = n;
            _value[n - 1] = true;
        } else {
            for (int i = 1; i <= _value.length; i++) {
                _value[i - 1] = true;
            }
        }
        if (_profiler != null)
            _profiler.finish("QuantumCell.QuantumCell");
    }

    QuantumCell(Profiler profiler, QuantumCell c) {
        _profiler = profiler;
        if (_profiler != null)
            _profiler.start("QuantumCell.QuantumCell");
        _value = new boolean[c._value.length];
        _finalValue = c._finalValue;
        System.arraycopy(c._value, 0, _value, 0, _value.length);
        _checked = c._checked;
        _entropy = c._entropy;
        if (_profiler != null)
            _profiler.finish("QuantumCell.QuantumCell");
    }

    public boolean isCompletable() {
        if (_profiler != null)
            _profiler.start("QuantumCell.isCompletable");
        if (_entropy != null) {
            if (_profiler != null)
                _profiler.finish("QuantumCell.isCompletable");
            return _entropy != 0;
        }
        for (boolean b : _value) {
            if (b) {
                if (_profiler != null)
                    _profiler.finish("QuantumCell.isCompletable");
                return true;
            }
        }
        if (_profiler != null)
            _profiler.finish("QuantumCell.isCompletable");
        return false;
    }

    public int getEntropy() {
        if (_profiler != null)
            _profiler.start("QuantumCell.getEntropy");

        if (_entropy != null) {
            if (_profiler != null)
                _profiler.finish("QuantumCell.getEntropy");
            return _entropy;
        }

        if (_finalValue != null) {
            if (_profiler != null)
                _profiler.finish("QuantumCell.getEntropy");
            return 1;
        }

        _entropy = 0;
        for (boolean b : _value) {
            if (b)
                _entropy++;
        }
        if (_profiler != null)
            _profiler.finish("QuantumCell.getEntropy");

        return _entropy;
    }

    @Override
    public Integer getValue() {
        if (_profiler != null)
            _profiler.start("QuantumCell.getValue");
        if (_finalValue != null) {
            if (_profiler != null)
                _profiler.finish("QuantumCell.getValue");
            return _finalValue;
        }
        if (getEntropy() != 1) {
            if (_profiler != null)
                _profiler.finish("QuantumCell.getValue");
            return null;
        }

        for (int i = 0; i < _value.length; i++) {
            if (_value[i]) {
                _finalValue = i+1;
                if (_profiler != null)
                    _profiler.finish("QuantumCell.getValue");
                return i;
            }
        }
        if (_profiler != null)
            _profiler.finish("QuantumCell.getValue");
        return null;
    }

    @Override
    public void setValue(Integer value) {

    }

    public boolean[] getPossibilities() {
        return _value.clone();
    }

    public void setPossibilities(boolean[] poss) {
        _value = poss.clone();
        _finalValue = null;
        _entropy = null;
    }

    public void resetPossibilities() {
        _finalValue = null;
        _entropy = null;
        Arrays.fill(_value, false);
    }

    public void setPossibility(Integer i) {
        if (i == null || _value[i])
            return;
        _finalValue = null;
        if (_entropy != null)
            _entropy++;
        _value[i] = true;
    }

    public void unsetPossibility(Integer i) {
        if (i == null || !_value[i])
            return;
        _finalValue = null;
        if (_entropy != null)
            _entropy--;
        _value[i] = false;
    }

    public void updateChecked() {
        _checked = !_checked;
    }

    public boolean isChecked() {
        return _checked;
    }
}
