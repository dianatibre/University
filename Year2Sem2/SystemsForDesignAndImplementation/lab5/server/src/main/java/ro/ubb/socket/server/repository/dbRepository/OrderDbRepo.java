package ro.ubb.socket.server.repository.dbRepository;


import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.InMemoryRepository;
import ro.ubb.socket.server.repository.Repository;
import ro.ubb.socket.server.utils.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderDbRepo implements Repository<Integer, Order> {
    private final DatabaseConnection connection;
    private final Validator<Order> validator;

    /**
     * Constructor for the OrderDbRepo
     *
     * @param validator the validator
     */
    public OrderDbRepo(Validator<Order> validator) {
        this.validator = validator;
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Find the order with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Order> findOne(Integer id) {
        String sql = "select * from orderDB where orderId=?";
        Order order = null;
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("OrderId");
                int restaurantId = resultSet.getInt("RestaurantId");
                int clientId = resultSet.getInt("ClientId");
                int dishId = resultSet.getInt("DishId");
                String paymentType = resultSet.getString("PaymentType");
                Date date = resultSet.getDate("OrderDate");
                order = new Order(restaurantId, clientId, dishId, paymentType, date);
                order.setId(id);
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return Optional.of(order);
    }

    @Override
    public Map<Integer, Order> getEntities() {
        return null;
    }

    /**
     * @return all orders from the database
     */
    @Override
    public Iterable<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "select * from orderDB";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("OrderId");
                int restaurantId = resultSet.getInt("RestaurantId");
                int clientId = resultSet.getInt("ClientId");
                int dishId = resultSet.getInt("DishId");
                String paymentType = resultSet.getString("PaymentType");
                Date date = resultSet.getDate("OrderDate");
                Order order = new Order(restaurantId, clientId, dishId, paymentType, date);
                order.setId(id);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    /**
     * Saves the given order in table.
     *
     * @param order - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Order> save(Order order) throws ValidatorException, IllegalAccessException {
        this.validator.validate(order);
        String sql = "insert into orderDB values(?,?,?,?,?,?)";
        Date sqlDate = new Date(order.getDate().getTime());
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, order.getId());
            statement.setInt(2, order.getRestaurantID());
            statement.setInt(3, order.getClientID());
            statement.setInt(4, order.getDishID());
            statement.setString(5, order.getPaymentType());
            statement.setDate(6, sqlDate);
            statement.execute();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(order);
        }
    }

    /**
     * Delete from table the order with the given id (if exists)
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Order> delete(Integer id) {
        Optional<Order> order = this.findOne(id);
        try {
            order.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from orderDB where orderid=?";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    /**
     * Updates the given client.
     *
     * @param order must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Order> update(Order order) throws ValidatorException {
        this.validator.validate(order);
        Optional<Order> orderEntity = this.findOne(order.getId());
        try {
            orderEntity.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "update orderDB set restaurantId=?,clientId=?,dishId=?,paymentType=?,orderDate=? where OrderId=?";
        Date sqlDate = new Date(order.getDate().getTime());
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.setInt(1, order.getRestaurantID());
            statement.setInt(2, order.getClientID());
            statement.setInt(3, order.getDishID());
            statement.setString(4, order.getPaymentType());
            statement.setDate(5, sqlDate);
            statement.setInt(6, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderEntity;
    }

    @Override
    public int size() {
        return 0;
    }

    public void emptyTable() {
        String sql = "delete from order";
        try (PreparedStatement statement = connection.getConnectionInstance().prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
