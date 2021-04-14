package ro.ubb.socket.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class DishService extends ServiceClientGeneric<Integer, Dish> implements ro.ubb.socket.common.service.DishService {

    @Autowired
    private ro.ubb.socket.common.service.DishService dishService;

    public DishService(ExecutorService ex) {
        this.executorService = ex;
    }

    @Override
    public OptionalObj<Dish> add(Dish dish) throws ValidatorException {
        return dishService.add(dish);
    }

    @Override
    public List<Dish> get() {
        return dishService.get();
    }

    @Override
    public OptionalObj<Dish> delete(Integer integer) {
        return dishService.delete(integer);
    }

    @Override
    public OptionalObj<Dish> update(Dish dish) throws ValidatorException {
        return dishService.update(dish);
    }

    public CompletableFuture<OptionalObj<Dish>> myDelete(Integer id) {
        return CompletableFuture.supplyAsync(() -> delete(id), executorService);
    }

    public CompletableFuture<OptionalObj<Dish>> myUpdate(Dish dish) {
        return CompletableFuture.supplyAsync(() -> update(dish), executorService);
    }

    public CompletableFuture<List<Dish>> myGet() {
        return CompletableFuture.supplyAsync(this::get, executorService);
    }

    public CompletableFuture<OptionalObj<Dish>> myAdd(Dish dish) {
        return CompletableFuture.supplyAsync(() -> add(dish), executorService);
    }

    @Override
    public List<Dish> sortDishCategory() {
        return dishService.sortDishCategory();
    }

    @Override
    public List<Dish> filterDishCategory(String category) {
        return dishService.filterDishCategory(category);
    }
}
