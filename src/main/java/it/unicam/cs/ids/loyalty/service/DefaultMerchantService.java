package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.factories.BenefitFactory;
import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Employee;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.repository.PartnershipRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;
import it.unicam.cs.ids.loyalty.repository.EmployeeRepository;
import it.unicam.cs.ids.loyalty.repository.LevelRepository;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the CrudService for Merchant entities.
 */
@Service
public class DefaultMerchantService implements CrudService<Merchant> {

	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private PartnershipRepository partnershipRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private LoyaltyProgramRepository loyaltyProgramRepository;
	@Autowired
	private LevelRepository levelRepository;
	@Autowired
	private BenefitRepository benefitRepository;

	public DefaultMerchantService(MerchantRepository merchantRepository) {

	}

	@Override
	public List<Merchant> getAll() {
		return merchantRepository.findAll();
	}

	@Override
	public Optional<Merchant> getById(int id) {
		return merchantRepository.findById(id);
	}

	@Override
	public Optional<Merchant> getByName(String name) {
		List<Merchant> merchants = merchantRepository.findByName(name);
		return merchants.isEmpty() ? Optional.empty() : Optional.of(merchants.get(0));
	}

	@Override
	public Merchant create(Merchant merchant) {
		return merchantRepository.save(merchant);
	}

	@Override
	public Merchant update(Merchant merchant) {

		return merchantRepository.save(merchant);
	}

	@Override
	public void delete(int id) {
		merchantRepository.deleteById(id);
	}

	public LoyaltyProgram createLoyaltyProgram(String programName, String description, boolean isCoalition,
			int merchantId) {
		LoyaltyProgram loyaltyProgram = new LoyaltyProgram(programName, description, isCoalition);
		Optional<Merchant> optionalMerchant = merchantRepository.findById(merchantId);
		Merchant merchant = optionalMerchant
				.orElseThrow(() -> new EntityNotFoundException("Merchant not found with id: " + merchantId));

		Partnership partnership = createPartnership(merchant, loyaltyProgram);

		loyaltyProgram.addPartnership(partnership);

		LoyaltyProgram createdProgram = loyaltyProgramRepository.save(loyaltyProgram);
		partnershipRepository.save(partnership);
		return createdProgram;
	}

	@Transactional
	Partnership createPartnership(Merchant merchant, LoyaltyProgram loyaltyProgram) {
		Partnership partnership = new Partnership();
		partnership.setMerchant(merchant);
		partnership.setLoyaltyProgram(loyaltyProgram);
		LocalDate localDate = LocalDate.now();
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		partnership.setStartDate(date);
		return partnership;
	}

	public void createBenefit(String type, String name, String description, int pointsRequired, int merchantId,
			int loyaltyProgramId, int levelId, Object... additionalParams) {
		Merchant offeringMerchant = merchantRepository.findById(merchantId)
				.orElseThrow(() -> new IllegalArgumentException("Merchant non trovato."));
		LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId)
				.orElseThrow(() -> new IllegalArgumentException("Programma Fedeltà non trovato."));
		List<Level> associatedLevel = new ArrayList<Level>();
		if (levelId == 0) {

			associatedLevel.addAll(loyaltyProgram.getLevels());
		} else
			associatedLevel.add(levelRepository.findById(levelId).get());

		for (Level level : associatedLevel) {
			Benefit benefit = BenefitFactory.createBenefit(type, name, description, pointsRequired, offeringMerchant,
					loyaltyProgram, level, additionalParams);

			benefitRepository.save(benefit);
		}
	}

	@Transactional
	public void joinCoalition(Merchant merchant, LoyaltyProgram loyaltyProgram) {
		Partnership partnership = createPartnership(merchant, loyaltyProgram);
		partnershipRepository.save(partnership);

	}

	public void createNewEmployee(int merchantId, String name, String matricola, String username, String password) {

		Employee newEmployee = new Employee(name, matricola, username, password);

		Merchant merchant = merchantRepository.findById(merchantId).orElse(null);
		newEmployee.setMerchant(merchant);
		merchant.getEmployees().add(newEmployee);
		employeeRepository.save(newEmployee);
		merchantRepository.save(merchant);

	}

}