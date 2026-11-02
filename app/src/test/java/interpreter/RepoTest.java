package interpreter;

import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RepoTest {

    @TempDir
    Path dir;

    @Test
    void testGetCrtPrg() {
        PrgState initialState = PrgState.of(new NopStmt());
        IRepository repo = new Repository(initialState, dir.resolve("log1.txt").toString());
        assertEquals(initialState, repo.getCurrentPrg());
    }

    @Test
    void testAddState(){
        PrgState initialState = PrgState.of(new NopStmt());
        IRepository repo = new Repository(initialState, dir.resolve("log2.txt").toString());

        PrgState secondState = PrgState.of(new NopStmt());

        repo.add(secondState);
        assertEquals(initialState, repo.getCurrentPrg());
    }
}