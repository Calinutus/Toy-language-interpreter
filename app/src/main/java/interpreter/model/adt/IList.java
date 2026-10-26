package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.List;

public interface IList<T> {
    void add(T elem);
    T get(int index) throws ADTException;
    int size();
    boolean isEmpty();
    List<T> getContent();
}
