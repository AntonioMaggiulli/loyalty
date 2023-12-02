package it.unicam.cs.ids.loyalty.controller;

import it.unicam.cs.ids.loyalty.model.Merchant;
import it.unicam.cs.ids.loyalty.service.DefaultMerchantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewMerchantController {

    private final DefaultMerchantService merchantService;

    public NewMerchantController(DefaultMerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/newmerchant")
    public String showNewMerchantForm(Model model) {
        // Aggiungi un oggetto Merchant vuoto al model
        model.addAttribute("merchant", new Merchant());
        return "newmerchant";
    }

    @PostMapping("/newmerchant")
    public String addNewMerchant(@ModelAttribute("merchant") Merchant merchant) {
        // Salva il nuovo merchant nel repository
        merchantService.create(merchant);
        
        // Puoi decidere di reindirizzare a una pagina specifica dopo l'aggiunta del merchant
        return "redirect:/merchant";
    }

}

