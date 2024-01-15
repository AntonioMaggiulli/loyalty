package it.unicam.cs.ids.loyalty.repository;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Transaction;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Collection<Transaction> findByLoyaltyBenefit(Benefit loyaltyBenefit);
}
