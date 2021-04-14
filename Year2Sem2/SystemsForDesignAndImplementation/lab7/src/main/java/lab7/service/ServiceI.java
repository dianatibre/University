package lab7.service;

import lab7.domain.BaseEntity;
import lab7.domain.validators.ValidatorException;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ServiceI<ID extends Serializable, T extends BaseEntity<ID>> {
    /**
     * Add a given element to the repository
     *
     * @param t the current object to be added
     *
     * @return an {@code Optional}, null if t was added successfully or an object of type T for the id if it exists
     *
     * @throws ValidatorException
     *                      if the given entity doesn't respect the criteria for validation.
     * @throws IllegalArgumentException
     *                      if the given entity is null.
     */
    Optional<T> add(T t) throws ValidatorException;

    /**
     *Gets all entities
     *
     * @return a list of all elements from the repo
     */
    List<T> get();

    /**
     * Deletes the entity with given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - returns the entity if it was deleted, otherwise (e.g. id does not exist) null;
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<T> delete(ID id);

    /**
     * Updates the given entity.
     *
     * @param t must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<T> update(T t) throws ValidatorException;

    /**
     *Gets all entities with a specific property
     *
     * @param pred - the predicate used to filter
     * @return a list of all elements from the repo with the given property
     */
    List<T> filterFunction(Predicate<T> pred);

    /**
     *Gets all entities, sorted
     *
     * @param pred - the comparator function used for sort
     * @return a list of all elements from the repo, sorted by criteria
     */
    List<T> sortFunction(Comparator<T> pred);

    void deleteAllById(Integer id);

    void deleteOrderWhenDeletingRestaurant(Integer id);

    void deleteOrderWhenDeletingDish(Integer id);

    void deleteOrderWhenDeletingClient(Integer id);
}
