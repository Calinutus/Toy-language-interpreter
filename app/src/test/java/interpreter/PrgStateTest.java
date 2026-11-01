package interpreter;

import interpreter.model.adt.*;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;

public class PrgStateTest {

    @Test
    void testPrgStateInitialization() {
        IStack<IStmt> stack = new MyStack<>();
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IList<IValue> out = new MyList<>();
        IDictionary<StringValue, BufferedReader> fileTable = new MyDictionary<>();

        IStmt dummyStmt = state -> state;

        PrgState state = new PrgState(stack, symTable, out, fileTable, dummyStmt);

        assertFalse(state.getExeStack().isEmpty());
        assertNotNull(state.getSymTable());
        assertNotNull(state.getOut());
        assertNotNull(state.getFileTable());
        assertEquals(dummyStmt, state.getOriginalProgram());
    }

    @Test
    void testPrgStateOfFactory() {
        IStmt dummyStmt = state -> state;
        PrgState state = PrgState.of(dummyStmt);

        assertFalse(state.getExeStack().isEmpty());
        assertNotNull(state.getSymTable());
        assertNotNull(state.getOut());
        assertNotNull(state.getFileTable());
        assertEquals(dummyStmt, state.getOriginalProgram());
    }
}