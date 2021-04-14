package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.validators.ValidatorException;
import ro.ubb.socket.server.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServerServiceGeneric<ID, T extends BaseEntity<ID>> {
    protected ExecutorService executorService;
    protected Repository<ID, T> repository;

    public ServerServiceGeneric() {
    }

    public ServerServiceGeneric(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Add a given element to the repository
     *
     * @param t the current object to be added
     * @return an {@code Optional}, null if t was added successfully or an object of type T for the id if it exists
     * @throws ValidatorException       if the given entity doesn't respect the criteria for validation.
     * @throws IllegalArgumentException if the given entity is null.
     */
    public Future<Optional<T>> add(T t) throws ValidatorException {
        System.out.println(t);
        return executorService.submit(() -> repository.save(t));
    }

    /**
     * Gets all entities
     *
     * @return a list of all elements from the repo
     */
    public Future<List<T>> get() {
        return executorService.submit(() -> StreamSupport.stream(this.repository.findAll().spliterator(), false).collect(Collectors.toList()));
    }

    /**
     * Deletes the entity with given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    public Future<Optional<T>> delete(ID id) {
        return executorService.submit(() -> repository.delete(id));
    }

    /**
     * Updates the given entity.
     *
     * @param t must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    public Future<Optional<T>> update(T t) throws ValidatorException {
        return executorService.submit(() -> repository.update(t));
    }

}
