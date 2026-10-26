package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;


public record VarExp(String key) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws ExpressionException {
        try {
            return symTable.lookup(key);
        } catch (ADTException e) {
            throw new ExpressionException("Variable " + key + " is not defined!");
        }
    }

    @Override
    public String toString() {
        return key;
    }
}