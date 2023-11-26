package it.unicam.cs.ids.loyalty.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import it.unicam.cs.ids.loyalty.model.LoyaltyProgram;
import it.unicam.cs.ids.loyalty.service.DefaultLoyaltyProgramService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loyaltyPrograms")
public class LoyaltyProgramController {

    private final DefaultLoyaltyProgramService loyaltyProgramService;

    @Autowired
    public LoyaltyProgramController(DefaultLoyaltyProgramService loyaltyProgramService) {
        this.loyaltyProgramService = loyaltyProgramService;
    }

    @GetMapping
    public List<LoyaltyProgram> getAllLoyaltyPrograms() {
        return loyaltyProgramService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<LoyaltyProgram> getLoyaltyProgramById(@PathVariable int id) {
        return loyaltyProgramService.getById(id)
               ;
    }

    @GetMapping("/byname/{programName}")
    public Optional<LoyaltyProgram> getLoyaltyProgramByName(@PathVariable String programName) {
        return loyaltyProgramService.getByName(programName)
                ;}

    @PostMapping
    public ResponseEntity<LoyaltyProgram> createLoyaltyProgram(
            @RequestParam String programName,
            @RequestParam String description,
            @RequestParam boolean isCoalition,
            @RequestParam int merchantId) {
        LoyaltyProgram loyaltyProgram = loyaltyProgramService.createLoyaltyProgram(programName, description, isCoalition, merchantId);
        return new ResponseEntity<>(loyaltyProgram, HttpStatus.CREATED);
    }
}

