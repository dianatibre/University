package ro.ubb.socket.server.repository.dbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.socket.common.domain.Address;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AddressDbRepo implements Repository<Integer, Address> {
    @Autowired
    private JdbcOperations jdbcOperations;
    private final Validator<Address> validator;

    /**
     * Constructor for the AddressDbRepo
     *
     * @param validator the validator
     */
    public AddressDbRepo(Validator<Address> validator) {
        this.validator = validator;
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
        a = jdbcOperations.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rowNumber) -> {
            int idd = rs.getInt("addressId");
            String city = rs.getString("city");
            String street = rs.getString("street");
            int number = rs.getInt("number");
            String zipCode = rs.getString("zipCode");
            Address address = new Address(city, street, number, zipCode);
            address.setId(idd);
            return address;
        });
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
    public List<Address> findAll() {
        String sql = "select * from address";
        return jdbcOperations.query(sql, (rs, rowNum) -> {
            int idd = rs.getInt("addressId");
            String city = rs.getString("city");
            String street = rs.getString("street");
            int number = rs.getInt("number");
            String zipCode = rs.getString("zipCode");
            Address address = new Address(city, street, number, zipCode);
            address.setId(idd);
            return address;
        });
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
        try {
            this.validator.validate(a);
            String sql = "insert into address values(?,?,?,?,?)";
            int i = jdbcOperations.update(sql, a.getId(), a.getCity(), a.getStreet(), a.getNumber(), a.getZipCode());
            if (i == 1)
                return Optional.empty();
            return Optional.of(a);
        } catch (Exception e) {
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
        jdbcOperations.update(sql, id);
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
        try {
            this.validator.validate(a);
            String sql = "update address set city=?,street=?,number=?,zipcode=? where addressId=?";
            int i = jdbcOperations.update(sql, a.getCity(), a.getStreet(), a.getNumber(), a.getZipCode(), a.getId());
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
