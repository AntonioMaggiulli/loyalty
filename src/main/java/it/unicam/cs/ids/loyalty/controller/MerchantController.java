package it.unicam.cs.ids.loyalty.controller;

import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.service.CrudService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final CrudService<Merchant> merchantService;

    @Autowired
    public MerchantController(CrudService<Merchant> merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public List<Merchant> getAllMerchants() {
        return merchantService.getAll();
    }

    @GetMapping("/{id}")
    public Merchant getMerchantById(@PathVariable int id) {
        return merchantService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Merchant not found with id: " + id));
    }

    @GetMapping("/byname/{name}")
    public Merchant getMerchantByName(@PathVariable String name) {
        return merchantService.getByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Merchant not found with name: " + name));
    }

}
