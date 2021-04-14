package ro.ubb.socket.server.repository.dbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.Dish;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class DishDbRepo implements Repository<Integer, Dish> {

    @Autowired
    private JdbcOperations jdbcOperations;
    private final Validator <Dish> validator;


    /**
     * Constructor for the DishDbRepo
     *
     * @param validator the validator
     */
    public DishDbRepo(Validator<Dish> validator) {
        this.validator = validator;
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
        String sql = "select * from dish where dishId=?";
        Dish dish;
        dish = jdbcOperations.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rowNumber) -> {
            int idd = rs.getInt("dishId");
            String name = rs.getString("name");
            float price = rs.getFloat("price");
            int quantity = rs.getInt("quantity");
            String category = rs.getString("category");
            Dish dish1 = new Dish(name, price, quantity, category);
            dish1.setId(idd);
            return dish1;
        });
        return Optional.of(dish);
    }

    @Override
    public Map<Integer, Dish> getEntities() {
        return null;
    }

    /**
     * @return all dishes from the database
     */
    @Override
    public List<Dish> findAll() {
        String sql = "select * from dish";
        return jdbcOperations.query(sql, (rs, rowNum) -> {
            int idd = rs.getInt("dishId");
            String name = rs.getString("name");
            float price = rs.getFloat("price");
            int quantity = rs.getInt("quantity");
            String category = rs.getString("category");
            Dish dish = new Dish(name, price, quantity, category);
            dish.setId(idd);
            return dish;
        });
    }

    /**
     * Saves the given dish in table.
     *
     * @param dish - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Dish> save(Dish dish) throws ValidatorException, IllegalAccessException {
        try {
            this.validator.validate(dish);
            String sql = "insert into dish values(?,?,?,?,?)";
            int i = jdbcOperations.update(sql, dish.getId(), dish.getName(), dish.getPrice(), dish.getQuantity(), dish.getCategory());
            if (i == 1)
                return Optional.empty();
            return Optional.of(dish);
        } catch (Exception e) {
            return Optional.of(dish);
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
        Optional<Dish> d = this.findOne(id);
        try {
            d.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from dish where dishId=?";
        jdbcOperations.update(sql, id);
        return d;
    }


    /**
     * Updates the given dish.
     *
     * @param dish must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Dish> update(Dish dish) throws ValidatorException {
        try {
            this.validator.validate(dish);
            String sql = "update dish set name=?,price=?,quantity=?,category=? where dishId=?";
            int i = jdbcOperations.update(sql, dish.getName(), dish.getPrice(), dish.getQuantity(), dish.getCategory(), dish.getId());
            if (i == 0)
                return Optional.empty();
            return Optional.of(dish);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int size() {
        return 0;
    }

}
