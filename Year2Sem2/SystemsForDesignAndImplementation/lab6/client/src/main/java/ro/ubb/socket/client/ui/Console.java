package ro.ubb.socket.client.ui;


import ro.ubb.socket.client.service.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Console {
    private AddressService addressService;
    private DishService dishService;
    private OrderService orderService;
    private RestaurantService restaurantService;
    private ClientService clientService;
    private ExecutorService executorService;

    /**
     * Console constructor
     *  @param addressService the service for addressService
     * @param dishService
     * @param orderService
     * @param restaurantService
     */
    public Console(AddressService addressService, ClientService clientService, DishService dishService, OrderService orderService, RestaurantService restaurantService) {
        this.addressService = addressService;
        this.dishService = dishService;
        this.orderService = orderService;
        this.restaurantService = restaurantService;
        this.clientService = clientService;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * The main menu.
     */
    protected void printOptions() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Exit \n";
            str += "\t 1. Client Menu \n";
            str += "\t 2. Dish Menu \n";
            str += "\t 3. Order Menu \n";
            str += "\t 4. Address Menu \n";
            str += "\t 5. Restaurant Menu \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getCommands().getOrDefault(key, () -> System.out.println("Invalid command")).run();
        }
    }

    /**
     * @return the main commands
     */
    private Map<String, Runnable> getCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", () -> System.exit(0));
        commands.put("1", this::callClientMenu);
        commands.put("2", this::callDishMenu);
        commands.put("3", this::callOrderMenu);
        commands.put("4", this::callAddressMenu);
        commands.put("5", this::callRestaurantMenu);
        return commands;
    }

    private void callClientMenu() {
        ClientConsole clientConsole = new ClientConsole(this.clientService, this);
        clientConsole.printClientMenu();
    }

    private void callDishMenu() {
        DishConsole dishConsole = new DishConsole(this.dishService, this.orderService, this);
        dishConsole.printDishMenu();
    }

    private void callOrderMenu() {
        OrderConsole orderConsole = new OrderConsole(this.orderService, this);
        orderConsole.printOrderMenu();
    }

    private void callAddressMenu() {
        AddressConsole addressConsole = new AddressConsole(this.addressService, this);
        addressConsole.printAddressMenu();
    }

    private void callRestaurantMenu() {
        RestaurantConsole restaurantConsole = new RestaurantConsole(this.restaurantService, this.orderService, this);
        restaurantConsole.printRestaurantMenu();
    }

    /**
     * The function runs the console.
     */
    public void runConsole() {
        while (true)
            this.printOptions();
    }
}

