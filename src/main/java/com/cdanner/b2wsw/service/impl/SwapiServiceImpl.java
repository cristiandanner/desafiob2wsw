package com.cdanner.b2wsw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.cdanner.b2wsw.exception.NotFoundException;
import com.cdanner.b2wsw.model.SwapiPlanet;
import com.cdanner.b2wsw.model.SwapiSearch;
import com.cdanner.b2wsw.service.ISwapiService;
import com.cdanner.b2wsw.util.Constants;

@Service
public class SwapiServiceImpl implements ISwapiService {

	@Autowired
	private RestTemplate restTemplate;

	@Cacheable("SwapiPlanetCache")
	public SwapiPlanet getSwapiPlanet(String planetName) {
		String urlSwapiSearch = Constants.SWAPI_PLANETS_SEARCH_URL + planetName;
		SwapiSearch swapiSearch = restTemplate.getForObject(urlSwapiSearch, SwapiSearch.class);

		if (CollectionUtils.isEmpty(swapiSearch.getResults()))
			throw new NotFoundException("Utilize um nome válido para buscar na SWAPI");

		return swapiSearch.getResults().stream().filter(swapiPlanet -> swapiPlanet.getName().equals(planetName))
				.findFirst()
				.orElseThrow(() -> new NotFoundException("Utilize um nome válido para buscar na SWAPI"));
	}
}
