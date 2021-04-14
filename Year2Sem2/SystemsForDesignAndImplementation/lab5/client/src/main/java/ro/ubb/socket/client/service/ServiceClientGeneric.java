package ro.ubb.socket.client.service;

import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ServiceClientGeneric<ID,T extends BaseEntity<ID>> implements Service<ID,T> {
    protected ExecutorService executorService;
    protected TcpClient tcpClient;

    @Override
    public CompletableFuture<Optional<T>> add(T t) throws ValidatorException {
        return null;
    }

    @Override
    public CompletableFuture<List<T>> get() {
        return null;
    }

    @Override
    public CompletableFuture<Optional<T>> delete(ID id) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<T>> update(T t) throws ValidatorException {
        return null;
    }

    @Override
    public CompletableFuture<List<T>> sort() {
        return null;
    }

    @Override
    public void emptyList() {

    }
}
