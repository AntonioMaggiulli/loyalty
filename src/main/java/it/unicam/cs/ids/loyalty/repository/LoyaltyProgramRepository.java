package it.unicam.cs.ids.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;

import java.util.List;

/**
 * Repository for the {@link LoyaltyProgram} entity. Provides default CRUD
 * (Create, Read, Update, Delete) operations and the ability to define custom
 * queries.
 */
@Repository
public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Integer> {

	/**
	 * Finds a loyalty program by the specified program name.
	 *
	 * @param programName The name of the loyalty program to search for.
	 * @return An instance of {@link LoyaltyProgram} if found, or an empty Optional.
	 */
	List<LoyaltyProgram> findByProgramName(String programName);

}
