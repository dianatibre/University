package ro.ubb.socket.common.domain;

import java.io.Serializable;
import java.util.Optional;

public class OptionalObj<T> implements Serializable {
    T data = null;

    public OptionalObj(Optional<T> optional) {
        optional.ifPresent(value -> data = value);
    }

    public T getValue() {
        return data;
    }

    @Override
    public String toString() {
        return "OptionalObj{" +
                "data=" + data +
                '}';
    }
}
