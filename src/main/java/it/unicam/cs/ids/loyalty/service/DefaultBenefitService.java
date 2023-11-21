package it.unicam.cs.ids.loyalty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Benefit entities.
 */
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

    /**
     * Retrieves Benefit entities by the offering merchant.
     *
     * @param offeringMerchant The offering merchant to search for.
     * @return List of Benefit entities offered by the specified merchant.
     */
    public List<Benefit> getBenefitsByOfferingMerchant(Merchant offeringMerchant) {
        return benefitRepository.findByOfferingMerchant(offeringMerchant);
    }

    /**
     * Retrieves Benefit entities by the associated loyalty program.
     *
     * @param loyaltyProgram The associated loyalty program to search for.
     * @return List of Benefit entities associated with the specified loyalty program.
     */
    public List<Benefit> getBenefitsByLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        return benefitRepository.findByLoyaltyProgram(loyaltyProgram);
    }

	@Override
	public Optional<Benefit> getByName(String name) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}


}
