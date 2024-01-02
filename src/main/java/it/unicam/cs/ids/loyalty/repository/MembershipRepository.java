package it.unicam.cs.ids.loyalty.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.MemberCard;
import it.unicam.cs.ids.loyalty.model.Membership;

/**
 * Repository interface for managing Membership entities.
 */
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {

	List<Membership> findByCustomerId(int idCustomer);

	Optional<Membership> findByMemberCard(MemberCard memberCard);

}