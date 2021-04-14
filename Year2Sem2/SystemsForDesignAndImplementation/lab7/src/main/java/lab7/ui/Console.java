package lab7.ui;

import lab7.domain.*;
import lab7.service.ServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class Console {

    @Autowired
    @Qualifier("addressService")
    private ServiceI<Integer, Address> addressServiceI;

    @Autowired
    @Qualifier("clientService")
    private ServiceI<Integer, Client> clientServiceI;

    @Autowired
    @Qualifier("orderService")
    private ServiceI<Integer, Orders> orderServiceI;

    @Autowired
    @Qualifier("restaurantService")
    private ServiceI<Integer, Restaurant> restaurantServiceI;

    @Autowired
    @Qualifier("dishService")
    private ServiceI<Integer, Dish> dishServiceI;

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
        ClientConsole clientConsole = new ClientConsole(this.clientServiceI, this.orderServiceI, this);
        clientConsole.printClientMenu();
    }

    private void callDishMenu() {
        DishConsole dishConsole = new DishConsole(this.dishServiceI, this.orderServiceI, this);
        dishConsole.printDishMenu();
    }

    private void callOrderMenu() {
        OrderConsole orderConsole = new OrderConsole(this.orderServiceI, this);
        orderConsole.printOrderMenu();
    }

    private void callAddressMenu() {
        AddressConsole addressConsole = new AddressConsole(addressServiceI, clientServiceI, orderServiceI, this);
        addressConsole.printAddressMenu();
    }

    private void callRestaurantMenu() {
        RestaurantConsole restaurantConsole = new RestaurantConsole(this.restaurantServiceI, this.orderServiceI, this);
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
