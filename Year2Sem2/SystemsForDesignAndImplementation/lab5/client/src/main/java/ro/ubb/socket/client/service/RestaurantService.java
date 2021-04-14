package ro.ubb.socket.client.service;

import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Message;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class RestaurantService extends ServiceClientGeneric<Integer, Restaurant> implements ro.ubb.socket.common.service.RestaurantService {
    public RestaurantService(ExecutorService ex, TcpClient tc) {
        this.executorService = ex;
        this.tcpClient = tc;
    }

    @Override
    public CompletableFuture<Optional<Restaurant>> add(Restaurant restaurant) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(RestaurantService.ADD_RESTAURANT, restaurant);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Restaurant> result = null;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Restaurant> optionalObj = (OptionalObj<Restaurant>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Restaurant>> get() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(RestaurantService.GET_RESTAURANT, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            List<Restaurant> result = (List<Restaurant>) response.getBody();
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Restaurant>> delete(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(RestaurantService.DELETE_RESTAURANT, id);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Restaurant> result = null;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Restaurant> optionalObj = (OptionalObj<Restaurant>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Restaurant>> update(Restaurant restaurant) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(RestaurantService.UPDATE_RESTAURANT, restaurant);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Restaurant> result = null;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Restaurant> optionalObj = (OptionalObj<Restaurant>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Restaurant>> sort() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(RestaurantService.SORT_RESTAURANT, "sort");
            Message response = tcpClient.sendAndReceive(request);
            List<Restaurant> result = (List<Restaurant>) response.getBody();
            return result;
        }, executorService);
    }

    public CompletableFuture<Set<Restaurant>> filter(int rating) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(RestaurantService.FILTER_RESTAURANT, rating);
            Message response = tcpClient.sendAndReceive(request);
            Set<Restaurant> result = (Set<Restaurant>) response.getBody();
            return result;
        }, executorService);
    }
}
