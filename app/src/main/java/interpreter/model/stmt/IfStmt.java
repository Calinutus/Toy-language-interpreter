package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.exp.IExp;
import interpreter.model.PrgState;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue val = cond.eval(state.getSymTable());
        if (!val.getType().equals(new BoolType())) {
            throw new StatementException("conditional expression is not a boolean");
        }

        BoolValue boolVal = (BoolValue) val;
        if (boolVal.val()) {
            state.getExeStack().push(thenS);
        } else {
            state.getExeStack().push(elseS);
        }
        return state;
    }

    @Override
    public String toString() {
        return "(IF(" + cond + ") THEN(" + thenS + ") ELSE(" + elseS + "))";
    }
}