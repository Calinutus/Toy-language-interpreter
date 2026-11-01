package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IStmt originalProgram;

    public PrgState(IStack<IStmt> stk, IDictionary<String, IValue> symblTbl, IList<IValue> ot, IDictionary<StringValue, BufferedReader> ftbl,IStmt prg) {
        this.exeStack = stk;
        this.symTable = symblTbl;
        this.out = ot;
        this.fileTable = ftbl;
        this.originalProgram = prg;
        stk.push(prg);
    }

    public static PrgState of(IStmt program) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                program
        );
    }
    public IStack<IStmt> getExeStack() {
        return exeStack;
    }
    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }
    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }
    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public IDictionary<StringValue , BufferedReader> getFileTable(){
        return fileTable;
    }

    public void setFileTable(IDictionary<StringValue , BufferedReader> fileTable){ this.fileTable = fileTable;}

    public IStmt getOriginalProgram() {
        return originalProgram;
    }
    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }

    @Override
    public String toString() {
        return "ExeStack:\n" + exeStack.toString() +
                "\nSymTable:\n" + symTable.toString() +
                "\nOut:\n" + out.toString() +
                "\nFileTable:\n" + fileTable.toString() + "\n";
    }
}