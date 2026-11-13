package interpreter;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.stmt.WriteHeapStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HeapOperationsTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
    }

    @Test
    void testNewStmtTypeMismatch(){
        assertThrows(MyException.class,()->{new NewStmt("v", new ValueExp(new BoolValue(true))).execute(state);});
    }

    @Test
    void  testReadHeapExpSuccess() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(100))).execute(state);
        ReadHeapExp rh = new ReadHeapExp(new VarExp("v"));

        assertEquals(new IntValue(100) , rh.eval(state.getSymTable() , state.getHeap()));
    }

    @Test
    void testReadHeapUndefinedAddress() throws MyException {
        state.getSymTable().update("v" , new RefValue(999 , new IntType()));
        ReadHeapExp rh = new ReadHeapExp(new VarExp("v"));

        assertThrows(MyException.class,()-> rh.eval(state.getSymTable() , state.getHeap()));
    }

    @Test
    void testWriteHeapExpSuccess() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(10))).execute(state);
        new WriteHeapStmt("v", new ValueExp(new IntValue(50))).execute(state);

        RefValue ref = (RefValue) state.getSymTable().lookup("v");
        assertEquals(new IntValue(50) , state.getHeap().get(ref.address()));
    }

    @Test
    void testWriteHeapMismatch() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(10))).execute(state);
        assertThrows(MyException.class,()-> new WriteHeapStmt("v", new ValueExp(new BoolValue(true))).execute(state));
    }

    @Test
    void testWriteHeapUndeclaredVariable() throws MyException {
        assertThrows(MyException.class, () -> new WriteHeapStmt("notEx", new ValueExp(new IntValue(10))).execute(state));
    }
}
