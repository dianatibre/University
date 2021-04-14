package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.ClientService;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

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
     * The menu for client.
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
            str += "\t 5. Sort clients after their name\n";
            str += "\t 6. Filter the clients after an age\n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getClientCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the client's commands
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
     * filter dishes after a specific rating taken as input from keyboard
     */
    private void filterClients() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Age:");
            int age = Integer.parseInt(bufferedReader.readLine());
            try {
                clientService.filter(age).thenAccept(rentals -> {
                    Optional<Integer> op = Optional.of(rentals.size());
                    op.filter(x -> x == 0).ifPresent(s -> {
                        System.out.println("No client has the given value for this age!");
                    });
                    rentals.forEach(System.out::println);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
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
            Optional<Client> r = Optional.ofNullable(client);
            r.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't have a null client!");
            return;
        }
        clientService.add(client).thenAccept(c -> {
            c.ifPresent(x -> {
                System.out.println("Client already exists or no address with this id exists!");
            });
        })
                .whenComplete((r, ex) -> {
                    if (ex != null)
                        System.out.println(ex.getMessage());
                });
    }

    /**
     * The function reads a client from keyboard.
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
            System.out.println("AddressId:");
            Integer capacity = Integer.valueOf(bufferedReader.readLine());
            System.out.println("Email:");
            String email = String.valueOf(bufferedReader.readLine());
            Client client = new Client(name, age, capacity, email);
            client.setId(id);
            return client;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the client.
     */
    private void getClients() {
        clientService.get().thenAccept(clients -> {
            Optional<Integer> op = Optional.of(clients.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("There are no clients!");
            });
            clients.forEach(System.out::println);
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
            clientService.delete(id)
                    .whenComplete((r, ex) -> {
                        if (ex != null)
                            System.out.println("No clients with this ID was found!");
                    });
        } catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            System.out.println("No client with this Id");
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
        try {
            Optional<Client> r = Optional.ofNullable(client);
            r.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't update a null client!");
            return;
        }
        try {
            clientService.update(client)
                    .whenComplete((r, ex) -> {
                        if (ex != null)
                            System.out.println(ex.getMessage());
                    });
        } catch (ValidatorException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortClients() {
        clientService.sort().thenAccept(clients -> {
            Optional<Integer> op = Optional.of(clients.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("No clients added yet!");
            });
            clients.forEach(System.out::println);
        });
    }
}
