package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.Restaurant;

import java.util.List;
import java.util.concurrent.Future;

public interface DishService extends Service<Integer, Dish> {
    String ADD_DISH="addDish";
    String GET_DISHES="getDishes";
    String DELETE_DISH="deleteDish";
    String UPDATE_DISH="updateDish";
    String SORT_DISH="sortDish";
    String FILTER_DISH="filterDish";
}
