package fr.ivan.sudoku.bitProcedural;

import fr.ivan.profiler.Profiler;
import fr.ivan.sudoku.util.Cell;

import java.util.Arrays;

public class QuantumCell extends Cell<Integer> {

    private boolean _checked = false;
    private Integer _entropy;
    private Integer _finalValue;
    private int _size;
    private final Profiler _profiler;

    QuantumCell(Profiler profiler, int size, Integer n) {
        _profiler = profiler;
        if (_profiler != null)
            _profiler.start("QuantumCell.QuantumCell");
        _value = 0;
        _size = size;
        if (n != null) {
            _entropy = 1;
            _finalValue = n - 1;
            _value = setIthBit(_value, n-1);
        } else {
            _value = getMaxIntFromSIze(size);
        }
        if (_profiler != null)
            _profiler.finish("QuantumCell.QuantumCell");
    }

    QuantumCell(Profiler profiler, QuantumCell c) {
        _profiler = profiler;
        if (_profiler != null)
            _profiler.start("QuantumCell.QuantumCell");
        _value = c._value;
        _finalValue = c._finalValue;
        _checked = c._checked;
        _entropy = c._entropy;
        _size = c._size;
        if (_profiler != null)
            _profiler.finish("QuantumCell.QuantumCell");
    }

    public static Integer getMaxIntFromSIze(int size) {
        return (int) Math.round(Math.pow(2, size)- 1);
    }

    public static int getMaskBit(int n) {
        return 1 << n;
    }

    public static Integer setIthBit(Integer nb, int bit) {
        return nb | getMaskBit(bit);
    }
    public static Integer unsetIthBit(Integer nb, int size, int bit) {
        return nb & (getMaxIntFromSIze(size) ^ getMaskBit(bit));
    }

    public static int getIthBit(Integer nb, int bit) {
        return nb & getMaskBit(bit);
    }

    public boolean isCompletable() {
        if (_profiler != null)
            _profiler.start("QuantumCell.isCompletable");

        if (_profiler != null)
            _profiler.finish("QuantumCell.isCompletable");
        return _value != 0;
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
        _entropy = Integer.bitCount(_value);

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

        for (int i = 0; i < _size; i++) {
            if (getIthBit(_value, i) == 1) {
                _finalValue = i;
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
        boolean[] res = new boolean[_size];
        for (int i = 0; i < _size; i++) {
            res[i] = getIthBit(_value, i) == 1;
        }
        return res;
    }

    public void setPossibilities(boolean[] poss) {
        _value = 0;
        for (int i = 0; i < poss.length; i++) {
            if (poss[i])
                _value = setIthBit(_value, i);
        }

        _finalValue = null;
        _entropy = null;
    }

    public void resetPossibilities() {
        _finalValue = null;
        _entropy = null;
        _value = 0;
    }

    public void setPossibility(Integer i) {
        if (i == null || getIthBit(_value, i) == 1)
            return;
        _finalValue = null;
        if (_entropy != null)
            _entropy++;
        _value = setIthBit(_value, i);
    }

    public void unsetPossibility(Integer i) {
        if (i == null || getIthBit(_value, i) != 1)
            return;
        _finalValue = null;
        if (_entropy != null)
            _entropy--;
        _value = unsetIthBit(_value, _size, i);
    }

    public void updateChecked() {
        _checked = !_checked;
    }

    public boolean isChecked() {
        return _checked;
    }
}
