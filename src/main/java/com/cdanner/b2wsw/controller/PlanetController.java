package com.cdanner.b2wsw.controller;

import javax.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.cdanner.b2wsw.model.Planet;
import com.cdanner.b2wsw.service.IPlanetService;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

	@Autowired
	private IPlanetService planetService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	List<Planet> getAll(@RequestParam(required = false) String name, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "50") int per_page) {
		return planetService.getAll(name, page, per_page);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	Planet getById(@PathVariable String id) {
		return planetService.getById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Planet postPlanet(@Valid @RequestBody Planet planet) {
		Planet createdPlanet = planetService.insert(planet);
		return createdPlanet;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	Planet putPlanet(@Valid @RequestBody Planet planet) {
		Planet updatedPlanet = planetService.update(planet);
		return updatedPlanet;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteById(@PathVariable String id) {
		planetService.deleteById(id);
	}
}
