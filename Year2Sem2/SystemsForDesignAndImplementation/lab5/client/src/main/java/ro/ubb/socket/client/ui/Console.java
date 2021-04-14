package ro.ubb.socket.client.ui;


import ro.ubb.socket.client.service.*;
import ro.ubb.socket.common.domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Console {
    protected AddressService addressService;
    protected DishService dishService;
    private final OrderService orderService;
    protected RestaurantService restaurantService;
    private final ClientService clientService;

    /**
     * Console constructor
     *
     * @param addressService the service for addressService
     * @param dishService    the service for dishService
     */
    public Console(AddressService addressService, DishService dishService, OrderService orderService, RestaurantService restaurantService, ClientService clientService) {
        this.addressService = addressService;
        this.dishService = dishService;
        this.orderService = orderService;
        this.restaurantService = restaurantService;
        this.clientService = clientService;
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

