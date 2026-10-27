package interpreter;

import interpreter.controller.Controller;
import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    void testExample1() throws MyException {
        // Ex1: int v; v = 2; print(v)
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));

        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), ex1);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo);

        controller.allStep();

        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void testExample2() throws MyException {
        // Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)
        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a",
                                new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)),
                                        new ArithExp(ArithOp.MUL, new ValueExp(new IntValue(3)),
                                                new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",
                                        new ArithExp(ArithOp.ADD, new VarExp("a"),
                                                new ValueExp(new IntValue(1)))),
                                        new PrintStmt(new VarExp("b"))))));

        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), ex2);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo);

        controller.allStep();

        assertEquals(new IntValue(17), state.getSymTable().lookup("a"));
        assertEquals(new IntValue(18), state.getSymTable().lookup("b"));
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void testExample3() throws MyException {
        // Ex3: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),
                                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))))));

        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), ex3);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo);

        controller.allStep();

        assertEquals(new BoolValue(true), state.getSymTable().lookup("a"));
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void testDivisionByZeroThrowsException() {
        // int v; v = 4 / 0; print(v)
        IStmt divisionByZero = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ArithExp(ArithOp.DIV,
                                new ValueExp(new IntValue(4)),
                                new ValueExp(new IntValue(0)))),
                        new PrintStmt(new VarExp("v"))
                )
        );

        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), divisionByZero);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo);

        assertThrows(MyException.class, () -> {controller.allStep();});
    }

    @Test
    void testOneStepEmptyStackThrowsException() throws ADTException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();

        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo);

        assertThrows(MyException.class, () -> controller.oneStep(state));
    }
}