package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.DishService;
import ro.ubb.socket.client.service.OrderService;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.validators.ValidatorException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DishConsole {
    private final DishService dishService;
    private final OrderService orderService;
    private final Console console;

    /**
     * DishConsole constructor
     *
     * @param dishService  the service for the clientService
     * @param orderService
     * @param console      the main console
     */
    public DishConsole(DishService dishService, OrderService orderService, Console console) {
        this.dishService = dishService;
        this.orderService = orderService;
        this.console = console;
    }

    /**
     * The menu for dishes.
     */
    protected void printDishMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new dish. \n";
            str += "\t 2. Get all dishes. \n";
            str += "\t 3. Delete a dish. \n";
            str += "\t 4. Update a dish. \n";
            str += "\t 5. Sort dishes after their category. \n";
            str += "\t 6. Filter dishes after the category. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getDishCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the dishes' commands
     */
    private Map<String, Runnable> getDishCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addDish);
        commands.put("2", this::getDishes);
        commands.put("3", this::deleteDish);
        commands.put("4", this::updateDish);
        commands.put("5", this::sortDishes);
        commands.put("6", this::filterDishes);
        return commands;
    }

    /**
     * Adds a dish.
     *
     * @throws Exception          if the client is null
     * @throws ValidatorException if the entity is not a valid one.
     */
    private void addDish() {
        Dish dish = readDish();
        try {
            Optional<Dish> a = Optional.ofNullable(dish);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't have a null client!");
            return;
        }
        console.getExecutorService().submit(() -> {
            Optional<Dish> a;
            try {
                a = Optional.ofNullable(dishService.myAdd(dish).get().getValue());
                a.ifPresent(x -> {
                    System.out.println("Dish already existed!/Validation Exception");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * The function reads a dish from keyboard.
     *
     * @return null in case of exception,otherwise the client read
     * @throws IOException if the input from keyboard is not ok
     */
    private Dish readDish() {
        System.out.println("Read Dish {name,price,quantity,category}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Name:");
            String name = bufferedReader.readLine();
            System.out.println("Price:");
            float price = Float.parseFloat(bufferedReader.readLine());
            System.out.println("Quantity:");
            int quantity = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Category:");
            String category = bufferedReader.readLine();
            Dish dish = new Dish(name, price, quantity, category);
            dish.setId(id);
            return dish;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the dishes.
     */
    private void getDishes() {
        console.getExecutorService().submit(() -> {
            CompletableFuture<List<Dish>> dishes = dishService.myGet();
            Optional<Integer> op;
            try {
                op = Optional.of(dishes.get().size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("There are no dishes!");
                });
                dishes.get().forEach(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deletes a dish.
     *
     * @throws Exception   if the id of the client doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteDish() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                try {
                    Optional<Dish> a = Optional.ofNullable(dishService.myDelete(id).get().getValue());
                    Optional<Order> b = Optional.ofNullable(orderService.myDelete(id).get().getValue());
                    a.orElseThrow(Exception::new);
                } catch (Exception e) {
                    System.out.println("Dish doesn't exists/Validation exception");
                }
            });
        } catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Updates a dish
     *
     * @throws IllegalArgumentException if the dish is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateDish() {
        Dish dish = readDish();
        console.getExecutorService().submit(() -> {
            try {
                Optional<Dish> a = Optional.ofNullable(dish);
                a.orElseThrow(Exception::new);
                Optional<Dish> c = Optional.ofNullable(dishService.myUpdate(dish).get().getValue());
                c.orElseThrow(Exception::new);
            } catch (Exception e) {
                System.out.println("Dish with this ID doesn't exist!/Validation Exception!");
            }
        });

    }

    /**
     * Prints all the sorted dishes from the list received
     */
    private void sortDishes() {
        console.getExecutorService().submit(() -> {
            List<Dish> dishes = dishService.sortDishCategory();
            Optional<Integer> op = Optional.of(dishes.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("There are no dishes!");
            });
            dishes.forEach(System.out::println);
        });
    }

    /**
     * Filters the dishes after the category value
     * The functions gets as input from the keyboard a category value
     * returns the list of dishes with that category
     */
    private void filterDishes() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Category:");
            String category = bufferedReader.readLine();
            console.getExecutorService().submit(() -> {
                List<Dish> dishes = dishService.filterDishCategory(category);
                Optional<Integer> op = Optional.of(dishes.size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("No dish with this category");
                });
                dishes.forEach(System.out::println);
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
