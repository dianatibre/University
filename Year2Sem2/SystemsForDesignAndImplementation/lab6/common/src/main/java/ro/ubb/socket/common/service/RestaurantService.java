package ro.ubb.socket.common.service;
import ro.ubb.socket.common.domain.Restaurant;

import java.util.List;

public interface RestaurantService extends Service<Integer, Restaurant> {
    /**
     * The function sorts the restaurants by their rating.
     *
     * @return the restaurants sorted after their rating
     */
    List<Restaurant> sortRestaurantsRating();

    /**
     * The function filters the addresses by a rating.
     *
     * @param rating - rating number
     * @return the restaurants filtered after a rating
     */
    List<Restaurant> filterRestaurantsRating(Integer rating);
}