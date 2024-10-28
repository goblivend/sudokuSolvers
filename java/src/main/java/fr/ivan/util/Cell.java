package fr.ivan.util;

import static fr.ivan.util.Utils.IntToChar;
import static fr.ivan.util.Utils.getAlphabet;

public abstract class Cell<T> {

    protected T _value;
    protected int _size;

    public abstract Integer getValue();

    public abstract void setValue(Integer value);
    @Override
    public String toString() {
        return toString(getAlphabet(_size));
    }
    public String toString(String alphabet) {
        return "" + IntToChar(alphabet, getValue()) ;
    }
}
