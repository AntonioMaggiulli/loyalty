package it.unicam.cs.ids.loyalty.repository;

import it.unicam.cs.ids.loyalty.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Integer> {

    Optional<Level> findByName(String name);

}
