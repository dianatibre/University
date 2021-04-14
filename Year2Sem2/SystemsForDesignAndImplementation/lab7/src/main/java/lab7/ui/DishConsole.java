package lab7.ui;

import lab7.domain.Address;
import lab7.domain.Dish;
import lab7.domain.Orders;
import lab7.domain.validators.ValidatorException;
import lab7.service.AddressService;
import lab7.service.DishService;
import lab7.service.ServiceI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;

public class DishConsole {

    private ServiceI<Integer, Dish> dishServiceI;
    private ServiceI<Integer, Orders> ordersServiceI;
    private final Console console;

    public DishConsole(ServiceI<Integer, Dish> dishServiceI, ServiceI<Integer, Orders> orderServiceI, Console console) {
        this.dishServiceI = dishServiceI;
        this.ordersServiceI = orderServiceI;
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

    private void filterDishes() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Category:");
            String category = String.valueOf(bufferedReader.readLine());
            Predicate<Dish> filterA = a -> a.getCategory().equals(category);
            List<Dish> dishes = dishServiceI.filterFunction(filterA);
            Optional<Integer> op = Optional.of(dishes.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No dishes with this category!");});
            dishes.stream().forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void sortDishes() {
        Comparator<Dish> func = Comparator.comparing(Dish::getName,String::compareTo).reversed();
        List<Dish> dishes = dishServiceI.sortFunction(func);
        Optional<Integer> op = Optional.of(dishes.size());
        op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No dishes added yet!");});
        dishes.stream().forEach(System.out::println);
    }

    private void updateDish() {
        Dish dish = readDish();
        try {
            Optional<Dish> a = Optional.ofNullable(dish);
            a.orElseThrow(Exception::new);
        }
        catch (Exception e) {
            DishService.getLOG().info("Can't have a null dish!");
            return;
        }
        try {
            Optional<Dish> a = dishServiceI.update(dish);
            a.orElseThrow(Exception::new);
        }
        catch (ValidatorException e) {
            DishService.getLOG().info(e.getMessage());
        }
        catch (Exception e) {
            DishService.getLOG().info("Dish with this ID doesn't exist!");
        }
    }

    private void deleteDish() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.parseInt(bufferRead.readLine());
            try {
                this.ordersServiceI.deleteOrderWhenDeletingDish(id);
            } catch (Exception ignored) {
            }
            Optional<Dish> a = dishServiceI.delete(id);
            a.orElseThrow(Exception::new);
        } catch (IOException ex) {
            DishService.getLOG().info(ex.getMessage());
        } catch (NumberFormatException ex) {
            DishService.getLOG().info("Please input an integer value!");
        } catch (Exception e) {
            DishService.getLOG().info("No dish with this ID was found!");
        }
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

    private void getDishes() {
        List<Dish> dishes = dishServiceI.get();
        Optional<Integer> op = Optional.of(dishes.size());
        op.filter(x -> x == 0).ifPresent(s -> {
            System.out.println("There are no dishes!");
        });
        dishes.stream().map(Dish::toString).forEach(AddressService.getLOG()::info);
    }

    private void addDish() {
        Dish dish = readDish();
        try {
            Optional<Dish> a = Optional.ofNullable(dish);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            DishService.getLOG().info("Can't have a null dish!");
            return;
        }
        try {
            Optional<Dish> a = dishServiceI.add(dish);
            a.ifPresent(x -> {
                DishService.getLOG().info("Dish already exists!");
            });
        } catch (ValidatorException e) {
            DishService.getLOG().info(e.getMessage());
        }
    }
    
    
}
