package fr.ivan.sudoku.util;

public abstract class Cell<T> {

    protected T _value;
    protected int _size;

    public abstract Integer getValue();

    public abstract void setValue(Integer value);

    public String toString(String alphabet) {
        return "" + Utils.IntToChar(alphabet, getValue()) ;
    }
    @Override
    public String toString() {
        return "" + Utils.IntToChar(Utils.getAlphabet(_size), getValue()) ;
    }
}
