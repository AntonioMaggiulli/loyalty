package it.unicam.cs.ids.loyalty.service;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
 
import it.unicam.cs.ids.loyalty.model.Benefit;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;

import it.unicam.cs.ids.loyalty.model.Merchant;

import it.unicam.cs.ids.loyalty.repository.BenefitRepository;
 
import java.util.List;

import java.util.Optional;


@Service("mainBenefitService")

public class DefaultBenefitService implements CrudService<Benefit> {
 
    private final BenefitRepository benefitRepository;
 
    @Autowired

    public DefaultBenefitService(BenefitRepository benefitRepository) {

        this.benefitRepository = benefitRepository;

    }
 
    @Override

    public List<Benefit> getAll() {

        return benefitRepository.findAll();

    }
 
    @Override

    public Optional<Benefit> getById(int id) {

        return benefitRepository.findById(id);

    }
 
    @Override

    public Benefit create(Benefit entity) {

        return benefitRepository.save(entity);

    }
 
    @Override

    public Benefit update(Benefit entity) {

        return benefitRepository.save(entity);

    }
 
    @Override

    public void delete(int id) {

        benefitRepository.deleteById(id);

    }

    public List<Benefit> getBenefitsByOfferingMerchant(Merchant offeringMerchant) {

        return benefitRepository.findByOfferingMerchant(offeringMerchant);

    }

    public List<Benefit> getBenefitsByLoyaltyProgram(LoyaltyProgram loyaltyProgram) {

        return benefitRepository.findByLoyaltyProgram(loyaltyProgram);

    }
 
	@Override

	public Optional<Benefit> getByName(String name) {

		return Optional.empty();

	}
 
 
}
