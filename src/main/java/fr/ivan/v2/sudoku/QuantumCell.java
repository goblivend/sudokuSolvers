package fr.ivan.v2.sudoku;

import fr.ivan.profiler.Profiler;
import fr.ivan.util.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class QuantumCell extends Cell<Long> {

    private boolean _checked = false;
    private Integer _entropy;
    private Integer _finalValue;
    private final Profiler _profiler;

    QuantumCell(Profiler profiler, int size, Integer n) {
        _profiler = profiler;
        if (_profiler != null)
            _profiler.start("QuantumCell.QuantumCell");
        _size = size;
        if (n != null) {
            _entropy = 1;
            _finalValue = n;
            _value = setIthBit(0, n-1);
        } else {
            _value = getMaxIntFromSize(size);
        }
        if (_profiler != null)
            _profiler.finish("QuantumCell.QuantumCell");
    }
    QuantumCell(QuantumCell c) {
        _profiler = c._profiler;
        if (_profiler != null)
            _profiler.start("QuantumCell.QuantumCellCopy");
        _value = c._value;
        _finalValue = c._finalValue;
        _checked = c._checked;
        _entropy = c._entropy;
        _size = c._size;
        if (_profiler != null)
            _profiler.finish("QuantumCell.QuantumCellCopy");
    }

    public static long getMaxIntFromSize(int size) {
        return (1L << size) - 1;
    }
    public static long getMaskBit(int n) {
        return 1L << n;
    }
    public static long setIthBit(long nb, int bit) {
        return nb | getMaskBit(bit);
    }
    public static long unsetIthBit(long nb, int bit) {
        return nb & ~getMaskBit(bit);
    }
    public static long getIthBit(long nb, int bit) {
        return (nb & getMaskBit(bit)) == 0 ? 0 : 1;
    }

    public boolean isCompletable() {
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
        _entropy = Long.bitCount(_value);

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
            if (getIthBit(_value, i) != 0) {
                _finalValue = i+1;
                if (_profiler != null)
                    _profiler.finish("QuantumCell.getValue");
                return i+1;
            }
        }
        if (_profiler != null)
            _profiler.finish("QuantumCell.getValue");
        return null;
    }

    @Override
    public void setValue(Integer n) {
        if (n == null)// || getIthBit(_value, n-1) != 0)
            return;
        _value = getMaskBit(n-1);
        _finalValue = n;
        _entropy = 1;
    }

    public List<Integer> getPossibilities() {
        List<Optional<Integer>> poss = new ArrayList<>();
        for (int i = 0; i < _size; i++) {
            poss.add(ofNullable(getIthBit(_value, i) != 0 ? i+1 : null));
        }

        return poss.stream().flatMap(Optional::stream).collect(toList());
    }

    public void setPossibilities(List<Integer> poss) {
        _value = 0L;
        for (Integer n : poss) {
            _value |= getMaskBit(n);
        }
        _finalValue = null;
        _entropy = null;
    }

    /**
     * Set all possibilities to false
     */
    public void resetPossibilities() {
        _finalValue = null;
        _entropy = null;
        _value = 0L;
    }

    /**
     * Sets the number n as a possible choice
     * @param n the number to accept
     */
    public void setPossibility(Integer n) {
        if (n == null || getIthBit(_value, n-1) == 1)
            return;
        _finalValue = null;
        if (_entropy != null)
            _entropy++;
        _value = setIthBit(_value, n-1);
    }

    /**
     * Unsets the number n as a possible choice
     * @param n the number to reject
     */
    public void unsetPossibility(Integer n) {
        if (n == null || getIthBit(_value, n-1) != 1)
            return;
        _finalValue = null;
        if (_entropy != null)
            _entropy--;
        _value = unsetIthBit(_value, n-1);
    }

    public void updateChecked() {
        _checked = !_checked;
    }

    public boolean isChecked() {
        return _checked;
    }
}
