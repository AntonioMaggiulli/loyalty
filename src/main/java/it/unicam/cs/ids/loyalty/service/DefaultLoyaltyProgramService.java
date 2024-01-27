package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.factories.BenefitFactory;
import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.Invitation;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.MemberCard;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.model.MembershipAccount;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.model.Transaction;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;
import it.unicam.cs.ids.loyalty.repository.CustomerRepository;
import it.unicam.cs.ids.loyalty.repository.InvitationRepository;
import it.unicam.cs.ids.loyalty.repository.LevelRepository;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import it.unicam.cs.ids.loyalty.repository.MemberCardRepository;
import it.unicam.cs.ids.loyalty.repository.MembershipAccountRepository;
import it.unicam.cs.ids.loyalty.repository.MembershipRepository;
import it.unicam.cs.ids.loyalty.repository.PartnershipRepository;
import it.unicam.cs.ids.loyalty.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import ch.qos.logback.core.filter.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

	private final DefaultMerchantService merchantService;
	private final LoyaltyProgramRepository loyaltyProgramRepository;
	private final CustomerRepository customerRepository;
	private final PartnershipRepository partnershipRepository;
	private final MembershipRepository membershipRepository;
	private final LevelRepository levelRepository;
	private final BenefitRepository benefitRepository;
	private final MemberCardRepository memberCardRepository;
	private final InvitationRepository invitationRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private MembershipAccountRepository membershipAccountRepository;

	@Autowired
	public DefaultLoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository,
			CustomerRepository customerRepository, PartnershipRepository partnershipRepository,
			LevelRepository levelRepository, BenefitRepository benefitRepository,
			MembershipRepository membershipRepository, MemberCardRepository memberCardRepository,
			DefaultMerchantService merchantService, InvitationRepository invitationRepository) {
		this.merchantService = merchantService;
		this.loyaltyProgramRepository = loyaltyProgramRepository;
		this.customerRepository = customerRepository;
		this.partnershipRepository = partnershipRepository;
		this.levelRepository = levelRepository;
		this.benefitRepository = benefitRepository;
		this.membershipRepository = membershipRepository;
		this.memberCardRepository = memberCardRepository;
		this.invitationRepository = invitationRepository;
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

		List<Partnership> partnerships = partnershipRepository.findByMerchantId(idMerchant);
		List<LoyaltyProgram> associatedPrograms = partnerships.stream().map(Partnership::getLoyaltyProgram).toList();
		return associatedPrograms;
	}

	public List<LoyaltyProgram> getAvailableCustomerProgram(int idCustomer) {

		List<LoyaltyProgram> allPrograms = loyaltyProgramRepository.findAll();
		List<LoyaltyProgram> subscribedPrograms = membershipRepository.findByCustomerId(idCustomer).stream()
				.map(Membership::getLoyaltyProgram).collect(Collectors.toList());

		return allPrograms.stream().filter(program -> !subscribedPrograms.contains(program))
				.collect(Collectors.toList());
	}

	public Membership joinLoyaltyProgram(int customerId, int loyaltyProgramId, String referralCode) {
		try {
			Customer customer = customerRepository.findById(customerId)
					.orElseThrow(() -> new EntityNotFoundException("Customer non trovato con ID: " + customerId));

			LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId).orElseThrow(
					() -> new EntityNotFoundException("Programma fedeltà non trovato con ID: " + loyaltyProgramId));

			Invitation receivedInvitation = null;

			if (referralCode != null && !referralCode.isEmpty()) {
				Customer sender = customerRepository.findByReferralCode(referralCode).orElseThrow(
						() -> new EntityNotFoundException("Mittente non trovato con referral code: " + referralCode));

				List<Invitation> invitations = invitationRepository.findBySender(sender);
				receivedInvitation = invitations.stream()
	                    .filter(invitation -> invitation.getFriendEmail().equals(customer.getEmail()))
	                    .findFirst()
	                    .orElse(null);

			}

			List<Level> sortedLevels = loyaltyProgram.getLevels().stream()
					.sorted(Comparator.comparingInt(Level::getPointsThreshold)).collect(Collectors.toList());
			Level initialLevel = sortedLevels.get(0);
			Membership membership = loyaltyProgram.enrollCustomer(customer, initialLevel);
			MembershipAccount newMembershipAccount = new MembershipAccount(membership);
			membership.setMembershipAccount(newMembershipAccount);
		membership.setReceivedInvitation(receivedInvitation);

			membershipRepository.save(membership);

			MemberCard newMemberCard = new MemberCard(membership);

			memberCardRepository.save(newMemberCard);
			newMemberCard.SetCardNumber();
			memberCardRepository.save(newMemberCard);
			membership.setMemberCard(newMemberCard);
			membershipRepository.save(membership);
			return membership;
		} catch (EntityNotFoundException e) {

			System.out.println("Errore durante l'adesione al programma fedeltà: " + e.getMessage());
		return null;
		}
	}

	@Transactional
	public Level createLevel(int loyaltyProgramId, String levelName, String levelDescription, int threshold) {
		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId)
				.orElseThrow(() -> new IllegalArgumentException("Programma fedeltà non trovato."));

		try {
			if (!loyaltyProgram.getLevels().isEmpty()) {
				validateThreshold(loyaltyProgram.getLevels(), threshold);
			}

			Level newLevel = new Level(levelName, levelDescription, loyaltyProgram, threshold);
			Level savedLevel = levelRepository.save(newLevel);

			loyaltyProgram.getLevels().add(savedLevel);
			loyaltyProgramRepository.save(loyaltyProgram);

			return savedLevel;
		} catch (IllegalArgumentException e) {

			System.out.println("Errore durante la validazione della soglia: " + e.getMessage());
			return null;
		}
	}

	@Transactional
	public List<Level> getLevelsOfLoyaltyProgram(int programId) {

		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(programId).orElseThrow(
				() -> new IllegalArgumentException("Programma di fedeltà non trovato con ID: " + programId));

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

	public Map<LoyaltyProgram, List<Membership>> findMembershipsBySurnameInMerchantLoyaltyPrograms(String surname,
			int merchantId) {

		List<LoyaltyProgram> merchantLoyaltyPrograms = loyaltyProgramRepository.findByMerchantId(merchantId);
		Map<LoyaltyProgram, List<Membership>> programCustomerMap = new HashMap<>();

		for (LoyaltyProgram program : merchantLoyaltyPrograms) {
			List<Membership> memberships = program.getMemberships().stream()
					.filter(membership -> membership.getCustomer().getCognome().equalsIgnoreCase(surname))
					.collect(Collectors.toList());
			if (!memberships.isEmpty())
				programCustomerMap.put(program, memberships);
		}
		if (programCustomerMap.isEmpty())
			return null;
		return programCustomerMap;
	}

	public Map<LoyaltyProgram, List<Membership>> getCustomerByTaxCodeForMerchantPrograms(String taxCode,
			int merchantId) {
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

	public Transaction createTransaction(String type, int merchantId, double amount, String cardString) {
		MemberCard card = memberCardRepository.findByCardNumber(cardString)
				.orElseThrow(() -> new EntityNotFoundException("MemberCard non trovata."));
		Membership membership = membershipRepository.findByMemberCard(card)
				.orElseThrow(() -> new EntityNotFoundException("Membership non trovata."));
		int loyaltyProgramId = membership.getLoyaltyProgram().getId();
		int levelId = membership.getCurrentLevel().getId();

		Benefit benefit = benefitRepository.findByTypeAndLoyaltyProgramIdAndOfferingMerchantIdAndAssociatedLevelId(type,
				loyaltyProgramId, merchantId, levelId).get(0);
		MembershipAccount account = membership.getAccount();

		Transaction transaction = new Transaction(benefit, amount, account);

		benefit.applyBenefit(transaction);

		transactionRepository.save(transaction);
		benefitRepository.save(benefit);

		account.updatePoints(transaction);
		account.checkUpgradeForLevel(benefit.getLoyaltyProgram());
		membershipAccountRepository.save(account);
		return transaction;
	}

	public void createTransaction(String type, Benefit benefit, double amount, String cardString) {
		MemberCard card = memberCardRepository.findByCardNumber(cardString)
				.orElseThrow(() -> new EntityNotFoundException("MemberCard non trovata."));
		Membership membership = membershipRepository.findByMemberCard(card)
				.orElseThrow(() -> new EntityNotFoundException("Membership non trovata."));

		MembershipAccount account = membership.getAccount();

		Transaction transaction = new Transaction(benefit, amount, account);

		benefit.applyBenefit(transaction);

		transactionRepository.save(transaction);
		benefitRepository.save(benefit);

		account.updatePoints(transaction);
		account.checkUpgradeForLevel(benefit.getLoyaltyProgram());
		membershipAccountRepository.save(account);
	}

	public void createBenefit(String type, String name, String description, int pointsRequired, int merchantId,
			int loyaltyProgramId, int levelId, Object... additionalParams) {
		Merchant offeringMerchant = merchantService.getById(merchantId)
				.orElseThrow(() -> new IllegalArgumentException("Merchant non trovato."));
		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId)
				.orElseThrow(() -> new IllegalArgumentException("Programma Fedeltà non trovato."));

		List<Level> associatedLevels = levelId == 0 ? new ArrayList<>(loyaltyProgram.getLevels())
				: Collections.singletonList(levelRepository.findById(levelId)
						.orElseThrow(() -> new IllegalArgumentException("Livello non trovato.")));

		for (Level level : associatedLevels) {

			if (type.equals("POINTS_REWARD")
					&& !benefitRepository.findByTypeAndLoyaltyProgramIdAndOfferingMerchantIdAndAssociatedLevelId(type,
							loyaltyProgramId, merchantId, level.getId()).isEmpty()) {
				throw new IllegalArgumentException(
						"Un Benefit di tipo PointsReward esiste già per questo livello fedeltà.");
			}

			Benefit benefit = BenefitFactory.createBenefit(type, name, description, pointsRequired, offeringMerchant,
					loyaltyProgram, level, additionalParams);

			benefitRepository.save(benefit);
		}
	}

	public Level updateLevel(int levelId, String newLevelName, String newLevelDescription, Integer newThreshold) {
		Level existingLevel = levelRepository.findById(levelId)
				.orElseThrow(() -> new IllegalArgumentException("Livello di fedeltà non trovato."));

		validateThreshold(existingLevel.getLoyaltyProgram().getLevels(), newThreshold);

		if (newLevelName != null) {
			existingLevel.setName(newLevelName);
		}

		if (newLevelDescription != null) {
			existingLevel.setDescription(newLevelDescription);
		}

		if (newThreshold != null) {
			existingLevel.setPointsThreshold(newThreshold);
		}
		return levelRepository.save(existingLevel);
	}

	private void validateThreshold(List<Level> existingLevels, Integer newThreshold) {
		if (newThreshold != null) {
			for (Level level : existingLevels) {
				if (level.getPointsThreshold() == newThreshold) {
					throw new IllegalArgumentException("Soglia duplicata. Impossibile aggiornare il livello.");
				}
			}
		}
	}

	public boolean replaceCard(String oldCard, String newCard) {

		if (oldCard == null || newCard == null) {
			throw new IllegalArgumentException("I parametri non possono essere nulli.");
		}

		MemberCard oldMemberCard = memberCardRepository.findByCardNumber(oldCard)
				.orElseThrow(() -> new EntityNotFoundException("MemberCard non trovata con il numero specificato."));

		MemberCard existingNewMemberCard = memberCardRepository.findByCardNumber(newCard).orElse(null);
		if (existingNewMemberCard != null) {
			throw new IllegalStateException("La nuova carta è già stata assegnata a un'altra MemberCard.");
		}
		oldMemberCard.setCardNumber(newCard);
		memberCardRepository.save(oldMemberCard);

		return true;
	}

	public List<Transaction> getTransactions(int customerId, int programId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new IllegalArgumentException("Customer non trovato."));
		LoyaltyProgram lProgram = loyaltyProgramRepository.findById(programId)
				.orElseThrow(() -> new IllegalArgumentException("Programma non trovato."));
		Membership membership = membershipRepository.findByCustomerAndLoyaltyProgram(customer, lProgram)
				.orElseThrow(() -> new IllegalArgumentException("Adesione non trovata."));

		List<Transaction> transactions = membership.getAccount().getTransactions();
		return transactions;
	}

	public Map<String, List<Transaction>> getTransactionsByBenefitType(LoyaltyProgram lProgram) {
		return lProgram.getMemberships().stream()
				.flatMap(membership -> membership.getAccount().getTransactions().stream())
				.collect(Collectors.groupingBy(transaction -> transaction.getLoyaltyBenefit().getType()));
	}

	public String inviteFriend(int customerId, int loyaltyProgramId, String friendContact) {

		Customer sender = customerRepository.findById(customerId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente non trovato."));

		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId)
				.orElseThrow(() -> new EntityNotFoundException("Programma fedeltà non trovato."));

		Membership membership = membershipRepository.findByCustomerAndLoyaltyProgram(sender, loyaltyProgram)
				.orElseThrow(
						() -> new EntityNotFoundException("Non sei iscritto a questo programma fedeltà."));

		Date invitationDate = new Date();
		String invitationText = generateDefaultInvitationText(sender, loyaltyProgram, membership, invitationDate);
		
		Invitation invitation = new Invitation(sender, friendContact, membership, invitationDate, invitationText);
		invitationRepository.save(invitation);
		return invitationText;
	}

	private String generateDefaultInvitationText(Customer sender, LoyaltyProgram loyaltyProgram, Membership membership,
			Date invitationDate) {
		return "Ciao! Ti invito a unirti a " + loyaltyProgram.getProgramName()
				+ " con il mio codice di riferimento: " + sender.getReferralCodeString()
				+ ". Registrati e inizia a usufruire dei vantaggi!";
	}

}


