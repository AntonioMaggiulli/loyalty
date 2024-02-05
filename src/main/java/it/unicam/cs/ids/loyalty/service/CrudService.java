package it.unicam.cs.ids.loyalty.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

	List<T> getAll();

	Optional<T> getById(int id);

	Optional<T> getByName(String name);

	T create(T entity);

	T update(T entity);

	void delete(int id);
}
