package interpreter;

import interpreter.controller.Controller;
import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
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
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @TempDir
    Path dir;

    private PrgState run(IStmt stmt) throws MyException {
        PrgState state = PrgState.of(stmt);
        var repo = new Repository(state , dir.resolve("log.txt").toString());
        new Controller(repo , false).allStep();
        return state;
    }

    @Test
    void testExample1() throws MyException {
        // Ex1: int v; v = 2; print(v)
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));

        PrgState state = PrgState.of(ex1);
        IRepository repo = new Repository(state, dir.resolve("log1.txt").toString());
        Controller controller = new Controller(repo , false);

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

        PrgState state = PrgState.of(ex2);
        IRepository repo = new Repository(state, dir.resolve("log2.txt").toString());
        Controller controller = new Controller(repo , false);

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

        PrgState state = PrgState.of(ex3);
        IRepository repo = new Repository(state, dir.resolve("log3.txt").toString());
        Controller controller = new Controller(repo , false);

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

        PrgState state = PrgState.of(divisionByZero);
        IRepository repo = new Repository(state, dir.resolve("log_div.txt").toString());
        Controller controller = new Controller(repo , false);

        assertThrows(MyException.class, () -> {controller.allStep();});
    }

    @Test
    void testOneStepEmptyStackThrowsException() throws ADTException {
        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().pop();

        IRepository repo = new Repository(state, dir.resolve("log_empty.txt").toString());
        Controller controller = new Controller(repo , false);


        assertThrows(MyException.class, () -> controller.oneStep(state));
    }

    @Test
    void  fileExampleReadsBothNumbersAndClosesTheFile() throws Exception{
        Path in = dir.resolve("test.in");
        Files.writeString(in , "15\n 50\n");
        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(new IntValue(15), s.getOut().get(0));
        assertEquals(new IntValue(50) , s.getOut().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception{
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:") , b = log.indexOf("SymTable:"), c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }
}
