package it.unicam.cs.ids.loyalty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;

@RestController
@RequestMapping("/")
public class HomeController {

    private final DefaultLoyaltyProgramService loyaltyProgramService;

    @Autowired
    public HomeController(DefaultLoyaltyProgramService loyaltyProgramService) {
        this.loyaltyProgramService = loyaltyProgramService;
    }

    @GetMapping
    public String home() {
        return "Welcome to the Loyalty Program Manager!";
    }

    @PostMapping("/createLoyaltyProgram")
    public ResponseEntity<String> createLoyaltyProgram(@RequestBody LoyaltyProgram loyaltyProgram) {
        try {
            LoyaltyProgram createdProgram = loyaltyProgramService.createLoyaltyProgram(loyaltyProgram);
            return ResponseEntity.ok("Loyalty Program created with ID: " + createdProgram.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Loyalty Program");
        }
    }
}
