package ro.ubb.socket.server.repository.dbRepository;



import ro.ubb.socket.common.domain.Restaurant;
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

public class RestaurantDbRepo implements Repository<Integer, Restaurant> {
    private final DatabaseConnection connection;
    private final Validator<Restaurant> validator;

    /**
     * Constructor for the RestaurantDbRepo
     *
     * @param validator the validator
     */
    public RestaurantDbRepo(Validator<Restaurant> validator) {
        this.validator = validator;
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Find the restaurant with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Restaurant> findOne(Integer id) {
        String sql = "select * from restaurant where restaurantId=?";
        Restaurant r = null;
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("RestaurantId");
                String name = resultSet.getString("Name");
                int rating = resultSet.getInt("Rating");
                int capacity = resultSet.getInt("Capacity");
                Boolean delivery = resultSet.getBoolean("Delivery");
                r = new Restaurant(name, rating, capacity, delivery);
                r.setId(id);
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return Optional.of(r);
    }

    @Override
    public Map<Integer, Restaurant> getEntities() {
        return null;
    }

    /**
     * @return all restaurants from the database
     */
    @Override
    public Iterable<Restaurant> findAll() {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "select * from restaurant";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("RestaurantId");
                String name = resultSet.getString("Name");
                int rating = resultSet.getInt("Rating");
                int capacity = resultSet.getInt("Capacity");
                Boolean delivery = resultSet.getBoolean("Delivery");
                Restaurant r = new Restaurant(name, rating, capacity, delivery);
                r.setId(id);
                restaurants.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return restaurants;
    }

    /**
     * Saves the given restaurant in table.
     *
     * @param r - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Restaurant> save(Restaurant r) throws ValidatorException, IllegalAccessException {
        this.validator.validate(r);
        String sql = "insert into restaurant values(?,?,?,?,?)";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, r.getId());
            statement.setString(2, r.getName());
            statement.setInt(3, r.getRating());
            statement.setInt(4, r.getCapacity());
            statement.setBoolean(5, r.getDelivery());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(r);
        }
    }

    /**
     * Delete from table the restaurant with the given id (if exists)
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Restaurant> delete(Integer id) {
        Optional<Restaurant> r = this.findOne(id);
        try {
            r.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from restaurant where restaurantId=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * Updates the given restaurant.
     *
     * @param r must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Restaurant> update(Restaurant r) throws ValidatorException {
        this.validator.validate(r);
        Optional<Restaurant> restaurant = this.findOne(r.getId());
        try {
            restaurant.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "update restaurant set name=?,rating=?,capacity=?,delivery=? where restaurantId=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setString(1, r.getName());
            statement.setInt(2, r.getRating());
            statement.setInt(3, r.getCapacity());
            statement.setBoolean(4, r.getDelivery());
            statement.setInt(5, r.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return restaurant;
    }

    @Override
    public int size() {
        return 0;
    }

    public void emptyTable() {
        String sql = "delete from restaurant";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

