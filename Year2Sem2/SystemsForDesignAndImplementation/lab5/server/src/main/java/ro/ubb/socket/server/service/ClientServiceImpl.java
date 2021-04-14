package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.ClientService;
import ro.ubb.socket.server.repository.dbRepository.ClientDbRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientServiceImpl extends ServerServiceGeneric<Integer, Client> implements ClientService {

    public ClientServiceImpl(ClientDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public Future<Optional<Client>> add(Client t) throws ValidatorException {
        return executorService.submit(() -> repository.save(t));
    }

    @Override
    public Future<List<Client>> sort() {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false)
                .distinct()
                .sorted(Comparator.comparing(Client::getName))
                .collect(Collectors.toList()));
    }

    @Override
    public void emptyList() {
        executorService.submit(() -> repository.emptyTable());
    }

    public Future<Set<Client>> filterClients(Integer age) {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false).filter(a -> a.getAge() == age).collect(Collectors.toSet()));
    }
}
