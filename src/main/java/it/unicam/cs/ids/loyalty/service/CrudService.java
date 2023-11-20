package it.unicam.cs.ids.loyalty.service;
import java.util.List;
import java.util.Optional;

/**
 * Generic interface for basic CRUD operations.
 *
 * @param <T> Type of the entity.
 */
public interface CrudService<T> {

    /**
     * Retrieves all entities.
     *
     * @return List of entities.
     */
    List<T> getAll();

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return Optional containing the entity if found, otherwise empty.
     */
    Optional<T> getById(int id);

    /**
     * Retrieves an entity by its name.
     *
     * @param name The name of the entity.
     * @return Optional containing the entity if found, otherwise empty.
     */
    Optional<T> getByName(String name);

    /**
     * Creates a new entity.
     *
     * @param entity The entity to create.
     * @return The created entity.
     */
    T create(T entity);

    /**
     * Updates an existing entity.
     *
     * @param entity The entity to update.
     * @return The updated entity.
     * @throws IllegalArgumentException if the entity is not valid.
     */
    T update(T entity);

    /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     */
    void delete(int id);
}
