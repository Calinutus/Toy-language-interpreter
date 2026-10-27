package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    private List<PrgState> repo;

    public Repository(PrgState initialState){
        this.repo = new ArrayList<>();
        this.repo.add(initialState);
    }

    @Override
    public PrgState getCurrentPrg(){
        return this.repo.get(0);
    }

    @Override
    public void add(PrgState state){
        this.repo.add(state);
    }
}
