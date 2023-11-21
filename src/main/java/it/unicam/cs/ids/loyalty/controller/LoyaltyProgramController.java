package it.unicam.cs.ids.loyalty.controller;
import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.service.CrudService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for managing LoyaltyProgram-related requests.
 */

@RestController
@RequestMapping("/api/loyaltyPrograms")
public class LoyaltyProgramController {

    private final CrudService<LoyaltyProgram> loyaltyProgramService;

    @Autowired
    public LoyaltyProgramController(CrudService<LoyaltyProgram> loyaltyProgramService) {
        this.loyaltyProgramService = loyaltyProgramService;
    }

    @GetMapping
    public List<LoyaltyProgram> getAllLoyaltyPrograms() {
        return loyaltyProgramService.getAll();
    }

    @GetMapping("/{id}")
    public LoyaltyProgram getLoyaltyProgramById(@PathVariable int id) {
        return loyaltyProgramService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("LoyaltyProgram not found with id: " + id));
    }

    @GetMapping("/byname/{programName}")
    public LoyaltyProgram getLoyaltyProgramByName(@PathVariable String programName) {
        return loyaltyProgramService.getByName(programName)
                .orElseThrow(() -> new EntityNotFoundException("LoyaltyProgram not found with name: " + programName));
    }
}
