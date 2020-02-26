package com.cdanner.b2wsw.service.impl;

import com.cdanner.b2wsw.exception.ConflictException;
import com.cdanner.b2wsw.exception.NotFoundException;
import com.cdanner.b2wsw.model.Planet;
import com.cdanner.b2wsw.model.SwapiPlanet;
import com.cdanner.b2wsw.repository.PlanetRepository;
import com.cdanner.b2wsw.service.IPlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetServiceImpl implements IPlanetService {

	@Autowired
	private SwapiServiceImpl swapiService;

	@Autowired
	private PlanetRepository planetRepository;

	@Override
	public Planet insert(Planet planet) {
		SwapiPlanet swapiPlanet = swapiService.getSwapiPlanet(planet.getName());

		Planet savedPlanet = getByName(planet.getName());
		if (savedPlanet != null)
			throw new ConflictException("Planeta com esse nome já foi salvo anteriomente.");

		Planet createdPlanet = planetRepository.insert(planet);
		createdPlanet.setFilmsCount(swapiPlanet.getFilmsCount());
		return createdPlanet;
	}
	
	@Override
	public Planet update(Planet planet) {
		SwapiPlanet swapiPlanet = swapiService.getSwapiPlanet(planet.getName());
		
		Planet savedPlanet = getByName(planet.getName());
		if (savedPlanet == null)
			throw new NotFoundException(String.format("Planeta com o nome %s não foi encontrado", planet.getName()));

		savedPlanet.setClimate(planet.getClimate());
		savedPlanet.setTerrain(planet.getTerrain());
		
		Planet updatedPlanet = planetRepository.save(savedPlanet);
		updatedPlanet.setFilmsCount(swapiPlanet.getFilmsCount());
		return updatedPlanet;
	}

	@Override
	public Planet getById(String id) {
		Planet planet = planetRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Planeta com o id %s não foi encontrado", id)));
		planet.setFilmsCount(swapiService.getSwapiPlanet(planet.getName()).getFilmsCount());
		return planet;
	}

	@Override
	public List<Planet> getAll(String name, int page, int per_page) {
		Page<Planet> planetsPages = planetRepository.findAll(PageRequest.of(page - 1, per_page));
		List<Planet> planets = planetsPages.getContent();

		if (name != null) {
			planets.stream().filter(planet -> planet.getName().equals(name)).findFirst().orElseThrow(
					() -> new NotFoundException(String.format("Planeta com o nome %s não foi encontrado", name)));
		}

		planets.forEach(planet -> planet.setFilmsCount(swapiService.getSwapiPlanet(planet.getName()).getFilmsCount()));
		return planets;
	}

	@Override
	public void deleteById(String id) {
		if (planetRepository.existsById(id))
			planetRepository.deleteById(id);
		else
			throw new NotFoundException(String.format("Planeta com o id %s não foi encontrado", id));
	}

	@Override
	public Planet getByName(String name) {
		return planetRepository.getByName(name);
	}
}
