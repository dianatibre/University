package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.AddressService;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddressConsole {
    private final AddressService addressService;
    private final Console console;

    /**
     * AddressConsole constructor
     *
     * @param addressService the service for the addressService
     * @param console        the main console
     */
    public AddressConsole(AddressService addressService, Console console) {
        this.addressService = addressService;
        this.console = console;
    }

    /**
     * The menu for address.
     */
    protected void printAddressMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = "";
            str += "\nPlease choose an option: \n";
            str += "\t 0. Back \n";
            str += "\t 1. Add a new address. \n";
            str += "\t 2. Get all addresses. \n";
            str += "\t 3. Delete an address. \n";
            str += "\t 4. Update an address. \n";
            str += "\t 5. Sort addresses after their city. \n";
            str += "\t 6. Filter addresses after the city. \n";
            System.out.println(str);
            System.out.println("Input the option:");
            String key = scanner.nextLine();
            getAddressCommands().getOrDefault(key, () -> System.out.println("Invalid command!")).run();
        }
    }

    /**
     * @return the address's commands
     */
    private Map<String, Runnable> getAddressCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("0", console::printOptions);
        commands.put("1", this::addAddress);
        commands.put("2", this::getAddresses);
        commands.put("3", this::deleteAddress);
        commands.put("4", this::updateAddress);
        commands.put("5", this::sortAddresses);
        commands.put("6", this::filterAddresses);
        return commands;
    }

    /**
     * Adds an address.
     *
     * @throws Exception          if the address is null
     * @throws ValidatorException if the entity is not a valid one.
     */
    private void addAddress() {
        Address address = readAddress();
        try {
            Optional<Address> a = Optional.ofNullable(address);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            System.out.println("Can't have a null address!");
            return;
        }
        console.getExecutorService().submit(() -> {
            try {
                Optional<Address> a = Optional.ofNullable(addressService.myAdd(address).get().getValue());
                a.ifPresent(x ->
                        System.out.println("Address already existed!/Validation Exception")
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * The function reads an address from keyboard.
     *
     * @return null in case of exception,otherwise the dish read
     * @throws IOException if the input from keyboard is not ok
     */
    private Address readAddress() {
        System.out.println("Read Address {city,street,number,zipcode}");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            System.out.println("City:");
            String city = bufferedReader.readLine();
            System.out.println("Street");
            String street = bufferedReader.readLine();
            System.out.println("Number");
            int number = Integer.parseInt(bufferedReader.readLine());
            System.out.println("ZipCode:");
            String zipcode = bufferedReader.readLine();
            Address address = new Address(city, street, number, zipcode);
            address.setId(id);
            return address;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Prints the addressees.
     */
    private void getAddresses() {
        console.getExecutorService().submit(() -> {
            CompletableFuture<List<Address>> addresses = addressService.myGet();
            Optional<Integer> op;
            try {
                op = Optional.of(addresses.get().size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("There are no addresses!");
                });
                addresses.get().forEach(System.out::println);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deletes an address.
     *
     * @throws Exception   if the id of the address doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteAddress() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Id:");
            Integer id = Integer.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                try {
                    Optional<Address> a = Optional.ofNullable(addressService.myDelete(id).get().getValue());
                    a.orElseThrow(Exception::new);
                } catch (Exception e) {
                    System.out.println("Address doesn't exists/Validation exception");
                }
            });
        } catch (NumberFormatException ex) {
            System.out.println("Please input an integer value!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Updates an address
     *
     * @throws IllegalArgumentException if the address is null
     * @throws ValidatorException       if the entity is not a valid one.
     **/
    private void updateAddress() {
        Address address = readAddress();
        console.getExecutorService().submit(() -> {
            try {
                Optional<Address> a = Optional.ofNullable(address);
                a.orElseThrow(Exception::new);
                Optional<Address> c = Optional.ofNullable(addressService.myUpdate(address).get().getValue());
                c.orElseThrow(Exception::new);
            } catch (Exception e) {
                System.out.println("Address with this ID doesn't exist!/Validation Exception!");
                return;
            }
        });

    }

    /**
     * Prints all the sorted clients from the list received
     */
    private void sortAddresses() {
        console.getExecutorService().submit(() -> {
            List<Address> addresses = addressService.sortAddressCity();
            Optional<Integer> op = Optional.of(addresses.size());
            op.filter(x -> x == 0).ifPresent(s -> {
                System.out.println("There are no addresses!");
            });
            addresses.forEach(System.out::println);
        });
    }

    /**
     * Filters the addresses after the city name
     * The functions gets as input from the keyboard a city name
     * returns the list of addresses in that city
     */
    private void filterAddresses() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("City:");
            String city = String.valueOf(bufferedReader.readLine());
            console.getExecutorService().submit(() -> {
                List<Address> addresses = addressService.filterAddressCity(city);
                Optional<Integer> op = Optional.of(addresses.size());
                op.filter(x -> x == 0).ifPresent(s -> {
                    System.out.println("No address with this city");
                });
                addresses.forEach(System.out::println);
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
