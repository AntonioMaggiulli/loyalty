package it.unicam.cs.ids.loyalty.service;
import it.unicam.cs.ids.loyalty.model.Customer;
import it.unicam.cs.ids.loyalty.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the CrudService for Customer entities.
 */
@Service
public class DefaultCustomerService implements CrudService<Customer> {

    private final CustomerRepository customerRepository;

    @Autowired
    public DefaultCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
        // Add logic here to validate or further process the merchant before updating
        return customerRepository.save(customer);
    }

    @Override
    public void delete(int id) {
        customerRepository.deleteById(id);
    }

}
