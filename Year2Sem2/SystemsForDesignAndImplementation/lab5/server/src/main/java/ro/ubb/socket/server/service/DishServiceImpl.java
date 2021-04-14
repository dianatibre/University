package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.DishService;
import ro.ubb.socket.server.repository.dbRepository.DishDbRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DishServiceImpl extends ServerServiceGeneric<Integer, Dish> implements DishService {

    public DishServiceImpl(DishDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public Future<Optional<Dish>> add(Dish t) throws ValidatorException {
        return executorService.submit(() -> repository.save(t));
    }

    @Override
    public Future<List<Dish>> sort() {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false)
                .distinct()
                .sorted(Comparator.comparing(Dish::getName))
                .collect(Collectors.toList()));
    }

    @Override
    public void emptyList() {
        executorService.submit(() -> repository.emptyTable());
    }

    public Future<Set<Dish>> filterDishes(String category) {
        return executorService.submit(() -> StreamSupport.stream(repository.findAll().spliterator(), false).filter(a -> a.getCategory().equals(category)).collect(Collectors.toSet()));
    }
}
