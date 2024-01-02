package it.unicam.cs.ids.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unicam.cs.ids.loyalty.model.MemberCard;

/**
 * Repository interface for managing Membercard entities.
 */
@Repository
public interface MemberCardRepository extends JpaRepository<MemberCard, Integer> {
	Optional<MemberCard> findByCardNumber(String cardNumber);

}