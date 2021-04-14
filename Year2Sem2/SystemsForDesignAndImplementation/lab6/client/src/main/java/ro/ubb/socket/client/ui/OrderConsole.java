package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.AddressService;
import ro.ubb.socket.client.service.OrderService;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OrderConsole {

    private final OrderService orderService;
    private final Console console;

    /**
     * OrderConsole constructor
     *
     * @param orderService the service for the orderService
     * @param console      the main console
     */
    public OrderConsole(OrderService orderService, Console console) {
        this.orderService = orderService;
        this.console = console;
    }

    /**
     * The menu for order.
     */
    protected void printOrderMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new order. \n";
            str += "\t 2. Get all orders. \n";
            str += "\t 3. Delete an order. \n";
            str += "\t 4. Update an order. \n";
            str += "\t 5. Sort orders after their date. \n";
            str += "\t 6. Filter orders after the payment type. \n";
            str += "\t 7. The best restaurants. \n";
            str += "\t 8. Most frequencies clients.\n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getOrdersCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the order's commands
     */
    private Map<String, Runnable> getOrdersCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addOrder);
        commands.put("2", this::getOrders);
        commands.put("3", this::deleteOrder);
        commands.put("4", this::updateOrder);
        commands.put("5", this::sortOrders);
        commands.put("6", this::filterOrders);
        commands.put("7", this::mostOrders);
        commands.put("8", this::frequenciesClients);
        return commands;
    }

    /**
     * Adds an order.
     *
     * @throws Exception          if the order is null
     * @throws ValidatorException if the entity is not a valid one.
     */
    private void addOrder() {
        Order order = readOrder();
        try {
            Optional<Order> a = Optional.ofNullable(order);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't have a null order!");
            return;
        }
        console.getExecutorService().submit(() -> {
            Optional<Order> a;
            try {
                a = Optional.ofNullable(orderService.myAdd(order).get().getValue());
                a.ifPresent(x -> {
                    System.out.println("Order already existed!/Validation Exception");
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * The function reads an order from keyboard.
     *
     * @return null in case of exception,otherwise the dish read
     * @throws IOException if the input from keyboard is not ok
     */
    private Order readOrder() {
        System.out.println("Read Order {restaurantID, clientID, dishID, payment type, date}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Order Id:");
            Integer oid = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Restaurant Id:");
            Integer rid = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Client Id:");
            Integer cid = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Dish Id:");
            Integer did = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Payment type:");
            String paymenttype = bufferedReader.readLine();
            System.out.println("Date");
            String date = bufferedReader.readLine();
            Date orderDate = new SimpleDateFormat("yyyy/MM/dd").parse(date);
            Order order = new Order(rid, cid, did, paymenttype, orderDate);
            order.setId(oid);
            return order;
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the orders.
     */
    private void getOrders() {
        console.getExecutorService().submit(() -> {
            CompletableFuture<List<Order>> orders = orderService.myGet();
            Optional<Integer> op;
            try {
                op = Optional.of(orders.get().size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("There are no orders!");
                });
                orders.get().forEach(System.out::println);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deletes an order.
     *
     * @throws Exception   if the id of the order doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteOrder() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                try {
                    Optional<Order> a = Optional.ofNullable(orderService.myDelete(id).get().getValue());
                    a.orElseThrow(Exception::new);
                } catch (Exception e) {
                    System.out.println("Order does not exists!");
                }
            });
        } catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Updates an order
     *
     * @throws IllegalArgumentException if the order is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateOrder() {
        Order order = readOrder();
        console.getExecutorService().submit(() -> {
            try {
                Optional<Order> a = Optional.ofNullable(order);
                a.orElseThrow(Exception::new);
                Optional<Order> c = Optional.ofNullable(orderService.myUpdate(order).get().getValue());
                c.orElseThrow(Exception::new);
            } catch (Exception e) {
                System.out.println("Order does not exists or nonexistent dishId/clientId/restaurantId! / Validation Exception!");
                return;
            }
        });

    }

    /**
     * Prints all the sorted orders from the list received
     */
    private void sortOrders() {
        console.getExecutorService().submit(() -> {
            List<Order> orders = orderService.sortOrderDate();
            Optional<Integer> op = Optional.of(orders.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("There are no orders!");
            });
            orders.forEach(System.out::println);
        });
    }

    /**
     * Filters the orders after the payment type name
     * The functions gets as input from the keyboard a payment type
     * returns the list of orders with that payment type
     */
    private void filterOrders() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Payment type:");
            String paymentType = String.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                List<Order> orders = orderService.filterOrder(paymentType);
                Optional<Integer> op = Optional.of(orders.size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("No orders with this payment type");
                });
                orders.forEach(System.out::println);
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Prints the most orders of the clients.
     */
    private void frequenciesClients() {
        console.getExecutorService().submit(() -> {
            List<Client> clients = orderService.mostDevotedClients();
            Optional<Integer> op = Optional.of(clients.size());
            try {
                op.filter(x -> x != 0).orElseThrow(Exception::new);
            } catch (Exception e) {
                System.out.println("No order has been made yet!");
                return;
            }
            System.out.println("Number of order times: " + this.orderService.numberMostDevotedClients());
            clients.stream().forEach(System.out::println);
        });
    }

    /**
     * Prints the most orders of the restaurants.
     */
    private void mostOrders() {
        console.getExecutorService().submit(() -> {
            List<Restaurant> restaurants = orderService.bestRestaurants();
            Optional<Integer> op = Optional.of(restaurants.size());
            try {
                op.filter(x -> x != 0).orElseThrow(Exception::new);
            } catch (Exception e) {
                System.out.println("No restaurant is opened yet!");
                return;
            }
            System.out.println("Number of orders times: " + this.orderService.numberBestRestaurants());
            restaurants.stream().forEach(System.out::println);
        });
    }
}
