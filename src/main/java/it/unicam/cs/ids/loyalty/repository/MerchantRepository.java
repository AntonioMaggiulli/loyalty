package it.unicam.cs.ids.loyalty.repository;
import it.unicam.cs.ids.loyalty.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

    List<Merchant> findByName(String name);
    
    Optional<Merchant> findById(int id);
}
