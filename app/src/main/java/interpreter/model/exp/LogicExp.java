package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record LogicExp(LogicOp op, IExp left, IExp right) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        IValue v1 = left.eval(symTable);
        if (!v1.getType().equals(new BoolType()))
            throw new ExpressionException("first operand is not a boolean");
        IValue v2 = right.eval(symTable);
        if (!v2.getType().equals(new BoolType()))
            throw new ExpressionException("second operand is not a boolean");

        BoolValue b1 = (BoolValue) v1;
        BoolValue b2 = (BoolValue) v2;
        boolean n1 = b1.val();
        boolean n2 = b2.val();

        return switch (op) {
            case AND -> new BoolValue(n1 && n2);
            case OR -> new BoolValue(n1 || n2);
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}