package it.unicam.cs.ids.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.Invitation;
import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

	List<Invitation> findBySender(Customer sender);

}
