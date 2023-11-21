package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the CrudService for LoyaltyProgram entities.
 */
@Service
public class DefaultLoyaltyProgramService implements CrudService<LoyaltyProgram> {

    private final LoyaltyProgramRepository loyaltyProgramRepository;

    @Autowired
    public DefaultLoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
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
     * Bean definition for the main LoyaltyProgramService.
     *
     * @param loyaltyProgramRepository The LoyaltyProgramRepository to be injected.
     * @return The DefaultLoyaltyProgramService bean.
     */
    @Bean
    public DefaultLoyaltyProgramService mainLoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository) {
        return new DefaultLoyaltyProgramService(loyaltyProgramRepository);
    }

    /**
     * Creates a new LoyaltyProgram entity.
     *
     * @param loyaltyProgram The LoyaltyProgram entity to create.
     * @return The created LoyaltyProgram entity.
     */
    public LoyaltyProgram createLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        // Add logic here to validate or further process the loyalty program before saving

        // Perform additional operations if needed

        // Update the loyalty program
        LoyaltyProgram createdProgram = loyaltyProgramRepository.save(loyaltyProgram);

        // Perform any additional operations or validations after saving, if needed

        return createdProgram;
    }
}

