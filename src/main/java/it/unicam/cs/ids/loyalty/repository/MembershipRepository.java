package it.unicam.cs.ids.loyalty.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import it.unicam.cs.ids.loyalty.model.Membership;
 
/**
* Repository interface for managing Membership entities.
*/
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
 
}