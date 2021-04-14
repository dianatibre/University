package ro.ubb.socket.server.repository.dbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.socket.common.domain.Restaurant;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestaurantDbRepo implements Repository<Integer, Restaurant> {
    @Autowired
    private JdbcOperations jdbcOperations;
    private final Validator<Restaurant> validator;

    /**
     * Constructor for the RestaurantDbRepo
     *
     * @param validator the validator
     */
    public RestaurantDbRepo(Validator<Restaurant> validator) {
        this.validator = validator;
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
        String sql = "select * from restaurant where restaurantid=?";
        Restaurant r = null;
        r = jdbcOperations.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rowNumber) -> {
            int idr = rs.getInt("restaurantid");
            String name = rs.getString("name");
            int rating = rs.getInt("rating");
            int capacity = rs.getInt("capacity");
            Boolean delivery = rs.getBoolean("delivery");
            Restaurant restaurant = new Restaurant(name, rating, capacity, delivery);
            restaurant.setId(idr);
            return restaurant;
        });
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
    public List<Restaurant> findAll() {
        String sql = "select * from restaurant";
        return jdbcOperations.query(sql, (rs, rowNum) -> {
            int idr = rs.getInt("restaurantid");
            String name = rs.getString("name");
            int rating = rs.getInt("rating");
            int capacity = rs.getInt("capacity");
            Boolean delivery = rs.getBoolean("delivery");
            Restaurant restaurant = new Restaurant(name, rating, capacity, delivery);
            restaurant.setId(idr);
            return restaurant;
        });
    }

    /**
     * Saves the given restaurant in table.
     *
     * @param a - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Restaurant> save(Restaurant a) throws ValidatorException, IllegalAccessException {
        try {
            this.validator.validate(a);
            String sql = "insert into restaurant values(?,?,?,?,?)";
            int i = jdbcOperations.update(sql, a.getId(), a.getName(), a.getRating(), a.getCapacity(), a.getDelivery());
            if (i == 1)
                return Optional.empty();
            return Optional.of(a);
        } catch (Exception e) {
            return Optional.of(a);
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
        Optional<Restaurant> a = this.findOne(id);
        try {
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from restaurant where restaurantid=?";
        jdbcOperations.update(sql, id);
        return a;
    }

    /**
     * Updates the given restaurant.
     *
     * @param a must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Restaurant> update(Restaurant a) throws ValidatorException {
        try {
            this.validator.validate(a);
            String sql = "update restaurant set name=?,rating=?,capacity=?,delivery=? where restaurantid=?";
            int i = jdbcOperations.update(sql, a.getName(), a.getRating(), a.getCapacity(), a.getDelivery(), a.getId());
            if (i == 0)
                return Optional.empty();
            return Optional.of(a);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int size() {
        return 0;
    }
}
