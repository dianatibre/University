package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.service.RestaurantService;
import ro.ubb.socket.server.repository.dbRepository.RestaurantDbRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RestaurantServiceImpl extends ServerServiceGeneric<Integer, Restaurant> implements RestaurantService {
    public RestaurantServiceImpl(RestaurantDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public List<Restaurant> sortRestaurantsRating() {
        Comparator<Restaurant> func = Comparator.comparing(Restaurant::getRating, Integer::compareTo);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).sorted(func).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Restaurant> filterRestaurantsRating(Integer rating) {
        Predicate<Restaurant> filterA = c -> c.getRating().equals(rating);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).filter(filterA).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
