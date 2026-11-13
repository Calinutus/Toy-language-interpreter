package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public record WriteHeapStmt(String varName, IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();
        if (!symTable.isDefined(varName)) {
            throw new StatementException("wH: variable " + varName + " is not declared");
        }

        if (!(symTable.lookup(varName) instanceof RefValue refVal)) {
            throw new StatementException("wH: variable " + varName + " is not a reference");
        }

        int address = refVal.address();
        if (!heap.isDefined(address)) {
            throw new StatementException("wH: address " + address + " is not defined in the heap");
        }

        IValue evaluatedVal = exp.eval(symTable, heap);

        if (!evaluatedVal.getType().equals(refVal.locationType())) {
            throw new StatementException("wH: type " + evaluatedVal.getType() +
                    " does not match location type " + refVal.locationType());
        }

        heap.update(address, evaluatedVal);

        return state;
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + exp + ")";
    }
}