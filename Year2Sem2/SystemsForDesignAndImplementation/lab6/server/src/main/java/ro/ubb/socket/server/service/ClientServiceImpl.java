package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.service.ClientService;
import ro.ubb.socket.server.repository.dbRepository.ClientDbRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientServiceImpl extends ServerServiceGeneric<Integer, Client> implements ClientService {

    public ClientServiceImpl(ClientDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public List<Client> sortClientAge() {
        Comparator<Client> func = Comparator.comparing(Client::getAge, Integer::compareTo);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).sorted(func).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Client> filterClientAge(int age) {
        Predicate<Client> filterA = c -> c.getAge() == (age);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).filter(filterA).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
