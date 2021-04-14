package ro.ubb.socket.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ClientService extends ServiceClientGeneric<Integer, Client> implements ro.ubb.socket.common.service.ClientService {

    @Autowired
    private ro.ubb.socket.common.service.ClientService clientService;

    public ClientService(ExecutorService ex) {
        this.executorService = ex;
    }

    @Override
    public OptionalObj<Client> add(Client client) throws ValidatorException {
        return clientService.add(client);
    }

    @Override
    public List<Client> get() {
        return clientService.get();
    }

    @Override
    public OptionalObj<Client> delete(Integer integer) {
        return clientService.delete(integer);
    }

    @Override
    public OptionalObj<Client> update(Client client) throws ValidatorException {
        return clientService.update(client);
    }

    @Override
    public List<Client> sortClientAge() {
        return clientService.sortClientAge();
    }

    @Override
    public List<Client> filterClientAge(int age) {
        return clientService.filterClientAge(age);
    }

    public CompletableFuture<OptionalObj<Client>> myDelete(Integer id) {
        return CompletableFuture.supplyAsync(() -> delete(id), executorService);
    }

    public CompletableFuture<OptionalObj<Client>> myUpdate(Client client) {
        return CompletableFuture.supplyAsync(() -> update(client), executorService);
    }

    public CompletableFuture<List<Client>> myGet() {
        return CompletableFuture.supplyAsync(this::get, executorService);
    }

    public CompletableFuture<OptionalObj<Client>> myAdd(Client client) {
        return CompletableFuture.supplyAsync(() -> add(client), executorService);
    }
}
