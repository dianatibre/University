package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.OrderService;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class OrderConsole {

    private OrderService orderService;
    private Console console;

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
            str += "\t 6. Filter the orders after paymentType. \n";
            str += "\t 7. The best restaurants. \n";
            str += "\t 8. Most frequencies clients.\n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getOrderCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the order's commands
     */
    private Map<String, Runnable> getOrderCommands() {
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
     * Adds a order.
     *
     * @throws Exception          if the order is null
     * @throws ValidatorException if the entity is not a valid one.
     */
    private void addOrder() {
        Order order = readOrder();
        try {
            Optional<Order> ord = Optional.ofNullable(order);
            ord.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't have a null order!");
            return;
        }
        orderService.add(order).thenAccept(c -> {
            c.ifPresent(x-> {System.out.println("Order already exists or innexistent clientId/dishId/restaurantId!");});
        })
                .whenComplete((r, ex) -> {
                    if(ex != null)
                        System.out.println(ex.getMessage());
                });
    }

    /**
     * Prints the orders.
     */
    private void getOrders() {
        orderService.get().thenAccept(orders -> {
            Optional<Integer> op = Optional.of(orders.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("There are no orders!");});
            orders.stream().forEach(System.out::println);
        });
    }

    /**
     * Deletes a client.
     *
     * @throws Exception   if the id of the client doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteOrder() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());

            orderService.delete(id)
                    .whenComplete((r, ex) -> {
                        if(ex != null)
                            System.out.println("No Order with this ID was found!");
                    });
        } catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            System.out.println("No order with this Id");
        }
    }

    /**
     * Updates a order.
     *
     * @throws IllegalArgumentException if the order is null
     * @throws ValidatorException       if the entity is not a valid one.
     */
    private void updateOrder() {
        Order order = readOrder();
        try {
            Optional<Order> cl = Optional.ofNullable(order);
            cl.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't update a null order!");
            return;
        }
        try {
            orderService.update(order)
                    .whenComplete((r, ex) -> {
                        if(ex != null)
                            System.out.println(ex.getMessage());
                    });
        } catch (ValidatorException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints all the sorted orders from the list received
     */
    private void sortOrders() {
        orderService.sort().thenAccept(orders-> {
            Optional<Integer> op = Optional.of(orders.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No order added yet!");});
            orders.stream().forEach(System.out::println);
        });
    }

    /**
     * filter the orders after a specific age
     * takes the paymentType as input from keyboard
     * prints the list of orders having a specific paymentType
     */
    private void filterOrders() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Payment Type:");
            String paymentType = bufferedReader.readLine();
            try {
                orderService.filter(paymentType).thenAccept(rentals -> {
                    Optional<Integer> op = Optional.of(rentals.size());
                    op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No order has the given value for paymentType!");});
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
     * Prints the most orders of the restaurants.
     */
    private void mostOrders() {
        this.orderService.numberBestRestaurants().thenApply(x -> {
            System.out.println("Number of orders: " + x);
            return null;
        });
        orderService.bestRestaurants().thenAccept(orders -> {
            Optional<Integer> op = Optional.of(orders.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No restaurant has any order yet!");});
            orders.stream().forEach(System.out::println);
        });
    }

    /**
     * Prints the most orders of the clients.
     */
    private void frequenciesClients() {
        this.orderService.numberMostDevotedClients().thenApply(x -> {
            System.out.println("Number of orders: " + x);
            return null;
        });
        orderService.mostDevotedClients().thenAccept(orders -> {
            Optional<Integer> op = Optional.of(orders.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No client has any order yet!");});
            orders.stream().forEach(System.out::println);
        });
    }

    /**
     * The function reads a order from keyboard.
     *
     * @return null in case of exception,otherwise the client read
     * @throws IOException if the input from keyboard is not ok
     */
    private Order readOrder() {
        System.out.println("Read Order {restaurantId,clientId,dishId,paymentType,orderDate}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("RestaurantId:");
            Integer restaurantId = Integer.valueOf(bufferedReader.readLine());
            System.out.println("ClientId:");
            int clientId = Integer.parseInt(bufferedReader.readLine());
            System.out.println("DishId:");
            int dishId = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Payment Type:");
            String paymentType = bufferedReader.readLine();
            System.out.println("orderDate");
            String dateOrderStr = bufferedReader.readLine();
            Date orderDate = new SimpleDateFormat("yyyy/MM/dd").parse(dateOrderStr);
            Order order = new Order(restaurantId, clientId, dishId, paymentType, orderDate);
            order.setId(id);
            return order;
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
