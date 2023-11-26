package it.unicam.cs.ids.loyalty.repository;
import it.unicam.cs.ids.loyalty.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Merchant entities.
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

    // Other basic methods provided by JpaRepository...

    /**
     * Finds merchants by name.
     *
     * @param name The name of the merchant to search for.
     * @return List of merchants with the specified name.
     */
    List<Merchant> findByName(String name);
    
    /**
     * Finds merchants by unique id.
     *
     * @param id The unique id of the merchant to search for.
     * @return merchant with the specified id.
     */
    Optional<Merchant> findById(int id);
}
