package it.unicam.cs.ids.loyalty.service;
import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the CrudService for Merchant entities.
 */
@Service
public class DefaultMerchantService implements CrudService<Merchant> {

    private final MerchantRepository merchantRepository;

    @Autowired
    public DefaultMerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
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
        // Add logic here to validate or further process the merchant before updating
        return merchantRepository.save(merchant);
    }

    @Override
    public void delete(int id) {
        merchantRepository.deleteById(id);
    }

}
