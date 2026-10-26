package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public class IntType implements IType {
    @Override
    public boolean equals(Object anotherType) {
        return anotherType instanceof IntType;
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }

    @Override
    public String toString() {
        return "int";
    }
}