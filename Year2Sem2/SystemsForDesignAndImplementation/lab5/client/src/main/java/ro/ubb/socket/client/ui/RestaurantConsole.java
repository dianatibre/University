package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.OrderService;
import ro.ubb.socket.client.service.RestaurantService;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class RestaurantConsole {
    private RestaurantService restaurantService;
    private OrderService orderService;
    private Console console;

    /**
     * RestauranyConsole constructor
     *
     * @param restaurantService the service for the restaurantService
     * @param console the main console
     */
    public RestaurantConsole(RestaurantService restaurantService, OrderService orderService,Console console) {
        this.restaurantService = restaurantService;
        this.orderService=orderService;
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
        commands.put("0",()->console.printOptions());
        commands.put("1", this::addRestaurant);
        commands.put("2", this::getRestaurants);
        commands.put("3",this::deleteRestaurant);
        commands.put("4", this::updateRestaurant);
        commands.put("5", this::sortRestaurants);
        commands.put("6", this::filterRestaurants);
        return commands;
    }

    /**
     * filter dishes after a specific rating taken as input from keyboard
     */
    private void filterRestaurants() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Rating:");
            int rating = Integer.valueOf(bufferedReader.readLine());
            try {
                restaurantService.filter(rating).thenAccept(rentals -> {
                    Optional<Integer> op = Optional.of(rentals.size());
                    op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No restaurant has the given value for rating!");});
                    rentals.stream().forEach(System.out::println);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Adds a restaurant.
     * @throws Exception if the restaurant is null
     * @throws ValidatorException if the entity is not a valid one.
     */
    private void addRestaurant(){
        Restaurant restaurant=readRestaurant();
        try{
            Optional<Restaurant> r=Optional.ofNullable(restaurant);
            r.orElseThrow(Exception::new);
        }
        catch(Exception e){
            System.out.println("Can't have a null restaurant!");
            return;
        }
        restaurantService.add(restaurant).thenAccept(c -> {
            c.ifPresent(x-> {System.out.println("Restaurant already exists!");});
        })
                .whenComplete((r, ex) -> {
                    if(ex != null)
                        System.out.println(ex.getMessage());
                });
    }

    /**
     * The function reads a restaurant from keyboard.
     * @throws IOException if the input from keyboard is not ok
     * @return null in case of exception,otherwise the dish read
     */
    private Restaurant readRestaurant(){
        System.out.println("Read Restaurant {name,rating,capacity,delivery}");
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Id:");
            Integer id=Integer.valueOf(bufferedReader.readLine());
            System.out.println("Name:");
            String name=bufferedReader.readLine();
            System.out.println("Rating");
            Integer rating=Integer.valueOf(bufferedReader.readLine());
            System.out.println("Capacity:");
            Integer capacity=Integer.valueOf(bufferedReader.readLine());
            System.out.println("Delivery:");
            Boolean delivery=Boolean.valueOf(bufferedReader.readLine());
            Restaurant restaurant=new Restaurant(name,rating,capacity,delivery);
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
    private void getRestaurants(){
        restaurantService.get().thenAccept(restaurants -> {
            Optional<Integer> op = Optional.of(restaurants.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("There are no restaurants!");});
            restaurants.stream().forEach(System.out::println);
        });
    }


    /**
     * Deletes a restaurant.
     * @throws Exception if the id of the restaurant doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteRestaurant() {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Id:");
            Integer id=Integer.valueOf(bufferedReader.readLine());
            restaurantService.delete(id)
                    .whenComplete((r, ex) -> {
                        if(ex != null)
                            System.out.println("No restaurants with this ID was found!");
                    });
        }
        catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        catch (Exception e) {
            System.out.println("No restaurant with this Id");
        }
    }

    /**
     * Updates a restaurant
     *
     * @throws IllegalArgumentException if the restaurant is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateRestaurant() {
        Restaurant restaurant = readRestaurant();
        try {
            Optional<Restaurant> r = Optional.ofNullable(restaurant);
            r.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't update a null restaurant!");
            return;
        }
        try {
            restaurantService.update(restaurant)
                    .whenComplete((r, ex) -> {
                        if(ex != null)
                            System.out.println(ex.getMessage());
                    });
        } catch (ValidatorException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Prints all the sorted restaurants from the list received
     *
     * */
    private void sortRestaurants() {
        restaurantService.sort().thenAccept(restaurants-> {
            Optional<Integer> op = Optional.of(restaurants.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No restaurants added yet!");});
            restaurants.stream().forEach(System.out::println);
        });
    }
}
