package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T>{
    private List<T> list = new ArrayList<>();

    @Override
    public void add(T elem){
        list.add(elem);
    }

    @Override
    public T get(int index) throws ADTException{
        if(index < 0 || index >= list.size()) throw new ADTException("Index" + index + "not in the list");
        return list.get(index);
    }

    @Override
    public int size(){
        return list.size();
    }

    @Override
    public boolean isEmpty(){
        return list.isEmpty();
    }


    @Override
    public List<T> getContent(){
        return list;
    }

    @Override
    public String toString(){
        return list.toString();
    }
}
