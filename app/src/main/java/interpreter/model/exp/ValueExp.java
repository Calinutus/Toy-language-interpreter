package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

public record ValueExp(IValue val) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws ExpressionException {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}