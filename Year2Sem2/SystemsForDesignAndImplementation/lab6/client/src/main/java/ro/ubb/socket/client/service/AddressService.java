package ro.ubb.socket.client.service;

import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AddressService extends ServiceClientGeneric<Integer, Address> implements ro.ubb.socket.common.service.AddressService {

    @Autowired
    private ro.ubb.socket.common.service.AddressService addressService;

    public AddressService(ExecutorService ex) {
        this.executorService = ex;
    }

    @Override
    public OptionalObj<Address> add(Address address) throws ValidatorException {
        return addressService.add(address);
    }

    @Override
    public List<Address> get() {
        return addressService.get();
    }

    @Override
    public OptionalObj<Address> delete(Integer id) {
        return addressService.delete(id);
    }

    public CompletableFuture<OptionalObj<Address>> myDelete(Integer id) {
        return CompletableFuture.supplyAsync(() -> delete(id), executorService);
    }

    public CompletableFuture<OptionalObj<Address>> myUpdate(Address address) {
        return CompletableFuture.supplyAsync(() -> update(address), executorService);
    }

    public CompletableFuture<List<Address>> myGet() {
        return CompletableFuture.supplyAsync(this::get, executorService);
    }

    public CompletableFuture<OptionalObj<Address>> myAdd(Address address) {
        return CompletableFuture.supplyAsync(() -> add(address), executorService);
    }

    @Override
    public OptionalObj<Address> update(Address address) throws ValidatorException {
        return addressService.update(address);
    }

    @Override
    public List<Address> sortAddressCity() {
        return addressService.sortAddressCity();
    }

    @Override
    public List<Address> filterAddressCity(String city) {
        return addressService.filterAddressCity(city);
    }
}
