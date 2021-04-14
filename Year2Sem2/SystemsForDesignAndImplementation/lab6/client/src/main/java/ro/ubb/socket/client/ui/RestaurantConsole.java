package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.OrderService;
import ro.ubb.socket.client.service.RestaurantService;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RestaurantConsole {

    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final Console console;

    public RestaurantConsole(RestaurantService restaurantService, OrderService orderService, Console console) {
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.console = console;
    }

    /**
     * The menu for restaurant.
     */
    protected void printRestaurantMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new restaurant. \n";
            str += "\t 2. Get all restaurants. \n";
            str += "\t 3. Delete a restaurant. \n";
            str += "\t 4. Update a restaurant. \n";
            str += "\t 5. Sort restaurants after their rating. \n";
            str += "\t 6. Filter the restaurants after a specific rating. \n";
            //str += "\t 7. Get the best restaurant based on ratings \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getRestaurantCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the restaurant's commands
     */
    private Map<String, Runnable> getRestaurantCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", () -> console.printOptions());
        commands.put("1", this::addRestaurant);
        commands.put("2", this::getRestaurants);
        commands.put("3", this::deleteRestaurant);
        commands.put("4", this::updateRestaurant);
        commands.put("5", this::sortRestaurants);
        commands.put("6", this::filterRestaurants);
        //commands.put("7", this::bestRestaurantBasedOnRating);
        return commands;
    }

    /**
     * filters the restaurants after a specific rating
     */
    private void filterRestaurants() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Rating:");
            Integer rating = Integer.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                List<Restaurant> restaurants = restaurantService.filterRestaurantsRating(rating);
                Optional<Integer> op = Optional.of(restaurants.size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("No restaurant with this rating");
                });
                restaurants.forEach(System.out::println);
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Adds a restaurant.
     *
     * @throws Exception if the restaurant is null
     */
    private void addRestaurant() {
        Restaurant restaurant = readRestaurant();
        try {
            Optional<Restaurant> a = Optional.ofNullable(restaurant);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't have a null restaurant!");
            return;
        }
        console.getExecutorService().submit(() -> {
            Optional<Restaurant> a = Optional.ofNullable(restaurantService.add(restaurant).getValue());
            a.ifPresent(x -> {
                System.out.println("Restaurant already exists!/Validation Exception");
            });
        });
    }

    /**
     * The function reads a restaurant from keyboard.
     *
     * @return null in case of exception,otherwise the dish read
     * @throws IOException if the input from keyboard is not ok
     */
    private Restaurant readRestaurant() {
        System.out.println("Read Restaurant {name,rating,capacity,delivery}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Name:");
            String name = bufferedReader.readLine();
            System.out.println("Rating");
            Integer rating = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Capacity:");
            Integer capacity = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Delivery:");
            Boolean delivery = Boolean.valueOf(bufferedReader.readLine());
            Restaurant restaurant = new Restaurant(name, rating, capacity, delivery);
            restaurant.setId(id);
            return restaurant;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the restaurant.
     */
    private void getRestaurants() {
        console.getExecutorService().submit(() -> {
            CompletableFuture<List<Restaurant>> restaurants = restaurantService.myGet();
            Optional<Integer> op = null;
            try {
                op = Optional.of(restaurants.get().size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("There are no restaurants!");
                });
                restaurants.get().forEach(System.out::println);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deletes a restaurant.
     *
     * @throws Exception   if the id of the restaurant doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteRestaurant() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                try {
                    Optional<Restaurant> a = Optional.ofNullable(restaurantService.myDelete(id).get().getValue());
                    Optional<Order> o = Optional.ofNullable(orderService.myDelete(id).get().getValue());
                    a.orElseThrow(Exception::new);
                    o.orElseThrow(Exception::new);
                } catch (Exception e) {
                    System.out.println("Restaurant doesn't exist/Validation exception");
                }
            });
        } catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Updates a restaurant
     *
     * @throws IllegalArgumentException if the restaurant is null
     **/
    private void updateRestaurant() {
        Restaurant restaurant = readRestaurant();
        console.getExecutorService().submit(() -> {
            try {
                Optional<Restaurant> a = Optional.ofNullable(restaurant);
                a.orElseThrow(Exception::new);
                Optional<Restaurant> c = Optional.ofNullable(restaurantService.myUpdate(restaurant).get().getValue());
                c.orElseThrow(Exception::new);
            } catch (Exception e) {
                System.out.println("Restaurant with this ID doesn't exist!/Validation Exception!");
                return;
            }
        });
    }


    /**
     * Prints all the sorted restaurants from the list received
     */
    private void sortRestaurants() {
        console.getExecutorService().submit(() -> {
            List<Restaurant> restaurants = restaurantService.sortRestaurantsRating();
            Optional<Integer> op = Optional.of(restaurants.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("There are no restaurants!");
            });
            restaurants.forEach(System.out::println);
        });
    }


    /**
     * Prints the best restaurant based on ratings (can be multiples with the same rating)
     */
//    private void bestRestaurantBasedOnRating() {
//        Set<Restaurant> restaurants = restaurantService.getAll();
//        restaurants.stream()
//                .max(Comparator.comparing(Restaurant::getRating))
//                .ifPresent(System.out::println);
//    }
}
