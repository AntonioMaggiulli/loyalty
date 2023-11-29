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
     * @param name The name of the customer to search for.
     * @return List of customers with the specified name.
     */
    List<Customer> findByCognome(String cognome);
 
    /**
     * Finds customer by name.
     *
     * @param name The name of the customerto search for.
     * @return List of customers with the specified name.
     */
    List<Customer> findByNome(String nome);
    
    /**
     * Finds customers by unique id.
     *
     * @param id The unique id of the customer to search for.
     * @return customer with the specified id.
     */
    Optional<Customer> findById(int id);
}