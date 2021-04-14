package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.DishService;
import ro.ubb.socket.client.service.OrderService;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class DishConsole {

    private DishService dishService;
    private OrderService orderService;
    private Console console;

    /**
     * DishConsole constructor
     *
     * @param dishService the service for the dishService
     * @param  orderService the service for the orderService
     * @param console the main console
     */
    public DishConsole(DishService dishService, OrderService orderService,Console console) {
        this.dishService = dishService;
        this.orderService=orderService;
        this.console = console;
    }


    /**
     * The menu for dish.
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
            str += "\t 5. Sort dishes after their name. \n";
            str += "\t 6. Filter the dishes after a specific category. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getDishCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the dish's commands
     */
    private Map<String, Runnable> getDishCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0",()->console.printOptions());
        commands.put("1", this::addDish);
        commands.put("2", this::getDishes);
        commands.put("3",this::deleteDish);
        commands.put("4", this::updateDish);
        commands.put("5", this::sortDishes);
        commands.put("6", this::filterDishes);
        return commands;
    }

    /**
     * filter dishes after a specific category taken as imput from keyboard
     */
    private void filterDishes() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Category:");
            String category = String.valueOf(bufferedReader.readLine());
            try {
                dishService.filter(category).thenAccept(rentals -> {
                    Optional<Integer> op = Optional.of(rentals.size());
                    op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No dish has the given value for category!");});
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
     * Adds a dish.
     * @throws Exception if the dish is null
     * @throws ValidatorException if the entity is not a valid one.
     */
    private void addDish(){
        Dish dish=readDish();
        try{
            Optional<Dish> d=Optional.ofNullable(dish);
            d.orElseThrow(Exception::new);
        }
        catch(Exception e){
            System.out.println("Can't have a null dish!");
            return;
        }
        dishService.add(dish).thenAccept(c -> {
            c.ifPresent(x-> {System.out.println("Dish already exists!");});
        })
                .whenComplete((r, ex) -> {
                    if(ex != null)
                        System.out.println(ex.getMessage());
                });
    }

    /**
     * The function reads a dish from keyboard.
     * @throws IOException if the input from keyboard is not ok
     * @return null in case of exception,otherwise the dish read
     */
    private Dish readDish(){
        System.out.println("Read Dish {name,price,quantity,category}");
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Id:");
            Integer id=Integer.valueOf(bufferedReader.readLine());
            System.out.println("Name:");
            String name=bufferedReader.readLine();
            System.out.println("Price");
            float price=Float.parseFloat(bufferedReader.readLine());
            System.out.println("Quantity");
            int quantity=Integer.parseInt(bufferedReader.readLine());
            System.out.println("Category:");
            String category=bufferedReader.readLine();
            Dish dish=new Dish(name,price,quantity,category);
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
    private void getDishes(){
        dishService.get().thenAccept(dishes -> {
            Optional<Integer> op = Optional.of(dishes.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("There are no addresses!");});
            dishes.stream().forEach(System.out::println);
        });
    }

    /**
     * Deletes a dish.
     * @throws Exception if the id of the dish doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteDish() {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Id:");
            Integer id=Integer.valueOf(bufferedReader.readLine());
            try{
                //this.orderService.deleteOrderByDishId(id);
            }
            catch (Exception e){}
            dishService.delete(id)
                    .whenComplete((r, ex) -> {
                        if(ex != null)
                            System.out.println("No Dish with this ID was found!");
                    });
        }
        catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        catch (Exception e) {
            System.out.println("No dish with this Id");
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
        try {
            Optional<Dish> d = Optional.ofNullable(dish);
            d.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't update a null dish!");
            return;
        }
        try {
            dishService.update(dish)
                    .whenComplete((r, ex) -> {
                        if(ex != null)
                            System.out.println(ex.getMessage());
                    });
        } catch (ValidatorException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Prints all the sorted dishes from the list received
     *
     * */
    private void sortDishes() {
        dishService.sort().thenAccept(addresses-> {
            Optional<Integer> op = Optional.of(addresses.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No address added yet!");});
            addresses.stream().forEach(System.out::println);
        });
    }
}
