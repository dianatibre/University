package ro.ubb.socket.server.repository.dbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.Order;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.Repository;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class OrderDbRepo implements Repository<Integer, Order> {
    @Autowired
    private JdbcOperations jdbcOperations;
    private final Validator<Order> validator;

    /**
     * Constructor for the OrderDbRepo
     *
     * @param validator the validator
     */
    public OrderDbRepo(Validator<Order> validator) {
        this.validator = validator;
    }

    @Override
    public Optional<Order> findOne(Integer id) {
        String sql = "select * from orderdb where orderid=?";
        Order o = null;
        o = jdbcOperations.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rowNumber) -> {
            int ido = rs.getInt("orderid");
            int idr = rs.getInt("restaurantid");
            int idc = rs.getInt("clientid");
            int idd = rs.getInt("dishid");
            String paymentType = rs.getString("paymenttype");
            Date orderdate = rs.getDate("orderdate");

            Order order = new Order(idr, idc, idd, paymentType, orderdate);
            order.setId(ido);
            return order;
        });
        return Optional.of(o);
    }

    @Override
    public Map<Integer, Order> getEntities() {
        return null;
    }

    @Override
    public Iterable<Order> findAll() {
        String sql = "select * from orderdb";
        return jdbcOperations.query(sql, (rs, rowNum) -> {
            int ido = rs.getInt("orderid");
            int idr = rs.getInt("restaurantid");
            int idc = rs.getInt("clientid");
            int idd = rs.getInt("dishid");
            String paymentType = rs.getString("paymenttype");
            Date orderdate = rs.getDate("orderdate");

            Order order = new Order(idr, idc, idd, paymentType, orderdate);
            order.setId(ido);
            return order;
        });
    }

    @Override
    public Optional<Order> save(Order entity) throws ValidatorException, IllegalAccessException {
        try {
            this.validator.validate(entity);
            String sql = "insert into orderdb values(?,?,?,?,?,?)";
            int i = jdbcOperations.update(sql, entity.getId(), entity.getRestaurantID(), entity.getClientID(), entity.getDishID(), entity.getPaymentType(), entity.getDate());
            if (i == 1)
                return Optional.empty();
            return Optional.of(entity);
        } catch (Exception e) {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Order> delete(Integer id) {
        Optional<Order> order = this.findOne(id);
        try {
            order.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from orderdb where orderid=?";
        jdbcOperations.update(sql, id);
        return order;
    }

    @Override
    public Optional<Order> update(Order entity) throws ValidatorException {
        try {
            this.validator.validate(entity);
            String sql = "update orderdb set restaurantid=?,clientid=?,dishid=?,paymenttype=?,orderdate=? where orderid=?";
            int i = jdbcOperations.update(sql, entity.getRestaurantID(), entity.getClientID(), entity.getDishID(), entity.getPaymentType(), entity.getDate(),entity.getId());
            if (i == 0)
                return Optional.empty();
            return Optional.of(entity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int size() {
        return 0;
    }
}
