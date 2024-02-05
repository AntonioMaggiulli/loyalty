package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.model.Employee;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;

import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;

import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.repository.PartnershipRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import it.unicam.cs.ids.loyalty.repository.EmployeeRepository;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
			int merchantId, LocalDate expirationDate) {
		LoyaltyProgram loyaltyProgram = new LoyaltyProgram(programName, description, isCoalition, expirationDate);
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


	@Transactional
	public void joinCoalition(Merchant merchant, LoyaltyProgram loyaltyProgram) {
		Partnership partnership = createPartnership(merchant, loyaltyProgram);
		merchant.addPartnership(partnership);
		partnershipRepository.save(partnership);
		merchantRepository.save(merchant);

	}

	public void createNewEmployee(int merchantId, String name, String matricola, String username, String password) {

		Merchant merchant = merchantRepository.findById(merchantId)
				.orElseThrow(() -> new EntityNotFoundException("Merchant non trovato con l'ID specificato."));

		Employee newEmployee = new Employee(name, matricola, username, password);
		newEmployee.setMerchant(merchant);

		merchant.getEmployees().add(newEmployee);

		employeeRepository.save(newEmployee);
		merchantRepository.save(merchant);
	}

}
