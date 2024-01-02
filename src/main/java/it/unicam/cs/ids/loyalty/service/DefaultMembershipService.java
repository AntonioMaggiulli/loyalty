package it.unicam.cs.ids.loyalty.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unicam.cs.ids.loyalty.model.MemberCard;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.repository.MemberCardRepository;

@Service
public class DefaultMembershipService {
	@Autowired
    private MemberCardRepository memberCardRepository;

    public Optional<Membership> getMembershipByCardNumber(String cardNumber) {
        return memberCardRepository.findByCardNumber(cardNumber)
                                   .map(MemberCard::getMembership);
    }
}

