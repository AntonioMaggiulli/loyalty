package it.unicam.cs.ids.loyalty.service;
 
import it.unicam.cs.ids.loyalty.factories.BenefitFactory;
import it.unicam.cs.ids.loyalty.model.Benefit;
import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.model.Partnership;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import it.unicam.cs.ids.loyalty.repository.PartnershipRepository;
import it.unicam.cs.ids.loyalty.repository.BenefitRepository;
import it.unicam.cs.ids.loyalty.repository.LevelRepository;
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
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
 
	public void joinLoyaltyProgram(int merchantId, int loyaltyProgramId) {
		Optional<Merchant> merchantOptional = merchantRepository.findById(merchantId);
		Optional<LoyaltyProgram> loyaltyProgramOptional = loyaltyProgramRepository.findById(loyaltyProgramId);
 
		if (merchantOptional.isPresent() && loyaltyProgramOptional.isPresent()) {
			Merchant merchant = merchantOptional.get();
			LoyaltyProgram loyaltyProgram = loyaltyProgramOptional.get();
			Date currentDate = new Date();
Partnership partnership=new Partnership(loyaltyProgram, merchant, currentDate);
		
			partnershipRepository.save(partnership);
		} else {
			throw new IllegalArgumentException("Azienda o programma fedeltà non trovati.");
		}
	
 
    }
	public Benefit createBenefit(
	        String type,
	        String name,
	        String description,
	        int pointsRequired,
	        int merchantId,
	        int loyaltyProgramId,
	        int levelId,
	        Object... additionalParams
	    ) {
	        Merchant offeringMerchant = merchantRepository.findById(merchantId)
	            .orElseThrow(() -> new IllegalArgumentException("Merchant non trovato."));
	        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId)
	            .orElseThrow(() -> new IllegalArgumentException("Programma Fedeltà non trovato."));
	        Level associatedLevel = levelRepository.findById(levelId)
	            .orElseThrow(() -> new IllegalArgumentException("Livello non trovato."));

	        Benefit benefit = BenefitFactory.createBenefit(
	            type,
	            name,
	            description,
	            pointsRequired,
	            offeringMerchant,
	            loyaltyProgram,
	            associatedLevel,
	            additionalParams
	        );
	        return benefitRepository.save(benefit);

	}
 
	private LoyaltyProgram getLoyaltyProgramById(int loyaltyProgramId) {
	    return loyaltyProgramRepository.findById(loyaltyProgramId)
	            .orElseThrow(() -> new IllegalArgumentException("Programma Fedeltà non trovato."));
	}
 
}