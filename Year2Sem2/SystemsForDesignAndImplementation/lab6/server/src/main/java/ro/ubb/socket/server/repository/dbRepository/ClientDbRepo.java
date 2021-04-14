package ro.ubb.socket.server.repository.dbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.socket.common.domain.Client;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientDbRepo implements Repository<Integer, Client> {

    @Autowired
    private JdbcOperations jdbcOperations;
    private final Validator<Client> validator;

    /**
     * Constructor for the ClientDbRepo
     *
     * @param validator the validator
     */
    public ClientDbRepo(Validator<Client> validator) {
        this.validator = validator;
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
        Client client;
        client = jdbcOperations.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rowNumber) -> {
            int idd = rs.getInt("clientId");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            int addressId = rs.getInt("addressId");
            String email = rs.getString("email");
            Client client1 = new Client(name, age, addressId, email);
            client1.setId(idd);
            return client1;
        });
        return Optional.of(client);
    }

    @Override
    public Map<Integer, Client> getEntities() {
        return null;
    }

    /**
     * @return all clients from the database
     */
    @Override
    public List<Client> findAll() {
        String sql = "select * from client";
        return jdbcOperations.query(sql, (rs, rowNum) -> {
            int idd = rs.getInt("clientId");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            int addressId = rs.getInt("addressId");
            String email = rs.getString("email");
            Client client = new Client(name, age, addressId, email);
            client.setId(idd);
            return client;
        });
    }

    /**
     * Saves the given client in table.
     *
     * @param client - must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Client> save(Client client) throws ValidatorException, IllegalAccessException {
        try {
            this.validator.validate(client);
            String sql = "insert into client values(?,?,?,?,?)";
            int i = jdbcOperations.update(sql, client.getId(), client.getName(), client.getAge(), client.getAddress(), client.getEmail());
            if (i == 1)
                return Optional.empty();
            return Optional.of(client);
        } catch (Exception e) {
            return Optional.of(client);
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
        Optional<Client> a = this.findOne(id);
        try {
            a.orElseThrow(Exception::new);
        } catch (Exception e) {
            return Optional.empty();
        }
        String sql = "delete from client where clientid=?";
        jdbcOperations.update(sql, id);
        return a;
    }

    /**
     * Updates the given client.
     *
     * @param client must not be null.
     * @return an {@code Optional} - null if the entity was not updated (e.g. id does not exist), otherwise returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Client> update(Client client) throws ValidatorException {
        try {
            this.validator.validate(client);
            String sql = "update client set name=?,age=?,addressId=?,email=? where clientId=?";
            int i = jdbcOperations.update(sql, client.getName(), client.getAge(), client.getAddress(), client.getEmail(), client.getId());
            if (i == 0)
                return Optional.empty();
            return Optional.of(client);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int size() {
        return 0;
    }
}
