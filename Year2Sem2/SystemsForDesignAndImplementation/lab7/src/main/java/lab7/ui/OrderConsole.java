package lab7.ui;

import lab7.domain.Orders;
import lab7.domain.validators.ValidatorException;
import lab7.service.ClientService;
import lab7.service.OrderService;
import lab7.service.ServiceI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class OrderConsole {
    private final ServiceI<Integer, Orders> orderServiceI;
    private final Console console;

    /**
     * OrderConsole constructor
     *
     * @param orderService the service for the ClientService
     * @param console       the main console
     */
    public OrderConsole(ServiceI<Integer, Orders> orderService, Console console) {
        this.orderServiceI = orderService;
        this.console = console;
    }

    /**
     * The menu for Order.
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
            str += "\t 6. Filter orders after their paymentType. \n";
            str += "\t 7. The best restaurants. \n";
            str += "\t 8. Most frequencies clients.\n";

            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getOrderCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the Order's commands
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
        commands.put("7", this::filterOrders);
        commands.put("8", this::filterOrders);
        return commands;
    }

    /**
     * Adds an Order.
     *
     * @throws ValidatorException if the Order is null
     */
    private void addOrder() {
        Orders order = readOrder();
        try {
            Optional<Orders> o = Optional.ofNullable(order);
            o.orElseThrow(Exception::new);
        } catch (Exception e) {
            OrderService.getLOG().info("Can't have a null Order!");
            return;
        }
        try {
            Optional<Orders> o = orderServiceI.add(order);
            o.ifPresent(x -> {
                OrderService.getLOG().info("Order already exists or inexistent restaurantID/dishID/clientID!");
            });
        } catch (ValidatorException e) {
            ClientService.getLOG().info(e.getMessage());
        }
    }

    /**
     * The function reads a order from keyboard.
     *
     * @return null in case of exception,otherwise the order read
     * @throws IOException if the input from keyboard is not ok
     */
    private Orders readOrder() {
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
            Orders order = new Orders(restaurantId, clientId, dishId, paymentType, orderDate);
            order.setId(id);
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
        List<Orders> orders = orderServiceI.get();
        Optional<Integer> op = Optional.of(orders.size());
        op.filter(x -> x == 0).ifPresent(s -> {
            System.out.println("There are no orders!");
        });
        orders.stream().map(Orders::toString).forEach(OrderService.getLOG()::info);
    }

    /**
     * Deletes an Order.
     *
     * @throws Exception   if the id of the Order doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteOrder() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.parseInt(bufferRead.readLine());
            Optional<Orders> o = orderServiceI.delete(id);
            o.orElseThrow(Exception::new);
        } catch (IOException ex) {
            ClientService.getLOG().info(ex.getMessage());
        } catch (NumberFormatException ex) {
            ClientService.getLOG().info("Please input an integer value!");
        } catch (Exception e) {
            ClientService.getLOG().info("No Order with this ID was found!");
        }
    }

    /**
     * Updates an order
     *
     * @throws IllegalArgumentException if the order is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateOrder() {
        Orders order = readOrder();
        try {
            Optional<Orders> o = Optional.ofNullable(order);
            o.orElseThrow(Exception::new);
        } catch (Exception e) {
            OrderService.getLOG().info("Can't have a null order!");
            return;
        }
        try {
            Optional<Orders> o = orderServiceI.update(order);
            o.orElseThrow(Exception::new);
        } catch (ValidatorException e) {
            ClientService.getLOG().info(e.getMessage());
        } catch (Exception e) {
            ClientService.getLOG().info("Order with this ID doesn't exist or nonexistent restaurantID/dishID/clientID!");
        }
    }

    /**
     * Prints all the sorted orders from the list received
     */
    private void sortOrders() {
        Comparator<Orders> func = Comparator.comparing(Orders::getDate, Date::compareTo).reversed();
        List<Orders> orders = orderServiceI.sortFunction(func);
        Optional<Integer> op = Optional.of(orders.size());
        op.filter(x -> x == 0).ifPresent(s -> {
            System.out.println("No order added yet!");
        });
        orders.forEach(System.out::println);
    }

    /**
     * Filters the Clients after the paymentType
     * The functions gets as input from the keyboard a paymentType
     * returns the list of orders payed in that manner
     */
    private void filterOrders() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("PaymentType:");
            String paymentType = bufferedReader.readLine();
            Predicate<Orders> filterA = a -> a.getPaymentType().equals(paymentType);
            List<Orders> orders = orderServiceI.filterFunction(filterA);
            Optional<Integer> op = Optional.of(orders.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("No orders with that paymentType!");
            });
            orders.forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
