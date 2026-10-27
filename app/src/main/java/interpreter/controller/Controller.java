package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private IRepository repo;
    private boolean displayFlag = false;

    public Controller(IRepository repo) {
        this.repo = repo;
    }

    public void setDisplayFlag(boolean value) {
        this.displayFlag = value;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty()) {
            throw new StatementException("program state stack is empty");
        }
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyException {
        PrgState prg = repo.getCurrentPrg();
        displayCurrentState(prg);
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            displayCurrentState(prg);
        }
    }

    private void displayCurrentState(PrgState state) {
        if (displayFlag) {
            System.out.println(state.toString());
            System.out.println("----------------------------------------");
        }
    }

    public IRepository getRepo() {
        return repo;
    }
}