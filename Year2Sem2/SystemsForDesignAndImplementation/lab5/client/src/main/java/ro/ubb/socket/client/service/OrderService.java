package ro.ubb.socket.client.service;

import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Message;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class OrderService extends ServiceClientGeneric<Integer, Order> implements ro.ubb.socket.common.service.OrderService {

    public OrderService(ExecutorService ex, TcpClient tc) {
        this.executorService = ex;
        this.tcpClient = tc;
    }

    @Override
    public CompletableFuture<Optional<Order>> add(Order order) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.ADD_ORDER, order);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Order> result = null;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Order> optionalObj = (OptionalObj<Order>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Order>> get() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.GET_ORDERS, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            List<Order> result = (List<Order>) response.getBody();
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Order>> delete(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.DELETE_ORDER, id);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Order> result = null;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Order> optionalObj = (OptionalObj<Order>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<Order>> update(Order t) throws ValidatorException {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.UPDATE_ORDER, t);
            Message response = tcpClient.sendAndReceive(request);
            Optional<Order> result = null;
            if (response.getHeader().equals(Message.OK)) {
                OptionalObj<Order> optionalObj = (OptionalObj<Order>) response.getBody();
                result = Optional.ofNullable(optionalObj.getValue());
            } else {
                String err = (String) response.getBody();
                throw new ValidatorException(err);
            }
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Order>> sort() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.SORT_ORDER, "sort");
            Message response = tcpClient.sendAndReceive(request);
            List<Order> result = (List<Order>) response.getBody();
            return result;
        }, executorService);
    }

    public CompletableFuture<Set<Order>> filter(String category) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.FILTER_ORDER, category);
            Message response = tcpClient.sendAndReceive(request);
            Set<Order> result = (Set<Order>) response.getBody();
            return result;
        }, executorService);
    }

    @Override
    public CompletableFuture<Long> numberBestRestaurants() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.NUMBER_BEST_RESTAURANT, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            Long result = (Long) response.getBody();
            return result;
        });
    }

    @Override
    public CompletableFuture<List<Restaurant>> bestRestaurants() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.BEST_RESTAURANT, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            List<Restaurant> result = (List<Restaurant>) response.getBody();
            return result;
        });

    }

    @Override
    public CompletableFuture<Long> numberMostDevotedClients() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.NUMBER_MOST_DEVOTED_CLIENTS, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            Long result = (Long) response.getBody();
            return result;
        });
    }

    @Override
    public CompletableFuture<List<Client>> mostDevotedClients() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(OrderService.MOST_DEVOTED_CLIENTS, "aaa");
            Message response = tcpClient.sendAndReceive(request);
            List<Client> result = (List<Client>) response.getBody();
            return result;
        });
    }
}

