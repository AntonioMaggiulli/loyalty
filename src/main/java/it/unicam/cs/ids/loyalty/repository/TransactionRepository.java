package it.unicam.cs.ids.loyalty.repository;

import it.unicam.cs.ids.loyalty.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    
}
