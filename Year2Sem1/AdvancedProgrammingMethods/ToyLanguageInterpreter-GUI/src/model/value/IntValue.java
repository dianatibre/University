package model.value;

import model.type.IntType;
import model.type.Type;

public class IntValue implements IValue {
    int val;
    public IntValue(int v){
        val=v;
    }

    public int getVal() {
        return val;
    }

    @Override
    public String toString() {
        return "" + val;
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntValue;
    }
}
