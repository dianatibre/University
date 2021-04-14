package model.adt;

import model.statement.IStmt;

public interface MyIStack<T> {
    T pop();
    void push(T elem);
    boolean isEmpty();
    String toString();
}
