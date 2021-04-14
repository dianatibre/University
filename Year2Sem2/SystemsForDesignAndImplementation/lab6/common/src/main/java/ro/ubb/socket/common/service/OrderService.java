package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;

import java.util.List;

public interface OrderService extends Service<Integer, Order> {
    
    List<Order> sortOrderDate();

    List<Order> filterOrder(String paymentType);

    Long numberBestRestaurants();

    List<Restaurant> bestRestaurants();

    Long numberMostDevotedClients();

    List<Client> mostDevotedClients();
}
