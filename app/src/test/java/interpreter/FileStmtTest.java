package interpreter;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {

    @TempDir
    Path dir;

    private PrgState state;
    private ValueExp file;

    @BeforeEach
    void setUp() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n");
        file = new ValueExp(new StringValue(f.toString()));
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @AfterEach
    void tearDown() {
        if (state != null && state.getFileTable() != null) {
            for (var reader : state.getFileTable().toMap().values()) {
                try {
                    reader.close();
                } catch (Exception ignored) {}
            }
        }
    }

    @Test
    void readGivesTheNextNumberThenZeroAtTheEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(file).execute(state));
    }

    @Test
    void closeFileRemovesItFromFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);
        var strVal = (StringValue) file.eval(state.getSymTable(), state.getHeap());
        assertTrue(state.getFileTable().isDefined(strVal));

        new CloseRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().isDefined(strVal));
    }

    @Test
    void readFileWhenNotOpenThrows() {
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state));
    }

    @Test
    void closeFileWhenNotOpenThrows() {
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(file).execute(state));
    }
}