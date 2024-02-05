package it.unicam.cs.ids.loyalty.service;

import it.unicam.cs.ids.loyalty.model.Level;
import it.unicam.cs.ids.loyalty.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service("mainLevelService")
public class DefaultLevelService implements CrudService<Level> {
	private final LevelRepository levelRepository;

	@Autowired
	public DefaultLevelService(LevelRepository levelRepository) {
		this.levelRepository = levelRepository;
	}

	@Override
	public List<Level> getAll() {
		return levelRepository.findAll();
	}

	@Override
	public Optional<Level> getById(int id) {
		return levelRepository.findById(id);
	}

	public Optional<Level> getByName(String name) {
		return levelRepository.findByName(name);
	}

	@Override
	public Level create(Level entity) {
		return levelRepository.save(entity);
	}

	@Override
	public Level update(Level entity) {
		return levelRepository.save(entity);
	}

	@Override
	public void delete(int id) {
		levelRepository.deleteById(id);
	}
}