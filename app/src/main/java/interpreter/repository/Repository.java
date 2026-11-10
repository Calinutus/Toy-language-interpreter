package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> states = new ArrayList<>();
    private final String logFilePath;

    public Repository(PrgState initialState, String logFilePath) {
        states.add(initialState);
        this.logFilePath = logFilePath;
    }

    @Override
    public PrgState getCurrentPrg() {
        return states.getFirst();
    }

    @Override
    public void add(PrgState state) {
        states.add(state);
    }

    @Override
    public void logPrgStateExec() throws MyException {
        try (var logFile = new PrintWriter(
                new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println(getCurrentPrg().toLogString());
        } catch (IOException e) {
            throw new MyException("cannot write the log file " + logFilePath + ": " + e.getMessage());
        }
    }
}