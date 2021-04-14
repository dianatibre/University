package ro.ubb.socket.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class RestaurantService extends ServiceClientGeneric<Integer, Restaurant> implements ro.ubb.socket.common.service.RestaurantService {

    @Autowired
    private ro.ubb.socket.common.service.RestaurantService restaurantService;

    public RestaurantService(ExecutorService ex) {
        this.executorService = ex;
    }

    @Override
    public OptionalObj<Restaurant> add(Restaurant restaurant) throws ValidatorException {
        return restaurantService.add(restaurant);
    }

    @Override
    public List<Restaurant> get() {
        return restaurantService.get();
    }

    @Override
    public OptionalObj<Restaurant> delete(Integer id) {
        return restaurantService.delete(id);
    }

    @Override
    public OptionalObj<Restaurant> update(Restaurant restaurant) throws ValidatorException {
        return restaurantService.update(restaurant);
    }

    public CompletableFuture<OptionalObj<Restaurant>> myDelete(Integer id) {
        return CompletableFuture.supplyAsync(() -> delete(id), executorService);
    }

    public CompletableFuture<OptionalObj<Restaurant>> myUpdate(Restaurant restaurant) {
        return CompletableFuture.supplyAsync(() -> update(restaurant), executorService);
    }

    public CompletableFuture<List<Restaurant>> myGet() {
        return CompletableFuture.supplyAsync(this::get, executorService);
    }

    public CompletableFuture<OptionalObj<Restaurant>> myAdd(Restaurant restaurant) {
        return CompletableFuture.supplyAsync(() -> add(restaurant), executorService);
    }

    @Override
    public List<Restaurant> sortRestaurantsRating() {
        return restaurantService.sortRestaurantsRating();
    }

    @Override
    public List<Restaurant> filterRestaurantsRating(Integer rating) {
        return restaurantService.filterRestaurantsRating(rating);
    }
}