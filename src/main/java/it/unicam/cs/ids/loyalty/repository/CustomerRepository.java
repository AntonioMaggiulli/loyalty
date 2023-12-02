package it.unicam.cs.ids.loyalty.repository;
import it.unicam.cs.ids.loyalty.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Customer entities.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Finds customer by surname.
     *
     * @param name The name of the merchant to search for.
     * @return List of merchants with the specified name.
     */
    List<Customer> findByCognome(String cognome);

    /**
     * Finds customer by name.
     *
     * @param name The name of the merchant to search for.
     * @return List of merchants with the specified name.
     */
    List<Customer> findByNome(String nome);
    
    /**
     * Finds merchants by unique id.
     *
     * @param id The unique id of the merchant to search for.
     * @return merchant with the specified id.
     */
    Optional<Customer> findById(int id);
}