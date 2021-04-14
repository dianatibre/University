package ro.ubb.socket.client;

import ro.ubb.socket.client.service.*;
import ro.ubb.socket.client.tcp.TcpClient;
import ro.ubb.socket.client.ui.Console;
import ro.ubb.socket.common.service.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) throws IllegalAccessException {
        ExecutorService executorService =
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors());
        TcpClient tcpClient = new TcpClient(Service.SERVER_HOST, Service.SERVER_PORT);
        AddressService addressService = new AddressService(executorService, tcpClient);
        DishService dishService = new DishService(executorService, tcpClient);
        OrderService orderService = new OrderService(executorService, tcpClient);
        RestaurantService restaurantService = new RestaurantService(executorService, tcpClient);
        ClientService clientService = new ClientService(executorService, tcpClient);
        Console clientConsole = new Console(addressService, dishService, orderService, restaurantService, clientService);

        clientConsole.runConsole();
        System.out.println("client - bye");
    }
}
