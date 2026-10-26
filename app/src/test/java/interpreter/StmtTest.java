package interpreter;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StmtTest {
    @Test
    void testAssignStmt() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        symTable.put("v", new IntValue(0));
        PrgState testState = new PrgState(new MyStack<>(), symTable, new MyList<>(), new NopStmt());

        IStmt assign = new AssignStmt("v", new ValueExp(new IntValue(42)));
        assign.execute(testState);

        assertEquals(new IntValue(42), testState.getSymTable().lookup("v"));

        IStmt invalidAssign = new AssignStmt("x", new ValueExp(new IntValue(10)));
        assertThrows(MyException.class, () -> invalidAssign.execute(testState));
    }
}
