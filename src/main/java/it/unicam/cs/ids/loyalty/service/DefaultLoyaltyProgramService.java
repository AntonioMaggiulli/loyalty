package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the CrudService for LoyaltyProgram entities.
 */
@Service
public class DefaultLoyaltyProgramService implements CrudService<LoyaltyProgram> {

    private final LoyaltyProgramRepository loyaltyProgramRepository;
    private final MerchantRepository merchantRepository;

    @Autowired
    public DefaultLoyaltyProgramService(
            LoyaltyProgramRepository loyaltyProgramRepository,
            MerchantRepository merchantRepository) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
        this.merchantRepository = merchantRepository;
    }
    @Override
    public List<LoyaltyProgram> getAll() {
        return loyaltyProgramRepository.findAll();
    }

    @Override
    public Optional<LoyaltyProgram> getById(int id) {
        return loyaltyProgramRepository.findById(id);
    }

    @Override
    public Optional<LoyaltyProgram> getByName(String programName) {
        List<LoyaltyProgram> loyaltyPrograms = loyaltyProgramRepository.findByProgramName(programName);
        return loyaltyPrograms.isEmpty() ? Optional.empty() : Optional.of(loyaltyPrograms.get(0));
    }


    @Override
    public LoyaltyProgram create(LoyaltyProgram loyaltyProgram) {
        return loyaltyProgramRepository.save(loyaltyProgram);
    }

    @Override
    public LoyaltyProgram update(LoyaltyProgram loyaltyProgram) {
        // Add logic here to validate or further process the loyalty program before updating
        return loyaltyProgramRepository.save(loyaltyProgram);
    }

    @Override
    public void delete(int id) {
        loyaltyProgramRepository.deleteById(id);
    }

    /**
     * Creates a new LoyaltyProgram entity and associates it with the specified merchant.
     *
     * @param loyaltyProgram The LoyaltyProgram entity to create.
     * @param merchantId     The ID of the merchant to associate with the loyalty program.
     * @return The created LoyaltyProgram entity.
     */
    public LoyaltyProgram createLoyaltyProgram(String programName, String description, boolean isCoalition, int merchantId) {
    	LoyaltyProgram loyaltyProgram = new LoyaltyProgram(programName, description, isCoalition);
        Optional<Merchant> optionalMerchant = merchantRepository.findById(merchantId);
        Merchant merchant = optionalMerchant.orElseThrow(() ->
                new EntityNotFoundException("Merchant not found with id: " + merchantId));

        Partnership partnership = new Partnership();
        partnership.setMerchant(merchant);
        partnership.setLoyaltyProgram(loyaltyProgram);
        loyaltyProgram.addPartnership(partnership);
        LoyaltyProgram createdProgram = loyaltyProgramRepository.save(loyaltyProgram);


        return createdProgram;
    }
}


