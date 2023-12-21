package it.unicam.cs.ids.loyalty.service;
import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.model.MemberCard;
import it.unicam.cs.ids.loyalty.model.Membership;
import it.unicam.cs.ids.loyalty.model.MembershipAccount;
import it.unicam.cs.ids.loyalty.repository.CustomerRepository;
 
import it.unicam.cs.ids.loyalty.repository.LoyaltyProgramRepository;
import it.unicam.cs.ids.loyalty.repository.MembershipRepository;
import it.unicam.cs.ids.loyalty.repository.MemberCardRepository;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
/**
* Default implementation of the CrudService for Customer entities.
*/
@Service
public class DefaultCustomerService implements CrudService<Customer> {
	@Autowired
    private final CustomerRepository customerRepository;
 
    @Autowired
    private final MembershipRepository membershipRepository;
 
	@Autowired
	final LoyaltyProgramRepository loyaltyProgramRepository;
	
	@Autowired
	final MemberCardRepository memberCardRepository;
 
    @Autowired
    public DefaultCustomerService(CustomerRepository customerRepository,
    		LoyaltyProgramRepository loyaltyProgramRepository,
    		MembershipRepository membershipRepository,
    		MemberCardRepository memberCardRepository) {
        this.customerRepository = customerRepository;
        this.loyaltyProgramRepository=loyaltyProgramRepository;
        this.membershipRepository=membershipRepository;
        this.memberCardRepository=memberCardRepository;
    }
 
    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
 
    @Override
    public Optional<Customer> getById(int id) {
        return customerRepository.findById(id);
    }
 
    @Override
    public Optional<Customer> getByName(String cognome) {
        List<Customer> customers = customerRepository.findByCognome(cognome);
        return customers.isEmpty() ? Optional.empty() : Optional.of(customers.get(0));
    }
 
 
    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }
 
    @Override
    public Customer update(Customer customer) {
      
        return customerRepository.save(customer);
    }
 
    @Override
    public void delete(int id) {
        customerRepository.deleteById(id);
    }
    public Membership joinLoyaltyProgram(int customerId, int loyaltyProgramId) {
        // Recupera il cliente
        Customer customer = getById(customerId).get();
 
        // Recupera il programma fedeltà
        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgramId).get();
 
        // Crea una nuova Membership
        Membership newMembership = new Membership(customer, loyaltyProgram);
       
 
        // Crea un nuovo MembershipAccount
        MembershipAccount newMembershipAccount = new MembershipAccount(newMembership);
        newMembershipAccount.setMembership(newMembership);
 
        // Associa Membership e MembershipAccount
        newMembership.setMembershipAccount(newMembershipAccount);
 
        // Salva la nuova Membership
        Membership savedMembership = membershipRepository.save(newMembership);
 
        // Crea una nuova MemberCard
        MemberCard newMemberCard = new MemberCard(newMembership);
       
 
		// Salva la nuova MemberCard
        memberCardRepository.save(newMemberCard);
 
        return savedMembership;
    }
}