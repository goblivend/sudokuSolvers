package fr.ivan.sudoku.util;

public abstract class Cell<T> {

    protected T _value;

    public abstract Integer getValue();

    public abstract void setValue(Integer value);

    @Override
    public String toString() {
        return getValue() == null ? " " : ("" + Utils.IntToChar(getValue()) + "");
    }
}
