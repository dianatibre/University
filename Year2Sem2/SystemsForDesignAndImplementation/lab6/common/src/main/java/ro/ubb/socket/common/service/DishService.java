package ro.ubb.socket.common.service;
import ro.ubb.socket.common.domain.Dish;

import java.util.List;

public interface DishService extends  Service<Integer, Dish>{

    /**
     * The function sorts the dishes by their category.
     *
     * @return the dishes sorted after their category
     */
    List<Dish> sortDishCategory();

    /**
     * The function filters the dishes by a category.
     *
     * @param category - a category
     * @return the dishes filtered after a category
     */
    List<Dish> filterDishCategory(String category);
}
