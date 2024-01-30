package it.unicam.cs.ids.loyalty.repository;

import it.unicam.cs.ids.loyalty.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	List<Customer> findByCognome(String cognome);

	List<Customer> findByNome(String nome);

	Optional<Customer> findById(int id);

	Optional<Customer> findByReferralCode(String referralCode);

}