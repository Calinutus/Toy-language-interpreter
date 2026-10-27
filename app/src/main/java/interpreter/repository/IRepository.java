package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

import java.io.IOException;

public interface IRepository {
    PrgState getCurrentPrg();
    void add(PrgState state);
}
