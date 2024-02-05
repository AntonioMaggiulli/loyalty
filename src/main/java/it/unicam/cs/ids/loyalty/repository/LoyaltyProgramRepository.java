package it.unicam.cs.ids.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;

import java.util.List;

@Repository
public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Integer> {

	List<LoyaltyProgram> findByProgramName(String programName);

	List<LoyaltyProgram> findByIsCoalition(boolean isCoalition);

	@Query("SELECT lp FROM LoyaltyProgram lp JOIN lp.partnerships p WHERE p.merchant.id = :merchantId")
	List<LoyaltyProgram> findByMerchantId(@Param("merchantId") int merchantId);

}
