package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.OptionalObj;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.common.service.Service;
import ro.ubb.socket.server.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class ServerServiceGeneric<ID, T extends BaseEntity<ID>> implements Service<ID, T> {
    protected ExecutorService executorService;
    protected Repository<ID, T> repository;

    public ServerServiceGeneric() {
    }

    /**
     * Add a given element to the repository
     *
     * @param t the current object to be added
     * @return an {@code Optional}, null if t was added successfully or an object of type T for the id if it exists
     * @throws ValidatorException       if the given entity doesn't respect the criteria for validation.
     * @throws IllegalArgumentException if the given entity is null.
     */
    public OptionalObj<T> add(T t) throws ValidatorException {
        try {
            return new OptionalObj<>(executorService.submit(() -> {
                return repository.save(t);
            }).get());
        } catch (Exception e) {
            return new OptionalObj<>(Optional.empty());
        }
    }

    /**
     * Gets all entities
     *
     * @return a list of all elements from the repo
     */
    public List<T> get() {
        try {
            return executorService.submit(() -> {
                return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
            }).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Deletes the entity with given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    public OptionalObj<T> delete(ID id) {
        try {
            return new OptionalObj<>(executorService.submit(() -> {
                return repository.delete(id);
            }).get());
        } catch (Exception e) {
            return new OptionalObj<>(Optional.empty());
        }
    }

    /**
     * Updates the given entity.
     *
     * @param t must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    public OptionalObj<T> update(T t) throws ValidatorException {
        try {
            return new OptionalObj<>(executorService.submit(() -> {
                return repository.update(t);
            }).get());
        } catch (Exception e) {
            return new OptionalObj<>(Optional.empty());
        }
    }

    /**
     * Gets the entities sorted
     *
     * @return a list of all elements sorted
     */
    public List<T> sort() {
        try {
            return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).sorted().collect(Collectors.toList())).get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
