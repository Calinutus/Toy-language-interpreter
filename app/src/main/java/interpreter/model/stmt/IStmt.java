package interpreter.model.stmt;
import interpreter.exceptions.ADTException;
import interpreter.model.PrgState;

public interface IStmt {
    PrgState execute(PrgState state) throws ADTException;
}
