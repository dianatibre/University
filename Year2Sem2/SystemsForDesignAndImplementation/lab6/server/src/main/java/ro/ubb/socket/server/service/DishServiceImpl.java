package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.service.DishService;
import ro.ubb.socket.server.repository.dbRepository.DishDbRepo;
import ro.ubb.socket.server.repository.dbRepository.OrderDbRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DishServiceImpl extends ServerServiceGeneric<Integer, Dish> implements DishService {

    public DishServiceImpl(DishDbRepo repository, ExecutorService ex) {
        this.repository = repository;
        this.executorService = ex;
    }

    @Override
    public List<Dish> sortDishCategory() {
        Comparator<Dish> func = Comparator.comparing(Dish::getCategory, String::compareTo);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).sorted(func).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    @Override
    public List<Dish> filterDishCategory(String category) {
        Predicate<Dish> filterA = c -> c.getCategory().equals(category);
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).filter(filterA).collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
