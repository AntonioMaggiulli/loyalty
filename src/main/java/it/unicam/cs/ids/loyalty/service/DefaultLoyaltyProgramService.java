package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.MemberCard;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.model.MembershipAccount;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;
import it.unicam.cs.ids.loyalty.repository.CustomerRepository;
import it.unicam.cs.ids.loyalty.repository.LevelRepository;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import it.unicam.cs.ids.loyalty.repository.MemberCardRepository;
import it.unicam.cs.ids.loyalty.repository.MembershipRepository;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.repository.PartnershipRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of the CrudService for LoyaltyProgram entities.
 */
@Service
public class DefaultLoyaltyProgramService implements CrudService<LoyaltyProgram> {

	private final LoyaltyProgramRepository loyaltyProgramRepository;
	private final CustomerRepository customerRepository;
	private final PartnershipRepository partnershipRepository;
	private final MembershipRepository membershipRepository;
	private final LevelRepository levelRepository;
	private final BenefitRepository benefitRepository;
	private final MemberCardRepository memberCardRepository;

	@Autowired
	public DefaultLoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository,
			CustomerRepository customerRepository, PartnershipRepository partnershipRepository,
			LevelRepository levelRepository, BenefitRepository benefitRepository,
			MembershipRepository membershipRepository, MemberCardRepository memberCardRepository) {
		this.loyaltyProgramRepository = loyaltyProgramRepository;
		this.customerRepository = customerRepository;
		this.partnershipRepository = partnershipRepository;
		this.levelRepository = levelRepository;
		this.benefitRepository = benefitRepository;
		this.membershipRepository = membershipRepository;
		this.memberCardRepository = memberCardRepository;
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

	public List<LoyaltyProgram> getAvailableCustomerProgram(int idCustomer) {

		// Recupera tutti i programmi fedeltà disponibili
		List<LoyaltyProgram> allPrograms = loyaltyProgramRepository.findAll();

		// Recupera le membership del cliente per ottenere i programmi a cui è già
		// iscritto
		List<LoyaltyProgram> subscribedPrograms = membershipRepository.findByCustomerId(idCustomer).stream()
				.map(Membership::getLoyaltyProgram).collect(Collectors.toList());

		// Filtra i programmi fedeltà, escludendo quelli a cui il cliente è già iscritto
		return allPrograms.stream().filter(program -> !subscribedPrograms.contains(program))
				.collect(Collectors.toList());
	}

	public void joinLoyaltyProgram(int customerId, int loyaltyProgramId) {

		Customer customer = customerRepository.findById(customerId).get();

		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId).get();

		List<Level> sortedLevels = loyaltyProgram.getLevels().stream()
				.sorted(Comparator.comparingInt(Level::getPointsThreshold)).collect(Collectors.toList());
		Level initialLevel = sortedLevels.get(0);

		Membership membership = loyaltyProgram.enrollCustomer(customer, initialLevel);

		// Creare account e card
		MembershipAccount newMembershipAccount = new MembershipAccount(membership);
		membership.setMembershipAccount(newMembershipAccount);

		membershipRepository.save(membership);

		MemberCard newMemberCard = new MemberCard(membership);

		memberCardRepository.save(newMemberCard);
		newMemberCard.SetCardNumber();
		memberCardRepository.save(newMemberCard);
		membership.setMemberCard(newMemberCard);
		membershipRepository.save(membership);
	}

	@Transactional
	public Level createLevel(int loyaltyProgramId, String levelName, String levelDescription, int threshold) {

		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId)
				.orElseThrow(() -> new IllegalArgumentException("Programma fedeltà non trovato."));

		// Crea un nuovo livello
		Level newLevel = new Level(levelName, levelDescription, loyaltyProgram, threshold);

		Level savedLevel = levelRepository.save(newLevel);

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
		return loyaltyProgram.getLevels().stream().sorted(Comparator.comparingInt(Level::getPointsThreshold))
				.collect(Collectors.toList());
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

	public Map<LoyaltyProgram, List<Membership>> getCustomersBySurnameForMerchantPrograms(String surname,
			int merchantId) {

		List<LoyaltyProgram> merchantLoyaltyPrograms = loyaltyProgramRepository.findByMerchantId(merchantId);
		Map<LoyaltyProgram, List<Membership>> programCustomerMap = new HashMap<>();

		for (LoyaltyProgram program : merchantLoyaltyPrograms) {
			List<Membership> memberships = program.getMemberships().stream()
					.filter(membership -> membership.getCustomer().getCognome().equalsIgnoreCase(surname))
					.collect(Collectors.toList());
			if (!memberships.isEmpty()) programCustomerMap.put(program, memberships);
		}
		if (programCustomerMap.isEmpty()) return null;
		return programCustomerMap;
	}

	public Map<LoyaltyProgram, List<Membership>> getCustomerByTaxCodeForMerchantPrograms(String taxCode,int merchantId) {
		List<LoyaltyProgram> merchantLoyaltyPrograms = loyaltyProgramRepository.findByMerchantId(merchantId);

		Map<LoyaltyProgram, List<Membership>> programCustomerMap = new HashMap<>();

		for (LoyaltyProgram program : merchantLoyaltyPrograms) {
			Optional<Membership> membershipOptional = program.getMemberships().stream()
					.filter(membership -> membership.getCustomer().getCodiceFiscale().equalsIgnoreCase(taxCode))
					.findFirst();

			membershipOptional.ifPresent(membership -> {
				List<Membership> memberships = new ArrayList<>();
				memberships.add(membership);
				programCustomerMap.put(program, memberships);
			});
		}

		return programCustomerMap;
	}

}
