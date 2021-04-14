package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;

import java.util.List;
import java.util.concurrent.Future;

public interface OrderService extends Service<Integer, Order> {
    String ADD_ORDER="addOrder";
    String GET_ORDERS="getOrders";
    String DELETE_ORDER="deleteOrder";
    String UPDATE_ORDER="updateOrder";
    String SORT_ORDER="sortOrder";
    String FILTER_ORDER="filterOrder";
    String NUMBER_BEST_RESTAURANT = "numberBestRestaurant";
    String BEST_RESTAURANT = "bestRestaurant";
    String NUMBER_MOST_DEVOTED_CLIENTS = "numberMostDevotedClients";
    String MOST_DEVOTED_CLIENTS = "mostDevotedClients";

    /**
     * Returns the number of best restaurants
     *
     * @return Long
     */
    public abstract Future<Long> numberBestRestaurants();

    /**
     * Returns the list of the best restaurants
     *
     * @return the list.
     */
    public abstract Future<List<Restaurant>> bestRestaurants();

    /**
     * Returns the number of the most devoted clients
     *
     * @return Long
     */
    public abstract Future<Long> numberMostDevotedClients();

    /**
     * Returns the list of the most devoted clients
     *
     * @return the list.
     */
    public abstract Future<List<Client>> mostDevotedClients();
}
