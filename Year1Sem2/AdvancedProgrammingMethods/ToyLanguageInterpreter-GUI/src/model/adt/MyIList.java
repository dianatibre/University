package model.adt;

import model.PrgState;

public interface MyIList<T> {
    void add(T elem);
    PrgState pop();
    String toString();

    int size();
    boolean isEmpty();
}
