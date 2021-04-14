package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.service.AddressService;
import ro.ubb.socket.server.repository.dbRepository.AddressDbRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddressServiceImpl extends ServerServiceGeneric<Integer, Address> implements AddressService {
    public AddressServiceImpl(AddressDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public List<Address> sortAddressCity() {
        Comparator<Address> func = Comparator.comparing(Address::getCity, String::compareTo);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).sorted(func).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Address> filterAddressCity(String city) {
        Predicate<Address> filterA = c -> c.getCity().equals(city);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).filter(filterA).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
