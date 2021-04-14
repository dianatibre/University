package lab7.ui;

import lab7.domain.Address;
import lab7.domain.Dish;
import lab7.domain.Orders;
import lab7.domain.Restaurant;
import lab7.domain.Orders;
import lab7.domain.validators.ValidatorException;
import lab7.service.AddressService;
import lab7.service.RestaurantService;
import lab7.service.ServiceI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;

public class RestaurantConsole {

    private ServiceI<Integer, Restaurant> restaurantServiceI;
    private ServiceI<Integer, Orders> orderServiceI;
    private final Console console;

    public RestaurantConsole(ServiceI<Integer, Restaurant> restaurantServiceI, ServiceI<Integer, Orders> orderServiceI, Console console) {
        this.restaurantServiceI = restaurantServiceI;
        this.orderServiceI = orderServiceI;
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

    private void filterRestaurants() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Rating:");
            Integer rating = Integer.valueOf(bufferedReader.readLine());
            Predicate<Restaurant> filterA = a -> a.getRating()==rating;
            List<Restaurant> restaurants = restaurantServiceI.filterFunction(filterA);
            Optional<Integer> op = Optional.of(restaurants.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No restaurant with this city!");});
            restaurants.stream().forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void sortRestaurants() {
        Comparator<Restaurant> func = Comparator.comparing(Restaurant::getRating,Integer::compareTo).reversed();
        List<Restaurant> restaurants = restaurantServiceI.sortFunction(func);
        Optional<Integer> op = Optional.of(restaurants.size());
        op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No restaurants added yet!");});
        restaurants.stream().forEach(System.out::println);
    }

    private void updateRestaurant() {
        Restaurant restaurant = readRestaurant();
        try {
            Optional<Restaurant> a = Optional.ofNullable(restaurant);
            a.orElseThrow(Exception::new);
        }
        catch (Exception e) {
            RestaurantService.getLOG().info("Can't have a null restaurant!");
            return;
        }
        try {
            Optional<Restaurant> a = restaurantServiceI.update(restaurant);
            a.orElseThrow(Exception::new);
        }
        catch (ValidatorException e) {
            RestaurantService.getLOG().info(e.getMessage());
        }
        catch (Exception e) {
            RestaurantService.getLOG().info("Restaurant with this ID doesn't exist!");
        }
    }

    private void deleteRestaurant() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.valueOf(bufferRead.readLine());

            try {
                this.orderServiceI.deleteOrderWhenDeletingRestaurant(id);
            } catch (Exception e) {
            }
            Optional<Restaurant> a = restaurantServiceI.delete(id);
            a.orElseThrow(Exception::new);
        } catch (IOException ex) {
            RestaurantService.getLOG().info(ex.getMessage());
        } catch (NumberFormatException ex) {
            RestaurantService.getLOG().info("Please input an integer value!");
        } catch (Exception e) {
            RestaurantService.getLOG().info("No restaurant with this ID was found!");
            return;
        }
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

    private void getRestaurants() {
        List<Restaurant> restaurants = restaurantServiceI.get();
        Optional<Integer> op = Optional.of(restaurants.size());
        op.filter(x -> x == 0).ifPresent(s -> {
            System.out.println("There are no restaurants!");
        });
        restaurants.stream().map(Restaurant::toString).forEach(AddressService.getLOG()::info);
    }

    private void addRestaurant() {
        Restaurant restaurant = readRestaurant();
        try {
            Optional<Restaurant> a = Optional.ofNullable(restaurant);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            RestaurantService.getLOG().info("Can't have a null restaurant!");
            return;
        }
        try {
            Optional<Restaurant> a = restaurantServiceI.add(restaurant);
            a.ifPresent(x -> {
                RestaurantService.getLOG().info("Restaurant already exists!");
            });
        } catch (ValidatorException e) {
            RestaurantService.getLOG().info(e.getMessage());
        }
    }


}
