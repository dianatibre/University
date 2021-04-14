package ro.ubb.socket.client.service;

import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class ServiceClientGeneric<ID, T extends BaseEntity<ID>> implements Service<ID, T> {
    protected ExecutorService executorService;

    @Override
    public OptionalObj<T> add(T t) throws ValidatorException {
        return null;
    }

    @Override
    public List<T> get() {
        return null;
    }

    @Override
    public OptionalObj<T> delete(ID id) {
        return null;
    }

    @Override
    public OptionalObj<T> update(T t) throws ValidatorException {
        return null;
    }
}
