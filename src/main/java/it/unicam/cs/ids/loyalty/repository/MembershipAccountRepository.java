package it.unicam.cs.ids.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.cs.ids.loyalty.model.MembershipAccount;

public interface MembershipAccountRepository extends JpaRepository<MembershipAccount, Integer> {

}
