package ro.ubb.socket.server.repository.dbRepository;

import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.InMemoryRepository;
import ro.ubb.socket.server.repository.Repository;
import ro.ubb.socket.server.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientDbRepo implements Repository<Integer, Client> {
    private final DatabaseConnection connection;
    private final Validator<Client> validator;

    /**
     * Constructor for the ClientDbRepo
     *
     * @param validator the validator
     */
    public ClientDbRepo(Validator<Client> validator) {
        this.validator = validator;
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Find the client with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Client> findOne(Integer id) {
        String sql = "select * from client where clientId=?";
        Client c = null;
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("ClientId");
                String name = resultSet.getString("Name");
                int age = resultSet.getInt("Age");
                int addressId = resultSet.getInt("AddressId");
                String email = resultSet.getString("Email");
                c = new Client(name, age, addressId, email);
                c.setId(id);
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return Optional.of(c);
    }

    @Override
    public Map<Integer, Client> getEntities() {
        return null;
    }

    /**
     * @return all clients from the database
     */
    @Override
    public Iterable<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "select * from client";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("clientid");
                String name = resultSet.getString("Name");
                int age = resultSet.getInt("Age");
                int addressId = resultSet.getInt("AddressId");
                String email = resultSet.getString("Email");
                Client c = new Client(name, age, addressId, email);
                c.setId(id);
                clients.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }

    /**
     * Saves the given client in table.
     *
     * @param c - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Client> save(Client c) throws ValidatorException, IllegalAccessException {
        this.validator.validate(c);
        String sql = "insert into client values(?,?,?,?,?)";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, c.getId());
            statement.setString(2, c.getName());
            statement.setInt(3, c.getAge());
            statement.setInt(4, c.getAddress());
            statement.setString(5, c.getEmail());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(c);
        }
    }

    /**
     * Delete from table the client with the given id (if exists)
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Client> delete(Integer id) {
        Optional<Client> c = this.findOne(id);
        try {
            c.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from client where clientid=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Updates the given client.
     *
     * @param c must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Client> update(Client c) throws ValidatorException {
        this.validator.validate(c);
        Optional<Client> client = this.findOne(c.getId());
        try {
            client.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "update client set name=?,age=?,addressId=?,email=? where clientId=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setString(1, c.getName());
            statement.setInt(2, c.getAge());
            statement.setInt(3, c.getAddress());
            statement.setString(4, c.getEmail());
            statement.setInt(5, c.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return client;
    }

    @Override
    public int size() {
        return 0;
    }

    public void emptyTable() {
        String sql = "delete from client";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
