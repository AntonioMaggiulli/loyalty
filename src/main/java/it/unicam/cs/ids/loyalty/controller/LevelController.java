package it.unicam.cs.ids.loyalty.controller;

import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.service.CrudService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
public class LevelController {

    private final CrudService<Level> levelService;

    @Autowired
    public LevelController(CrudService<Level> levelService) {
        this.levelService = levelService;
    }

    @GetMapping
    public List<Level> getAllLevels() {
        return levelService.getAll();
    }

    @GetMapping("/{id}")
    public Level getLevelById(@PathVariable int id) {
        return levelService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Level not found with id: " + id));
    }

    @PostMapping
    public Level createLevel(@RequestBody Level level) {
        return levelService.create(level);
    }

    @PutMapping("/{id}")
    public Level updateLevel(@PathVariable int id, @RequestBody Level level) {
        if (levelService.getById(id).isPresent()) {
            level.setUniqueId(id);
            return levelService.update(level);
        } else {
            throw new EntityNotFoundException("Level not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteLevel(@PathVariable int id) {
        levelService.delete(id);
    }
}
