package interpreter;

import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.NopStmt;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RepoTest {
    @Test
    void testGetCrtPrg() {
        PrgState initialState = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        IRepository repo = new Repository(initialState);
        assertEquals(initialState, repo.getCurrentPrg());
    }

    @Test
    void testAddState(){
        PrgState initialState = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        IRepository repo = new Repository(initialState);

        PrgState secondState = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());

        repo.add(secondState);
        assertEquals(initialState , repo.getCurrentPrg());
    }


}
