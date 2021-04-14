package ro.ubb.socket.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.socket.common.domain.*;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class OrderService extends ServiceClientGeneric<Integer, Order> implements ro.ubb.socket.common.service.OrderService {

    @Autowired
    private ro.ubb.socket.common.service.OrderService orderService;

    public OrderService(ExecutorService ex) {
        this.executorService = ex;
    }

    @Override
    public OptionalObj<Order> add(Order order) throws ValidatorException {
        return orderService.add(order);
    }

    @Override
    public List<Order> get() {
        return orderService.get();
    }

    @Override
    public OptionalObj<Order> delete(Integer id) {
        return orderService.delete(id);
    }

    @Override
    public OptionalObj<Order> update(Order order) throws ValidatorException {
        return orderService.update(order);
    }

    public CompletableFuture<OptionalObj<Order>> myDelete(Integer id) {
        return CompletableFuture.supplyAsync(() -> delete(id), executorService);
    }

    public CompletableFuture<OptionalObj<Order>> myUpdate(Order order) {
        return CompletableFuture.supplyAsync(() -> update(order), executorService);
    }

    public CompletableFuture<List<Order>> myGet() {
        return CompletableFuture.supplyAsync(this::get, executorService);
    }

    public CompletableFuture<OptionalObj<Order>> myAdd(Order order) {
        return CompletableFuture.supplyAsync(() -> add(order), executorService);
    }

    @Override
    public List<Order> sortOrderDate() {
        return orderService.sortOrderDate();
    }

    @Override
    public List<Order> filterOrder(String paymentType) {
        return orderService.filterOrder(paymentType);
    }

    @Override
    public Long numberBestRestaurants() {
        return orderService.numberBestRestaurants();
    }

    @Override
    public List<Restaurant> bestRestaurants() {
        return orderService.bestRestaurants();
    }

    @Override
    public Long numberMostDevotedClients() {
        return orderService.numberMostDevotedClients();
    }

    @Override
    public List<Client> mostDevotedClients() {
        return orderService.mostDevotedClients();
    }
}
