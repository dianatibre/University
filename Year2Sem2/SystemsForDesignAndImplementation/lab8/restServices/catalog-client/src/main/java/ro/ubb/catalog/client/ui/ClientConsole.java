package ro.ubb.catalog.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.ubb.catalog.client.rest.RestService;
import ro.ubb.catalog.core.model.Address;
import ro.ubb.catalog.core.model.Client;
import ro.ubb.catalog.core.model.validators.ValidatorException;
import ro.ubb.catalog.core.service.ServiceI;
import ro.ubb.catalog.web.dto.AddressDto;
import ro.ubb.catalog.web.dto.AddressesDto;
import ro.ubb.catalog.web.dto.ClientDto;
import ro.ubb.catalog.web.dto.ClientsDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ClientConsole {
    private RestService restService;
    private ServiceI<Integer, Client> clientServiceI;
    private Console console;
    private static final Logger LOG = LoggerFactory.getLogger(
            ClientConsole.class);

    /**
     * ClientConsole constructor
     *
     * @param clientService the service for the addressService
     * @param console        the main console
     */
    public ClientConsole(ServiceI<Integer,Client> clientService,Console console,RestService restService) {
        this.clientServiceI = clientService;
        this.restService=restService;
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
            str += "\t 5. Sort clients after their name. \n";
            str += "\t 6. Filter clients after the name. \n";
            str += "\t 7. Filter clients after the name and age. \n";
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
        commands.put("7", this::sortClients2);
        return commands;
    }

    private void sortClients2() {
        LOG.info("sortClients--- method entered");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Order: (asc/desc)");
        String order = null;
        try {
            order = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (order.equals("asc")) {
            ClientsDto clients = restService.sortClientsNameEmail();
            Optional<Integer> op = Optional.of(clients.getClients().size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("No clients added yet!");
            });
            clients.getClients().stream().map(ClientDto::toString).forEach(LOG::info);
        }
        else if (order.equals("desc")) {
            ClientsDto clients = restService.sortClientsNameEmailDesc();
            Optional<Integer> op = Optional.of(clients.getClients().size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("No clients added yet!");
            });
            clients.getClients().stream().map(ClientDto::toString).forEach(LOG::info);
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
        LOG.info("saveClient: client={}", client);
        try {
            Optional<Client> a = Optional.ofNullable(client);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null client!");
            return;
        }
        try {
            ClientDto newClient = ClientDto.builder().name(client.getName()).age(client.getAge()).email(client.getEmail()).address(client.getAddress()).build();
            newClient.setId(client.getId());
            Optional<ClientDto> c = restService.addClient(newClient);
            c.ifPresent(x->LOG.info("Client already exists"));
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception ex) {
            LOG.info("Validation exception!");
        }
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
     * Prints the clients.
     */
    private void getClients() {
        LOG.info("getAllClients --- method entered");
        ClientsDto clients = restService.getClients();
        Optional<Integer> op = Optional.of(clients.getClients().size());
        op.filter(x -> x == 0).ifPresent(s -> {
            LOG.info("There are no clients!");
        });
        clients.getClients().stream().map(ClientDto::toString).forEach(LOG::info);
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
            int id = Integer.valueOf(bufferedReader.readLine());
            LOG.info("deleteClient: id={}",id);
            restService.deleteClient(id);
            LOG.info("deleteClient --- method finished");
        }
        catch (IOException ex) {
            LOG.info(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOG.info("Please input an integer value!");
        } catch (Exception e) {
            LOG.info("No client with this ID was found!");
            return;
        }
    }

    /**
     * Updates a client
     *
     * @throws IllegalArgumentException if the client is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateClient() {
        Client client=readClient();
        LOG.info("updateClient: client={}",client);
        try {
            Optional<Client> a = Optional.ofNullable(client);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            LOG.info("Can't have a null client!");
            return;
        }
        try {
            ClientDto newClient = ClientDto.builder().name(client.getName()).age(client.getAge()).email(client.getEmail()).address(client.getAddress()).build();
            newClient.setId(client.getId());
            restService.updateClient(newClient, client.getId());
        } catch (ValidatorException e) {
            LOG.info(e.getMessage());
        } catch (Exception e) {
            LOG.info("Client with this ID doesn't exist!");
        }

    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortClients() {
        LOG.info("sortClients--- method entered");
        ClientsDto clients = restService.sortClientsName();
        Optional<Integer> op = Optional.of(clients.getClients().size());
        op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No clients added yet!");});
        clients.getClients().stream().map(ClientDto::toString).forEach(LOG::info);

    }

    /**
     * Filters the clients after the age value
     * The functions gets as input from the keyboard an age value
     * returns the list of clients with that age
     */
    private void filterClients() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Name:");
            String name = String.valueOf(bufferedReader.readLine());
            ClientsDto clients=restService.filterNameClient(name);
            Optional<Integer> op = Optional.of(clients.getClients().size());
            op.filter(x -> x == 0).ifPresent( s -> System.out.println("No clients with this name!"));
            clients.getClients().stream().map(ClientDto::toString).forEach(LOG::info);
        } catch (IOException ioException) {
            LOG.info(ioException.getMessage());
        }
    }
}
