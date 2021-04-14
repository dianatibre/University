package ro.ubb.socket.server.repository;

import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.validators.Validator;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author radu.
 */
public class InMemoryRepository<ID, T extends BaseEntity<ID>> implements Repository<ID, T> {

    protected Map<ID, T> entities;
    protected Validator<T> validator;

    public Map<ID, T> getEntities() {
        return entities;
    }

    public InMemoryRepository() {
    }

    public InMemoryRepository(Validator<T> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<T> findOne(ID id) {
        Optional<ID> opt = Optional.ofNullable(id);
        opt.orElseThrow(IllegalArgumentException::new);
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<T> findAll() {
        Set<T> allEntities = entities.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return allEntities;
    }

    @Override
    public Optional<T> save(T entity) throws ValidatorException, IllegalAccessException {
        Optional<T> opt = Optional.ofNullable(entity);
        opt.orElseThrow(IllegalAccessException::new);
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<T> delete(ID id) {
        Optional<ID> opt = Optional.ofNullable(id);
        opt.orElseThrow(IllegalArgumentException::new);
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<T> update(T entity) throws ValidatorException {
        Optional<T> optional = Optional.ofNullable(entity);
        optional.orElseThrow(IllegalArgumentException::new);
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }

    @Override
    public int size() {
        Iterable<T> set = this.findAll();
        return (int) StreamSupport.stream(set.spliterator(), false).count();
    }

    @Override
    public void emptyTable() {
        int a = 0;
    }
}
