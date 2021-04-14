package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.RestaurantService;
import ro.ubb.socket.server.repository.dbRepository.RestaurantDbRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RestaurantServiceImpl extends ServerServiceGeneric<Integer, Restaurant> implements RestaurantService {

    public RestaurantServiceImpl(RestaurantDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public Future<Optional<Restaurant>> add(Restaurant t) throws ValidatorException {
        return executorService.submit(() -> repository.save(t));
    }

    @Override
    public Future<List<Restaurant>> sort() {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false)
                .distinct()
                .sorted(Comparator.comparing(Restaurant::getRating))
                .collect(Collectors.toList()));
    }

    @Override
    public void emptyList() {
        executorService.submit(() -> repository.emptyTable());
    }


    public Future<Set<Restaurant>> filterRestaurants(Integer rating) {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false).filter(a -> a.getRating().equals(rating)).collect(Collectors.toSet()));
    }
}
