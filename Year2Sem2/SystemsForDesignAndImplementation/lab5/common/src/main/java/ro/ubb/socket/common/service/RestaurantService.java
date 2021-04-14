package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Restaurant;

public interface RestaurantService extends Service<Integer, Restaurant> {

    String ADD_RESTAURANT="addRestaurant";
    String GET_RESTAURANT="getRestaurants";
    String DELETE_RESTAURANT="deleteRestaurant";
    String UPDATE_RESTAURANT="updateRestaurant";
    String SORT_RESTAURANT="sortRestaurant";
    String FILTER_RESTAURANT="filterRestaurant";
}
