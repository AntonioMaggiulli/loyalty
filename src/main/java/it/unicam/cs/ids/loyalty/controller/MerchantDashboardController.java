package it.unicam.cs.ids.loyalty.controller;

import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.service.CrudService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant-dashboard")
public class MerchantDashboardController {

    private final CrudService<Merchant> merchantService;

    @Autowired
    public MerchantDashboardController(CrudService<Merchant> merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public Merchant getMerchantById(@RequestParam int merchantId) {
        return merchantService.getById(merchantId)
                .orElseThrow(() -> new EntityNotFoundException("Merchant not found with id: " + merchantId));
    }
}
