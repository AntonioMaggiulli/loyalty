package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.LevelRepository;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.repository.PartnershipRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

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
	private final PartnershipRepository partnershipRepository;
	private final LevelRepository levelRepository;

	@Autowired
	public DefaultLoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository,
			MerchantRepository merchantRepository, PartnershipRepository partnershipRepository,
			LevelRepository levelRepository) {
		this.loyaltyProgramRepository = loyaltyProgramRepository;
		this.merchantRepository = merchantRepository;
		this.partnershipRepository = partnershipRepository;
		this.levelRepository = levelRepository;
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
	public LoyaltyProgram createLoyaltyProgram(String programName, String description, boolean isCoalition,
			int merchantId) {
		LoyaltyProgram loyaltyProgram = new LoyaltyProgram(programName, description, isCoalition);
		Optional<Merchant> optionalMerchant = merchantRepository.findById(merchantId);
		Merchant merchant = optionalMerchant
				.orElseThrow(() -> new EntityNotFoundException("Merchant not found with id: " + merchantId));

		Partnership partnership = new Partnership();
		partnership.setMerchant(merchant);
		partnership.setLoyaltyProgram(loyaltyProgram);
		loyaltyProgram.addPartnership(partnership);
		LoyaltyProgram createdProgram = loyaltyProgramRepository.save(loyaltyProgram);

		return createdProgram;
	}

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
		List<Partnership> partnerships = partnershipRepository.findByMerchantID(idMerchant);

		// Crea una lista per i programmi fedeltà associati
		List<LoyaltyProgram> associatedPrograms = partnerships.stream().map(Partnership::getLoyaltyProgram).toList();

		return associatedPrograms;
	}

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
}
