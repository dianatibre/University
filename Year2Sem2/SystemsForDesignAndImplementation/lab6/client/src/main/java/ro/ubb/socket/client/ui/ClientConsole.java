package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.AddressService;
import ro.ubb.socket.client.service.ClientService;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ClientConsole {

    private final ClientService clientService;
    private final Console console;

    /**
     * ClientConsole constructor
     *
     * @param clientService the service for the clientService
     * @param console       the main console
     */
    public ClientConsole(ClientService clientService, Console console) {
        this.clientService = clientService;
        this.console = console;
    }

    /**
     * The menu for address.
     */
    protected void printClientMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new client. \n";
            str += "\t 2. Get all clients. \n";
            str += "\t 3. Delete a client. \n";
            str += "\t 4. Update a client. \n";
            str += "\t 5. Sort clients after their age. \n";
            str += "\t 6. Filter clients after the age. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getClientCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the address's commands
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
     * Adds a client.
     *
     * @throws Exception          if the client is null
     * @throws ValidatorException if the entity is not a valid one.
     */
    private void addClient() {
        Client client = readClient();
        try {
            Optional<Client> a = Optional.ofNullable(client);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't have a null client!");
            return;
        }
        console.getExecutorService().submit(() -> {
            Optional<Client> a;
            try {
                a = Optional.ofNullable(clientService.myAdd(client).get().getValue());
                a.ifPresent(x -> {
                    System.out.println("Client already existed!/Validation Exception");
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * The function reads a client from keyboard.
     *
     * @return null in case of exception,otherwise the client read
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
            int addressId = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Email:");
            String email = bufferedReader.readLine();
            Client client = new Client(name, age, addressId, email);
            client.setId(id);
            return client;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the addressees.
     */
    private void getClients() {
        console.getExecutorService().submit(() -> {
            CompletableFuture<List<Client>> clients = clientService.myGet();
            Optional<Integer> op;
            try {
                op = Optional.of(clients.get().size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("There are no clients!");
                });
                clients.get().forEach(System.out::println);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deletes a client.
     *
     * @throws Exception   if the id of the client doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteClient() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                try {
                    Optional<Client> a = Optional.ofNullable(clientService.myDelete(id).get().getValue());
                    a.orElseThrow(Exception::new);
                } catch (Exception e) {
                    System.out.println("Client doesn't exists/Validation exception");
                }
            });
        } catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Updates a client
     *
     * @throws IllegalArgumentException if the client is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateClient() {
        Client client = readClient();
        console.getExecutorService().submit(() -> {
            try {
                Optional<Client> a = Optional.ofNullable(client);
                a.orElseThrow(Exception::new);
                Optional<Client> c = Optional.ofNullable(clientService.myUpdate(client).get().getValue());
                c.orElseThrow(Exception::new);
            } catch (Exception e) {
                System.out.println("Client with this ID doesn't exist!/Validation Exception!");
            }
        });

    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortClients() {
        console.getExecutorService().submit(() -> {
            List<Client> clients = clientService.sortClientAge();
            Optional<Integer> op = Optional.of(clients.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("There are no clients!");
            });
            clients.forEach(System.out::println);
        });
    }

    /**
     * Filters the clients after the age value
     * The functions gets as input from the keyboard an age value
     * returns the list of clients with that age
     */
    private void filterClients() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Age:");
            int age = Integer.parseInt(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                List<Client> clients = clientService.filterClientAge(age);
                Optional<Integer> op = Optional.of(clients.size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("No client with this age");
                });
                clients.forEach(System.out::println);
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
