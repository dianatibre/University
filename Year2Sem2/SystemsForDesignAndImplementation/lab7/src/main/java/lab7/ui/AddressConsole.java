package lab7.ui;

import lab7.domain.Address;
import lab7.domain.Client;
import lab7.domain.validators.ValidatorException;
import lab7.service.AddressService;
import lab7.service.ServiceI;
import lab7.domain.Orders;

import javax.persistence.criteria.Order;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;

public class AddressConsole {
    private ServiceI<Integer, Address> addressServiceI;
    private ServiceI<Integer, Client> clientServiceI;
    private ServiceI<Integer, Orders> orderServiceI;
    private final Console console;

    /**
     * AddressConsole constructor
     *
     * @param addressService the service for the addressService
     * @param console        the main console
     */
    public AddressConsole(ServiceI<Integer,Address> addressService, ServiceI<Integer, Client> clientServiceI,ServiceI<Integer, Orders> orderServiceI,Console console) {
        this.addressServiceI = addressService;
        this.clientServiceI=clientServiceI;
        this.orderServiceI=orderServiceI;
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
     * @throws Exception if the address is null
     */
    private void addAddress() {
        Address address = readAddress();
        try {
            Optional<Address> a = Optional.ofNullable(address);
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            AddressService.getLOG().info("Can't have a null address!");
            return;
        }
        try {
            Optional<Address> a = addressServiceI.add(address);
            a.ifPresent(x -> {
                AddressService.getLOG().info("Address already exists!");
            });
        } catch (ValidatorException e) {
            AddressService.getLOG().info(e.getMessage());
        }
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
        List<Address> addresses = addressServiceI.get();
        Optional<Integer> op = Optional.of(addresses.size());
        op.filter(x -> x == 0).ifPresent(s -> {
            System.out.println("There are no addresses!");
        });
        addresses.stream().map(Address::toString).forEach(AddressService.getLOG()::info);
    }

    /**
     * Deletes an address.
     *
     * @throws Exception   if the id of the address doesn't exist
     * @throws IOException if the input is not ok
     */
    private void deleteAddress() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            int id = Integer.valueOf(bufferRead.readLine());

            try {
                this.orderServiceI.deleteAllById(id);
                this.clientServiceI.deleteAllById(id);
            } catch (Exception e) {
            }
            Optional<Address> a = addressServiceI.delete(id);
            a.orElseThrow(Exception::new);
        } catch (IOException ex) {
            AddressService.getLOG().info(ex.getMessage());
        } catch (NumberFormatException ex) {
            AddressService.getLOG().info("Please input an integer value!");
        } catch (Exception e) {
            AddressService.getLOG().info("No address with this ID was found!");
            return;
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
            try {
                Optional<Address> a = Optional.ofNullable(address);
                a.orElseThrow(Exception::new);
            }
            catch (Exception e) {
                AddressService.getLOG().info("Can't have a null address!");
                return;
            }
            try {
                Optional<Address> a = addressServiceI.update(address);
                a.orElseThrow(Exception::new);
            }
            catch (ValidatorException e) {
                AddressService.getLOG().info(e.getMessage());
            }
            catch (Exception e) {
                AddressService.getLOG().info("Address with this ID doesn't exist!");
            }
        }

        /**
         * Prints all the sorted clients from the list received
         */
        private void sortAddresses () {
            Comparator<Address> func = Comparator.comparing(Address::getCity,String::compareTo).reversed();
            List<Address> addresses = addressServiceI.sortFunction(func);
            Optional<Integer> op = Optional.of(addresses.size());
            op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No address added yet!");});
            addresses.stream().forEach(System.out::println);
        }

        /**
         * Filters the addresses after the city name
         * The functions gets as input from the keyboard a city name
         * returns the list of addresses in that city
         */
        private void filterAddresses () {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("City:");
                String city = String.valueOf(bufferedReader.readLine());
                Predicate<Address> filterA = a -> a.getCity().equals(city);
                List<Address> addresses = addressServiceI.filterFunction(filterA);
                Optional<Integer> op = Optional.of(addresses.size());
                op.filter(x -> x == 0).ifPresent( s -> {System.out.println("No address with this city!");});
                addresses.stream().forEach(System.out::println);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
