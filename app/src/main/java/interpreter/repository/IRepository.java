package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

public interface IRepository {
    PrgState getCurrentPrg();
    void add(PrgState state);
    void logPrgStateExec() throws MyException;
}