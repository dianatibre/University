package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.AddressService;
import ro.ubb.socket.server.repository.dbRepository.AddressDbRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddressServiceImpl extends ServerServiceGeneric<Integer, Address> implements AddressService {
    public AddressServiceImpl(AddressDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public Future<Optional<Address>> add(Address t) throws ValidatorException {
        return executorService.submit(() -> repository.save(t));
    }

    @Override
    public Future<List<Address>> sort() {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false)
                .distinct()
                .sorted(Comparator.comparing(Address::getCity))
                .collect(Collectors.toList()));
    }

    @Override
    public void emptyList() {
        executorService.submit(() -> repository.emptyTable());
    }

    public Future<Set<Address>> filterAddresses(String city) {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false).filter(a -> a.getCity().equals(city)).collect(Collectors.toSet()));
    }
}
