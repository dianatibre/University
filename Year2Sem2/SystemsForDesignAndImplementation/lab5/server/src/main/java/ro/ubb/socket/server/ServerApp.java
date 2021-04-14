package ro.ubb.socket.server;

import ro.ubb.socket.common.domain.*;
import ro.ubb.socket.common.domain.validators.*;
import ro.ubb.socket.common.service.*;
import ro.ubb.socket.server.repository.dbRepository.*;
import ro.ubb.socket.server.service.*;
import ro.ubb.socket.server.tcp.TcpServer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        TcpServer tcpServer = new TcpServer(executorService, Service.SERVER_PORT);
        Validator<Address> addressValidator = new AddressValidator();
        AddressDbRepo addressDbRepo = new AddressDbRepo(addressValidator);
        AddressServiceImpl addressService = new AddressServiceImpl(addressDbRepo, executorService);

        tcpServer.addHandler(
                AddressService.ADD_ADDRESS, (request) -> {
                    Address address = (Address) request.getBody();
                    Future<Optional<Address>> result = addressService.add(address);
                    try {
                        Optional<Address> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                AddressService.GET_ADDRESSES, (request) -> {
                    Future<List<Address>> result = addressService.get();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                AddressService.DELETE_ADDRESS, (request) -> {
                    Integer id = (Integer) request.getBody();
                    Future<Optional<Address>> result = addressService.delete(id);
                    try {
                        Optional<Address> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                AddressService.UPDATE_ADDRESS, (request) -> {
                    Address address = (Address) request.getBody();
                    Future<Optional<Address>> result = addressService.update(address);
                    try {
                        Optional<Address> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                AddressService.SORT_ADDRESS, (request) -> {
                    Future<List<Address>> result = addressService.sort();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                AddressService.FILTER_ADDRESS, (request) -> {
                    String city = (String) request.getBody();
                    Future<Set<Address>> result = addressService.filterAddresses(city);
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });


        //Dish

        Validator<Dish> dishValidator = new DishValidator();
        DishDbRepo dishDbRepo = new DishDbRepo(dishValidator);
        DishServiceImpl dishService = new DishServiceImpl(dishDbRepo, executorService);

        tcpServer.addHandler(
                DishService.ADD_DISH, (request) -> {
                    Dish dish = (Dish) request.getBody();
                    Future<Optional<Dish>> result = dishService.add(dish);
                    try {
                        Optional<Dish> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                DishService.GET_DISHES, (request) -> {
                    Future<List<Dish>> result = dishService.get();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                DishService.DELETE_DISH, (request) -> {
                    Integer id = (Integer) request.getBody();
                    Future<Optional<Dish>> result = dishService.delete(id);
                    try {
                        Optional<Dish> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                DishService.UPDATE_DISH, (request) -> {
                    Dish dish = (Dish) request.getBody();
                    Future<Optional<Dish>> result = dishService.update(dish);
                    try {
                        Optional<Dish> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                DishService.SORT_DISH, (request) -> {
                    Future<List<Dish>> result = dishService.sort();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                DishService.FILTER_DISH, (request) -> {
                    String category = (String) request.getBody();
                    Future<Set<Dish>> result = dishService.filterDishes(category);
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        /// Restaurant

        Validator<Restaurant> restaurantValidator = new RestaurantValidator();
        RestaurantDbRepo restaurantDbRepo = new RestaurantDbRepo(restaurantValidator);
        RestaurantServiceImpl restaurantService = new RestaurantServiceImpl(restaurantDbRepo, executorService);

        tcpServer.addHandler(
                RestaurantService.ADD_RESTAURANT, (request) -> {
                    Restaurant restaurant = (Restaurant) request.getBody();
                    Future<Optional<Restaurant>> result = restaurantService.add(restaurant);
                    try {
                        Optional<Restaurant> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                RestaurantService.GET_RESTAURANT, (request) -> {
                    Future<List<Restaurant>> result = restaurantService.get();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                RestaurantService.DELETE_RESTAURANT, (request) -> {
                    Integer id = (Integer) request.getBody();
                    Future<Optional<Restaurant>> result = restaurantService.delete(id);
                    try {
                        Optional<Restaurant> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });


        tcpServer.addHandler(
                RestaurantService.UPDATE_RESTAURANT, (request) -> {
                    Restaurant restaurant = (Restaurant) request.getBody();
                    Future<Optional<Restaurant>> result = restaurantService.update(restaurant);
                    try {
                        Optional<Restaurant> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                RestaurantService.SORT_RESTAURANT, (request) -> {
                    Future<List<Restaurant>> result = restaurantService.sort();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                RestaurantService.FILTER_RESTAURANT, (request) -> {
                    Integer rating = (Integer) request.getBody();
                    Future<Set<Restaurant>> result = restaurantService.filterRestaurants(rating);
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        //Client

        Validator<Client> clientValidator = new ClientValidator();
        ClientDbRepo clientDbRepo = new ClientDbRepo(clientValidator);
        ClientServiceImpl clientService = new ClientServiceImpl(clientDbRepo, executorService);

        tcpServer.addHandler(
                ClientService.ADD_CLIENT, (request) -> {
                    Client restaurant = (Client) request.getBody();
                    Future<Optional<Client>> result = clientService.add(restaurant);
                    try {
                        Optional<Client> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                ClientService.GET_CLIENTS, (request) -> {
                    Future<List<Client>> result = clientService.get();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                ClientService.DELETE_CLIENTS, (request) -> {
                    Integer id = (Integer) request.getBody();
                    Future<Optional<Client>> result = clientService.delete(id);
                    try {
                        Optional<Client> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });


        tcpServer.addHandler(
                ClientService.UPDATE_CLIENT, (request) -> {
                    Client restaurant = (Client) request.getBody();
                    Future<Optional<Client>> result = clientService.update(restaurant);
                    try {
                        Optional<Client> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                ClientService.SORT_CLIENT, (request) -> {
                    Future<List<Client>> result = clientService.sort();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                ClientService.FILTER_CLIENT, (request) -> {
                    Integer rating = (Integer) request.getBody();
                    Future<Set<Client>> result = clientService.filterClients(rating);
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        //Order

        Validator<Order> orderValidator = new OrderValidator();
        OrderDbRepo orderDbRepo = new OrderDbRepo(orderValidator);
        OrderServiceImpl orderService = new OrderServiceImpl(clientDbRepo, dishDbRepo, restaurantDbRepo, orderDbRepo, executorService);

        tcpServer.addHandler(
                OrderService.ADD_ORDER, (request) -> {
                    Order order = (Order) request.getBody();
                    Future<Optional<Order>> result = orderService.add(order);
                    try {
                        Optional<Order> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                OrderService.GET_ORDERS, (request) -> {
                    Future<List<Order>> result = orderService.get();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                OrderService.DELETE_ORDER, (request) -> {
                    Integer id = (Integer) request.getBody();
                    Future<Optional<Order>> result = orderService.delete(id);
                    try {
                        Optional<Order> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                OrderService.UPDATE_ORDER, (request) -> {
                    Order order = (Order) request.getBody();
                    Future<Optional<Order>> result = orderService.update(order);
                    try {
                        Optional<Order> a = result.get();
                        return new Message(Message.OK, new OptionalObj<>(a));
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                OrderService.SORT_ORDER, (request) -> {
                    Future<List<Order>> result = orderService.sort();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                OrderService.FILTER_ORDER, (request) -> {
                    String paymentType = (String) request.getBody();
                    Future<Set<Order>> result = orderService.filterOrders(paymentType);
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                OrderService.NUMBER_BEST_RESTAURANT, (request) -> {
                    Future<Long> result = orderService.numberBestRestaurants();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                orderService.BEST_RESTAURANT, (request) -> {
                    Future<List<Restaurant>> result = orderService.bestRestaurants();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                OrderService.NUMBER_MOST_DEVOTED_CLIENTS, (request) -> {
                    Future<Long> result = orderService.numberMostDevotedClients();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });

        tcpServer.addHandler(
                orderService.MOST_DEVOTED_CLIENTS, (request) -> {
                    Future<List<Client>> result = orderService.mostDevotedClients();
                    try {
                        return new Message(Message.OK, result.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message(Message.ERROR, e.getMessage());
                    }
                });


        tcpServer.startServer();
        System.out.println("server - bye");
    }
}
