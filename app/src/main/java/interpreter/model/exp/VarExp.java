package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

public record VarExp(String key) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws ExpressionException {
        return symTable.lookup(key);
    }

    @Override
    public String toString() {
        return key;
    }
}