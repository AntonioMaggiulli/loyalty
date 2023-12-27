package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;
import it.unicam.cs.ids.loyalty.repository.LevelRepository;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.repository.PartnershipRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the CrudService for LoyaltyProgram entities.
 */
@Service
public class DefaultLoyaltyProgramService implements CrudService<LoyaltyProgram> {

	private final LoyaltyProgramRepository loyaltyProgramRepository;
	private final MerchantRepository merchantRepository;
	private final PartnershipRepository partnershipRepository;
	private final LevelRepository levelRepository;
	private final BenefitRepository benefitRepository;

	@Autowired
	public DefaultLoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository,
			MerchantRepository merchantRepository, PartnershipRepository partnershipRepository,
			LevelRepository levelRepository, BenefitRepository benefitRepository) {
		this.loyaltyProgramRepository = loyaltyProgramRepository;
		this.merchantRepository = merchantRepository;
		this.partnershipRepository = partnershipRepository;
		this.levelRepository = levelRepository;
		this.benefitRepository = benefitRepository;
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
	public LoyaltyProgram update(LoyaltyProgram loyaltyProgram) {

		return loyaltyProgramRepository.save(loyaltyProgram);
	}

	@Override
	public void delete(int id) {
		loyaltyProgramRepository.deleteById(id);
	}

	@Override
	public LoyaltyProgram create(LoyaltyProgram loyaltyProgram) {
		return loyaltyProgramRepository.save(loyaltyProgram);
	}

	/**
	 * Creates a new LoyaltyProgram entity and associates it with the specified
	 * merchant.
	 *
	 * @param loyaltyProgram The LoyaltyProgram entity to create.
	 * @param merchantId     The ID of the merchant to associate with the loyalty
	 *                       program.
	 * @return The created LoyaltyProgram entity.
	 */

	// spostare questa responsabilità
	/*
	 * public void addMerchantToProgram(int programId, int merchantId) {
	 * Optional<LoyaltyProgram> loyaltyProgramOptional =
	 * loyaltyProgramRepository.findById(programId); Optional<Merchant>
	 * merchantOptional = merchantRepository.findById(merchantId);
	 * 
	 * if (loyaltyProgramOptional.isPresent() && merchantOptional.isPresent()) {
	 * LoyaltyProgram loyaltyProgram = loyaltyProgramOptional.get(); Merchant
	 * merchant = merchantOptional.get();
	 * 
	 * loyaltyProgram.addMerchant(merchant);
	 * loyaltyProgramRepository.save(loyaltyProgram); } else { throw new
	 * IllegalArgumentException("Programma fedeltà o azienda non trovati."); } }
	 */

	public List<LoyaltyProgram> getCoalitions() {
		return loyaltyProgramRepository.findByIsCoalition(true);
	}

	public List<LoyaltyProgram> getByMerchant(int idMerchant) {
		// Recupera le partnership associate all'azienda specificata
		List<Partnership> partnerships = partnershipRepository.findByMerchantId(idMerchant);

		// Crea una lista per i programmi fedeltà associati
		List<LoyaltyProgram> associatedPrograms = partnerships.stream().map(Partnership::getLoyaltyProgram).toList();

		return associatedPrograms;
	}

	@Transactional
	public Level createLevel(int loyaltyProgramId, String levelName, String levelDescription) {
		// Recupera il programma fedeltà
		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId)
				.orElseThrow(() -> new IllegalArgumentException("Programma fedeltà non trovato."));

		// Crea un nuovo livello
		Level newLevel = new Level(levelName, levelDescription, loyaltyProgram);

		// Salva il nuovo livello nel repository
		Level savedLevel = levelRepository.save(newLevel);

		// Aggiungi il livello alla lista dei livelli del programma fedeltà
		loyaltyProgram.getLevels().add(savedLevel);
		loyaltyProgramRepository.save(loyaltyProgram);

		return savedLevel;
	}

	@Transactional
	public List<Level> getLevelsOfLoyaltyProgram(int programId) {
		// Trova il programma di fedeltà tramite il suo ID
		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(programId).orElseThrow(
				() -> new IllegalArgumentException("Programma di fedeltà non trovato con ID: " + programId));

		// Restituisci i livelli associati al programma di fedeltà
		return loyaltyProgram.getLevels();
	}
	public Map<Integer, List<Benefit>> getBenefitsByLoyaltyProgram(int programId) {
        Map<Integer, List<Benefit>> benefitsByLevel = new HashMap<>();
        
        LoyaltyProgram program = this.getById(programId)
                .orElseThrow(() -> new IllegalArgumentException("Programma fedeltà non trovato con ID: " + programId));
        
        List<Level> levels = program.getLevels();
        for (Level level : levels) {
            List<Benefit> benefits = benefitRepository.findByAssociatedLevel(level);
            benefitsByLevel.put(level.getId(), benefits);
        }
        
        return benefitsByLevel;
    }
}
