package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record WhileStmt(IExp cond , IStmt body) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue val = cond.eval(state.getSymTable(), state.getHeap());

        if(!val.getType().equals(new BoolType())){
            throw new MyException("Conditional expression is not a boolean.");
        }

        BoolValue boolV = (BoolValue) val;
        if(boolV.val()){
            var stack = state.getExeStack();
            stack.push(this);
            stack.push(body);
        }
        return null;
    }

    @Override
    public String toString(){return "(while (" + cond + ") " + body + ")";}
}
