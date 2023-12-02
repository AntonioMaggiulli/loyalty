package it.unicam.cs.ids.loyalty.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unicam.cs.ids.loyalty.model.Partnership;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, Integer> {

	List<Partnership> findByMerchantID(int idMerchant);
}
