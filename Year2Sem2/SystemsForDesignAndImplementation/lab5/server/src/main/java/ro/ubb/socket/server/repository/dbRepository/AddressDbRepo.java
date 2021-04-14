package ro.ubb.socket.server.repository.dbRepository;

import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.InMemoryRepository;
import ro.ubb.socket.server.repository.Repository;
import ro.ubb.socket.server.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AddressDbRepo implements Repository<Integer, Address> {
    private final DatabaseConnection connection;
    private final Validator<Address> validator;

    /**
     * Constructor for the AddressDbRepo
     *
     * @param validator the validator
     */
    public AddressDbRepo(Validator<Address> validator) {
        this.validator = validator;
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Find the address with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Address> findOne(Integer id) {
        String sql = "select * from address where addressId=?";
        Address a = null;
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("AddressId");
                String city = resultSet.getString("City");
                String street = resultSet.getString("Street");
                int number = resultSet.getInt("Number");
                String ziCode = resultSet.getString("zipCode");
                a = new Address(city, street, number, ziCode);
                a.setId(id);
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return Optional.of(a);
    }

    @Override
    public Map<Integer, Address> getEntities() {
        return null;
    }

    /**
     * @return all addresses from the database
     */
    @Override
    public Iterable<Address> findAll() {
        List<Address> addresses = new ArrayList<>();
        String sql = "select * from address";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("addressid");
                String city = resultSet.getString("city");
                String street = resultSet.getString("street");
                int number = resultSet.getInt("number");
                String zipCode = resultSet.getString("zipcode");
                Address a = new Address(city, street, number, zipCode);
                a.setId(id);
                addresses.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return addresses;
    }

    /**
     * Saves the given address in table.
     *
     * @param a - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Address> save(Address a) throws ValidatorException, IllegalAccessException {
        this.validator.validate(a);
        System.out.println("save");
        System.out.println(a);
        String sql = "insert into address values(?,?,?,?,?)";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            System.out.println("connection");
            statement.setInt(1, a.getId());
            statement.setString(2, a.getCity());
            statement.setString(3, a.getStreet());
            statement.setInt(4, a.getNumber());
            statement.setString(5, a.getZipCode());
            statement.executeUpdate();
            System.out.println("finish");
            return Optional.empty();
        } catch (SQLException e) {
            System.out.println("not ok");
            return Optional.of(a);
        }
    }

    /**
     * Delete from table the address with the given id (if exists)
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Address> delete(Integer id) {
        Optional<Address> a = this.findOne(id);
        try {
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from address where addressid=?";
        try (
                PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ;
        }
        return a;
    }

    /**
     * Updates the given address.
     *
     * @param a must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Address> update(Address a) throws ValidatorException {
        this.validator.validate(a);
        Optional<Address> address = this.findOne(a.getId());
        try {
            address.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "update address set city=?,street=?,number=?,zipcode=? where addressId=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setString(1, a.getCity());
            statement.setString(2, a.getStreet());
            statement.setInt(3, a.getNumber());
            statement.setString(4, a.getZipCode());
            statement.setInt(5, a.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return address;
    }

    @Override
    public int size() {
        return 0;
    }

    public void emptyTable() {
        String sql = "delete from address";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
