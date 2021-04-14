package lab7.ui;

import lab7.domain.Address;
import lab7.domain.Client;
import lab7.domain.Orders;
import lab7.domain.validators.ValidatorException;
import lab7.service.ClientService;
import lab7.service.ServiceI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;

public class ClientConsole {
    private final ServiceI<Integer, Client> clientServiceI;
    private final ServiceI<Integer, Orders> orderServiceI;
    private final Console console;

    /**
     * ClientConsole constructor
     *
     * @param clientService the service for the ClientService
     * @param console       the main console
     */
    public ClientConsole(ServiceI<Integer, Client> clientService, ServiceI<Integer, Orders> orderServiceI, Console console) {
        this.clientServiceI = clientService;
        this.orderServiceI = orderServiceI;
        this.console = console;
    }

    /**
     * The menu for Client.
     */
    protected void printClientMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new Client. \n";
            str += "\t 2. Get all Clients. \n";
            str += "\t 3. Delete an Client. \n";
            str += "\t 4. Update an Client. \n";
            str += "\t 5. Sort Clients after their age. \n";
            str += "\t 6. Filter Clients after the age. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getClientCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the Client's commands
     */
    private Map<String, Runnable> getClientCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addClient);
        commands.put("2", this::getClients);
        commands.put("3", this::deleteClient);
        commands.put("4", this::updateClient);
        commands.put("5", this::sortClients);
        commands.put("6", this::filterClients);
        return commands;
    }

    /**
     * Adds an Client.
     *
     * @throws ValidatorException if the Client is null
     */
    private void addClient() {
        Client Client = readClient();
        try {
            Optional<Client> a = Optional.ofNullable(Client);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            ClientService.getLOG().info("Can't have a null Client!");
            return;
        }
        try {
            Optional<Client> a = clientServiceI.add(Client);
            a.ifPresent(x -> {
                ClientService.getLOG().info("Client already exists or nonexistent addressId!");
            });
        } catch (ValidatorException e) {
            ClientService.getLOG().info(e.getMessage());
        }
    }

    /**
     * The function reads an Client from keyboard.
     *
     * @return null in case of exception,otherwise the dish read
     * @throws IOException if the input from keyboard is not ok
     */
    private Client readClient() {
        System.out.println("Read Client {name,age,addressId,email}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Name:");
            String name = bufferedReader.readLine();
            System.out.println("Age");
            int age = Integer.parseInt(bufferedReader.readLine());
            System.out.println("AddressId");
            int number = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Email:");
            String email = bufferedReader.readLine();
            Client Client = new Client(name, age, number, email);
            Client.setId(id);
            return Client;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the Clients.
     */
    private void getClients() {
        List<Client> Clients = clientServiceI.get();
        Optional<Integer> op = Optional.of(Clients.size());
        op.filter(x -> x == 0).ifPresent(s -> {
            System.out.println("There are no Clients!");
        });
        Clients.stream().map(Client::toString).forEach(ClientService.getLOG()::info);
    }

    /**
     * Deletes an Client.
     *
     * @throws Exception   if the id of the Client doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteClient() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.parseInt(bufferRead.readLine());
            Optional<Client> a = clientServiceI.delete(id);
            a.orElseThrow(Exception::new);
            orderServiceI.deleteOrderWhenDeletingClient(id);
        } catch (IOException ex) {
            ClientService.getLOG().info(ex.getMessage());
        } catch (NumberFormatException ex) {
            ClientService.getLOG().info("Please input an integer value!");
        } catch (Exception e) {
            ClientService.getLOG().info("No Client with this ID was found!");
        }
    }

    /**
     * Updates an Client
     *
     * @throws IllegalArgumentException if the Client is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateClient() {
        Client Client = readClient();
        try {
            Optional<Client> a = Optional.ofNullable(Client);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            ClientService.getLOG().info("Can't have a null Client!");
            return;
        }
        try {
            Optional<Client> a = clientServiceI.update(Client);
            a.orElseThrow(Exception::new);
        } catch (ValidatorException e) {
            ClientService.getLOG().info(e.getMessage());
        } catch (Exception e) {
            ClientService.getLOG().info("Client with this ID doesn't exist or nonexistent addressId!");
        }
    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortClients() {
        Comparator<Client> func = Comparator.comparing(Client::getAge, Integer::compareTo).reversed();
        List<Client> Clients = clientServiceI.sortFunction(func);
        Optional<Integer> op = Optional.of(Clients.size());
        op.filter(x -> x == 0).ifPresent(s -> {
            System.out.println("No Client added yet!");
        });
        Clients.forEach(System.out::println);
    }

    /**
     * Filters the Clients after the city name
     * The functions gets as input from the keyboard a city name
     * returns the list of Clients in that city
     */
    private void filterClients() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Age:");
            int age = Integer.parseInt(bufferedReader.readLine());
            Predicate<Client> filterA = a -> a.getAge() == age;
            List<Client> Clients = clientServiceI.filterFunction(filterA);
            Optional<Integer> op = Optional.of(Clients.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("No Client with this age!");
            });
            Clients.forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
