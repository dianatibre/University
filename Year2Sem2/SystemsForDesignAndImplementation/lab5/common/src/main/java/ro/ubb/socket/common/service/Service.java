package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.validators.ValidatorException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface Service<ID,T extends BaseEntity<ID>> {
    String SERVER_HOST="localhost";
    int SERVER_PORT=1234;

    /**
     * Add a given element to the repository
     *
     * @param t the current object to be added
     * @return an {@code Optional}, null if t was added successfully or an object of type T for the id if it exists
     * @throws ValidatorException       if the given entity doesn't respect the criteria for validation.
     * @throws IllegalArgumentException if the given entity is null.
     */
    Future<Optional<T>> add(T t) throws ValidatorException;

    /**
     * Gets all entities
     *
     * @return a list of all elements from the repo
     */
    Future<List<T>> get();

    /**
     * Deletes the entity with given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    Future<Optional<T>> delete(ID id);

    /**
     * Updates the given entity.
     *
     * @param t must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    Future<Optional<T>> update(T t) throws ValidatorException;

    /**
     * Gets all entities, sorted
     *
     * @return a list of all elements from the repo, sorted by criteria
     */
    Future<List<T>> sort();

    void emptyList();
}
