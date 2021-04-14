package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.OrderService;
import ro.ubb.socket.server.repository.dbRepository.ClientDbRepo;
import ro.ubb.socket.server.repository.dbRepository.DishDbRepo;
import ro.ubb.socket.server.repository.dbRepository.OrderDbRepo;
import ro.ubb.socket.server.repository.dbRepository.RestaurantDbRepo;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.counting;

public class OrderServiceImpl extends ServerServiceGeneric<Integer, Order> implements OrderService {
    private final ClientDbRepo clientDbRepo;
    private final DishDbRepo dishDbRepo;
    private final RestaurantDbRepo restaurantDbRepo;

    public OrderServiceImpl(ClientDbRepo clientDbRepo, DishDbRepo dishDbRepo, RestaurantDbRepo restaurantDbRepo, OrderDbRepo repository, ExecutorService ex) {
        this.clientDbRepo = clientDbRepo;
        this.dishDbRepo = dishDbRepo;
        this.restaurantDbRepo = restaurantDbRepo;
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public Future<Optional<Order>> add(Order t) throws ValidatorException {
        return executorService.submit(() -> repository.save(t));
    }

    @Override
    public Future<List<Order>> sort() {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false)
                .distinct()
                .sorted(Comparator.comparing(Order::getDate))
                .collect(Collectors.toList()));
    }

    @Override
    public void emptyList() {
        executorService.submit(() -> repository.emptyTable());
    }

    public Future<Set<Order>> filterOrders(String paymentType) {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false).filter(a -> a.getPaymentType().equals(paymentType)).collect(Collectors.toSet()));
    }

    /**
     * Returns the list of the best restaurants
     *
     * @return the list.
     */
    public Future<List<Restaurant>> bestRestaurants() {
        Map<Integer, Long> map = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getRestaurantID, counting()));
        List<Integer> list = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getRestaurantID, counting())).entrySet().stream()
                .filter(k -> k.getValue().equals(Collections.max(map.values())))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return executorService.submit(() -> StreamSupport.stream(this.restaurantDbRepo.findAll().spliterator(), false).
                filter(x -> list.contains(x.getId())).collect(Collectors.toList()));
    }

    /**
     * Returns the number of the best restaurants
     *
     * @return int
     */
    public Future<Long> numberBestRestaurants() {
        return executorService.submit(() -> Collections.max(StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getRestaurantID, counting())).values()));
    }

    /**
     * Returns the list of the most devoted clients
     *
     * @return the list.
     */
    public Future<List<Client>> mostDevotedClients() {
        Map<Integer, Long> map = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getClientID, counting()));
        List<Integer> list = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getClientID, counting())).entrySet().stream()
                .filter(k -> k.getValue().equals(Collections.max(map.values())))
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return executorService.submit(() -> StreamSupport.stream(this.clientDbRepo.findAll().spliterator(), false).
                filter(x -> list.contains(x.getId())).collect(Collectors.toList()));
    }

    /**
     * Returns the number of the most devoted clients
     *
     * @return int
     */
    public Future<Long> numberMostDevotedClients() {
        return executorService.submit(() -> Collections.max(StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getClientID, counting())).values()));
    }
}
