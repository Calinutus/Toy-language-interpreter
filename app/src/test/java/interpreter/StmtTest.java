package interpreter;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.*;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class StmtTest {

    @TempDir
    Path dir;

    @Test
    void testNopStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        IStmt nop = new NopStmt();
        PrgState resultState = nop.execute(state);
        assertSame(state, resultState);
    }

    @Test
    void testVarDeclStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
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

        PrgState testState = new PrgState(
                new MyStack<>(),
                symTable,
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                new NopStmt()
        );

        IStmt assign = new AssignStmt("v", new ValueExp(new IntValue(42)));
        assign.execute(testState);

        assertEquals(new IntValue(42), testState.getSymTable().lookup("v"));

        IStmt invalidAssign = new AssignStmt("x", new ValueExp(new IntValue(10)));
        assertThrows(MyException.class, () -> invalidAssign.execute(testState));
    }

    @Test
    void testCompStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        IStmt stmt1 = new VarDeclStmt("a", new IntType());
        IStmt stmt2 = new AssignStmt("a", new ValueExp(new IntValue(10)));
        IStmt comp = new CompStmt(stmt1, stmt2);

        comp.execute(state);

        assertEquals(stmt1, state.getExeStack().pop());
        assertEquals(stmt2, state.getExeStack().pop());
    }

    @Test
    void testPrintStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        IStmt print = new PrintStmt(new ValueExp(new IntValue(100)));
        print.execute(state);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(100), state.getOut().get(0));
    }

    @Test
    void testIfStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        IStmt thenStmt = new AssignStmt("x", new ValueExp(new IntValue(1)));
        IStmt elseStmt = new AssignStmt("x", new ValueExp(new IntValue(2)));

        IStmt ifTrue = new IfStmt(new ValueExp(new BoolValue(true)), thenStmt, elseStmt);
        ifTrue.execute(state);
        assertEquals(thenStmt, state.getExeStack().pop());

        IStmt ifFalse = new IfStmt(new ValueExp(new BoolValue(false)), thenStmt, elseStmt);
        ifFalse.execute(state);
        assertEquals(elseStmt, state.getExeStack().pop());

        IStmt ifInvalid = new IfStmt(new ValueExp(new IntValue(5)), thenStmt, elseStmt);
        assertThrows(MyException.class, () -> ifInvalid.execute(state));
    }

    @Test
    void testAssignmentTypeMismatch() {
        IStmt ex = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new AssignStmt("v", new ValueExp(new BoolValue(true)))
        );
        PrgState state = PrgState.of(ex);
        IRepository repo = new Repository(state, dir.resolve("log_stmt.txt").toString());
        Controller controller = new Controller(repo , false);

        assertThrows(MyException.class, controller::allStep);
    }

    @Test
    void testWhileConditionTruePushesStatements() throws MyException {
        IStmt body = new PrintStmt(new ValueExp(new IntValue(1)));
        WhileStmt whileStmt = new WhileStmt(new ValueExp(new BoolValue(true)), body);

        PrgState state = PrgState.of(new NopStmt());

        state.getExeStack().pop();

        whileStmt.execute(state);

        var exeStack = state.getExeStack();
        assertFalse(exeStack.isEmpty());
        assertEquals(body, exeStack.pop());
        assertEquals(whileStmt, exeStack.pop());
        assertTrue(exeStack.isEmpty());
    }

    @Test
    void testWhileConditionFalseDoesNothing() throws MyException {
        IStmt body = new PrintStmt(new ValueExp(new IntValue(1)));
        WhileStmt whileStmt = new WhileStmt(new ValueExp(new BoolValue(false)), body);

        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().pop();

        whileStmt.execute(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void testWhileConditionTypeMismatch() {
        IStmt body = new PrintStmt(new ValueExp(new IntValue(1)));
        WhileStmt whileStmt = new WhileStmt(new ValueExp(new IntValue(10)), body);

        PrgState state = PrgState.of(new NopStmt());

        assertThrows(MyException.class, () -> whileStmt.execute(state));
    }

    @Test
    void testWhileLoopZeroTimes() throws MyException {
        // v = 5; while (v < 5) { print(v); v = v + 1; }
        IStmt ex = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(5))),
                        new CompStmt(
                                new WhileStmt(
                                        new RelationalExp(Relop.LT, new VarExp("v"), new ValueExp(new IntValue(5))),
                                        new PrintStmt(new VarExp("v"))
                                ),
                                new NopStmt()
                        )
                )
        );

        PrgState state = PrgState.of(ex);
        IRepository repo = new Repository(state, dir.resolve("log_while_zero.txt").toString());
        Controller controller = new Controller(repo, false);
        controller.allStep();

        assertTrue(state.getOut().isEmpty());
    }

    @Test
    void testWhileLoopSeveralTimes() throws MyException {
        // v = 3; while (v > 0) { print(v); v = v - 1; }
        IStmt ex = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(3))),
                        new CompStmt(
                                new WhileStmt(
                                        new RelationalExp(Relop.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new AssignStmt("v", new ArithExp(
                                                        ArithOp.SUB,
                                                        new VarExp("v"),
                                                        new ValueExp(new IntValue(1))
                                                ))
                                        )
                                ),
                                new NopStmt()
                        )
                )
        );

        PrgState state = PrgState.of(ex);
        IRepository repo = new Repository(state, dir.resolve("log_while_several.txt").toString());
        Controller controller = new Controller(repo, false);
        controller.allStep();


        var out = state.getOut();
        assertEquals(3, out.size());
        assertEquals(new IntValue(3), out.get(0));
        assertEquals(new IntValue(2), out.get(1));
        assertEquals(new IntValue(1), out.get(2));
    }
}