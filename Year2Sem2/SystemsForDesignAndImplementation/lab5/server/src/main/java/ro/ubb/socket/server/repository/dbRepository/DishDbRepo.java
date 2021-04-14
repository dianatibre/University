package ro.ubb.socket.server.repository.dbRepository;


import ro.ubb.socket.common.domain.Dish;
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

public class DishDbRepo implements Repository<Integer, Dish> {
    private final DatabaseConnection connection;
    private final Validator<Dish> validator;

    /**
     * Constructor for the DishDbRepo
     *
     * @param validator the validator
     */
    public DishDbRepo(Validator<Dish> validator) {
        this.validator = validator;
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Find the dish with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Dish> findOne(Integer id) {
        String sql = "select * from Dish where dishID=?";
        Dish d = null;
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("dishID");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                int quantity = resultSet.getInt("quantity");
                String category = resultSet.getString("category");
                d = new Dish(name, price, quantity, category);
                d.setId(id);
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return Optional.of(d);
    }

    @Override
    public Map<Integer, Dish> getEntities() {
        return null;
    }


    /**
     * @return all dishes from the database
     */
    @Override
    public Iterable<Dish> findAll() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "select * from dish";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("dishID");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                int quantity = resultSet.getInt("quantity");
                String category = resultSet.getString("category");
                Dish d = new Dish(name, price, quantity, category);
                d.setId(id);
                dishes.add(d);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dishes;
    }


    /**
     * Saves the given dish in table.
     *
     * @param d - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Dish> save(Dish d) throws ValidatorException, IllegalAccessException {
        this.validator.validate(d);
        String sql = "insert into dish values(?,?,?,?,?)";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, d.getId());
            statement.setString(2, d.getName());
            statement.setFloat(3, d.getPrice());
            statement.setInt(4, d.getQuantity());
            statement.setString(5, d.getCategory());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(d);
        }
    }


    /**
     * Delete from table the dish with the given id (if exists)
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Dish> delete(Integer id) {
        Optional<Dish> r = this.findOne(id);
        try {
            r.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from dish where dishID=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }


    /**
     * Updates the given dish.
     *
     * @param r must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Dish> update(Dish r) throws ValidatorException {
        this.validator.validate(r);
        Optional<Dish> dish = this.findOne(r.getId());
        try {
            dish.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "update restaurant set name=?,rating=?,capacity=?,delivery=? where restaurantId=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setString(1, r.getName());
            statement.setFloat(2, r.getPrice());
            statement.setInt(3, r.getQuantity());
            statement.setString(4, r.getCategory());
            statement.setInt(5, r.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dish;
    }

    @Override
    public int size() {
        return 0;
    }

    public void
    emptyTable() {
        String sql = "delete from dish";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

