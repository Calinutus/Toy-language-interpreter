package interpreter;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StmtTest {

    @Test
    void testNopStmt() throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        IStmt nop = new NopStmt();
        PrgState resultState = nop.execute(state);
        assertSame(state, resultState);
    }

    @Test
    void testVarDeclStmt() throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        IStmt varDecl = new VarDeclStmt("v", new IntType());
        varDecl.execute(state);

        assertTrue(state.getSymTable().isDefined("v"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));

        assertThrows(MyException.class, () -> varDecl.execute(state));
    }

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

    @Test
    void testCompStmt() throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        IStmt stmt1 = new VarDeclStmt("a", new IntType());
        IStmt stmt2 = new AssignStmt("a", new ValueExp(new IntValue(10)));
        IStmt comp = new CompStmt(stmt1, stmt2);

        comp.execute(state);

        assertEquals(stmt1, state.getExeStack().pop());
        assertEquals(stmt2, state.getExeStack().pop());
    }
}