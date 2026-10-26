package interpreter;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrgStateTest {

    @Test
    void testPrgStateInitialization() {
        IStack<IStmt> stack = new MyStack<>();
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IList<IValue> out = new MyList<>();

        IStmt dummyStmt = state -> state;

        PrgState state = new PrgState(stack, symTable, out, dummyStmt);

        assertFalse(state.getExeStack().isEmpty());
        assertNotNull(state.getSymTable());
        assertNotNull(state.getOut());
        assertEquals(dummyStmt, state.getOriginalProgram());
    }
}