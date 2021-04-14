package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.service.AddressService;
import ro.ubb.socket.common.service.OrderService;
import ro.ubb.socket.server.repository.dbRepository.AddressDbRepo;
import ro.ubb.socket.server.repository.dbRepository.ClientDbRepo;
import ro.ubb.socket.server.repository.dbRepository.OrderDbRepo;
import ro.ubb.socket.server.repository.dbRepository.RestaurantDbRepo;

import static java.util.stream.Collectors.counting;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OrderServiceImpl extends ServerServiceGeneric<Integer, Order> implements OrderService {
    private ClientDbRepo clientDbRepo;
    private RestaurantDbRepo restaurantDbRepo;

    public OrderServiceImpl(OrderDbRepo repository, ExecutorService ex, ClientDbRepo clientDbRepo, RestaurantDbRepo restaurantDbRepo) {
        this.repository = repository;
        this.executorService = ex;
        this.clientDbRepo = clientDbRepo;
        this.restaurantDbRepo = restaurantDbRepo;
    }

    @Override
    public List<Order> sortOrderDate() {
        Comparator<Order> func = Comparator.comparing(Order::getDate, Date::compareTo);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).sorted(func).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> filterOrder(String paymentType) {
        Predicate<Order> filterO = c -> c.getPaymentType().equals(paymentType);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).filter(filterO).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the number of orders the best restaurants have
     *
     * @return Long
     */
    @Override
    public Long numberBestRestaurants() {
        try {
            return executorService.submit(() -> {
                return Collections.max(StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(e -> e.getRestaurantID(), counting())).values());
            }).get();
        } catch (Exception e) {
            return 0l;
        }
    }

    /**
     * Returns the list of the best restaurants
     *
     * @return the list.
     */
    @Override
    public List<Restaurant> bestRestaurants() {
        try {
            return executorService.submit(() -> {
                Map<Integer, Long> map = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(e -> e.getRestaurantID(), counting()));
                List<Integer> list = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(e -> e.getRestaurantID(), counting())).entrySet().stream()
                        .filter(k -> k.getValue() == Collections.max(map.values()))
                        .map(k -> k.getKey()).collect(Collectors.toList());
                return StreamSupport.stream(this.restaurantDbRepo.findAll().spliterator(), false).
                        filter(x -> list.contains(x.getId())).collect(Collectors.toList());
            }).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the most devoted clients
     *
     * @return Long
     */
    @Override
    public Long numberMostDevotedClients() {
        try {
            return executorService.submit(() -> {
                return Collections.max(StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(e -> e.getClientID(), counting())).values());
            }).get();
        } catch (Exception e) {
            return 0l;
        }
    }

    /**
     * Returns the list of the most devoted clients
     *
     * @return the list.
     */
    @Override
    public List<Client> mostDevotedClients() {
        try {
            return executorService.submit(() -> {
                Map<Integer, Long> map = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(e -> e.getClientID(), counting()));
                List<Integer> list = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(e -> e.getClientID(), counting())).entrySet().stream()
                        .filter(k -> k.getValue() == Collections.max(map.values()))
                        .map(k -> k.getKey()).collect(Collectors.toList());
                return StreamSupport.stream(this.clientDbRepo.findAll().spliterator(), false).
                        filter(x -> list.contains(x.getId())).collect(Collectors.toList());
            }).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}