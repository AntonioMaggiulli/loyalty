package it.unicam.cs.ids.loyalty.repository;

import it.unicam.cs.ids.loyalty.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing Level entities.
 */
public interface LevelRepository extends JpaRepository<Level, Integer> {

    /**
     * Retrieves a Level entity by its name.
     *
     * @param name The name of the Level.
     * @return Optional containing the Level if found, otherwise empty.
     */
    Optional<Level> findByName(String name);

}
