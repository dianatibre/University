package ro.ubb.socket.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.socket.client.service.*;
import ro.ubb.socket.client.ui.Console;


public class ClientApp {
    public static void main(String[] args) throws IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ro.ubb.socket.client.config");
        AddressService addressService = context.getBean(AddressService.class);
        ClientService clientService = context.getBean(ClientService.class);
        DishService dishService = context.getBean(DishService.class);
        OrderService orderService = context.getBean(OrderService.class);
        RestaurantService restaurantService = context.getBean(RestaurantService.class);
        Console clientConsole = new Console(addressService, clientService, dishService, orderService, restaurantService);

        clientConsole.runConsole();
        System.out.println("client - bye");
    }
}
