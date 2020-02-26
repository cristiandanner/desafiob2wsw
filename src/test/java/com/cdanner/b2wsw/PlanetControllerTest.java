package com.cdanner.b2wsw;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.cdanner.b2wsw.model.Planet;
import com.cdanner.b2wsw.repository.PlanetRepository;
import com.cdanner.b2wsw.service.ISwapiService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class PlanetControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private final String NAME_PLANET_TEST = "Hoth";
	private final String WRONG_NAME_PLANET_TEST = "WRONG_NAME_PLANET_TEST";
	private final String WRONG_ID_PLANET_TEST = "WRONG_ID_PLANET_TEST";

	@Autowired
	private PlanetRepository planetRepository;

	@Autowired
	private ISwapiService swapiService;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		mappingJackson2HttpMessageConverter = Arrays.stream(converters)
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("O conversor JSON não pode ser nulo.", mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(webApplicationContext).build();
		planetRepository.deleteAll();
	}

	@SuppressWarnings("unchecked")
	private String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	private Planet getRealTestPlanet(String planetName) throws Exception {
		Planet planet = planetRepository.getByName(planetName);
		if (planet == null)
			planet = planetRepository.insert(new Planet(planetName, "tundra, ice caves, mountain ranges", "frozen"));
		try {
			planet.setFilmsCount(swapiService.getSwapiPlanet(planetName).getFilmsCount());
			return planet;
		} catch (Exception e) {
			throw new Exception("Falha ao preencher a base.");
		}
	}

	@Test
	public void getAll() throws Exception {
		List<Planet> planetList = new ArrayList<>();
		planetList.add(getRealTestPlanet("Bespin"));
		planetList.add(getRealTestPlanet("Naboo"));

		String planetListJson = json(planetList);

		mockMvc.perform(get("/api/planets").contentType(contentType)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(content().json(planetListJson));
	}

	@Test
	public void getById() throws Exception {
		Planet planet = getRealTestPlanet(NAME_PLANET_TEST);

		mockMvc.perform(get("/api/planets/" + planet.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.id", is(planet.getId())))
				.andExpect(jsonPath("$.name", is(planet.getName())))
				.andExpect(jsonPath("$.climate", is(planet.getClimate())))
				.andExpect(jsonPath("$.terrain", is(planet.getTerrain())))
				.andExpect(jsonPath("$.filmsCount", is(planet.getFilmsCount())));
	}

	@Test
	public void getByWrongId() throws Exception {
		mockMvc.perform(get("/api/planets/" + WRONG_ID_PLANET_TEST)).andExpect(status().isNotFound());
	}

	@Test
	public void getByName() throws Exception {
		List<Planet> planetList = new ArrayList<>();
		planetList.add(getRealTestPlanet(NAME_PLANET_TEST));
		
		String planetListJson = json(planetList);

		mockMvc.perform(get("/api/planets?name=" + NAME_PLANET_TEST)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(content().json(planetListJson));
	}

	@Test
	public void getByWrongName() throws Exception {
		mockMvc.perform(get("/api/planets/find?name=" + WRONG_NAME_PLANET_TEST)).andExpect(status().isNotFound());
	}

	@Test
	public void postPlanet() throws Exception {
		Planet planet = getRealTestPlanet(NAME_PLANET_TEST);
		String planetJson = json(planet);

		planetRepository.deleteAll();

		mockMvc.perform(post("/api/planets").contentType(contentType).content(planetJson)).andExpect(status().isCreated())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.name", is(planet.getName())))
				.andExpect(jsonPath("$.climate", is(planet.getClimate())))
				.andExpect(jsonPath("$.terrain", is(planet.getTerrain())))
				.andExpect(jsonPath("$.filmsCount", is(planet.getFilmsCount())));
	}

	@Test
	public void postPlanetWithConflictName() throws Exception {
		Planet planet = getRealTestPlanet(NAME_PLANET_TEST);
		String planetJson = json(planet);
		mockMvc.perform(post("/api/planets").contentType(contentType).content(planetJson)).andExpect(status().isConflict());
	}

	@Test
	public void postFakePlanet() throws Exception {
		String planetJson = json(new Planet(WRONG_NAME_PLANET_TEST, "tropical", "rock"));
		mockMvc.perform(post("/api/planets").contentType(contentType).content(planetJson)).andExpect(status().isNotFound());
	}
	
	@Test
	public void putPlanet() throws Exception {
		Planet planet = getRealTestPlanet(NAME_PLANET_TEST);
		planet.setClimate("temperate, arid");
		planet.setTerrain("rock, desert, mountain, barren");
		String planetJson = json(planet);
			
		mockMvc.perform(put("/api/planets").contentType(contentType).content(planetJson)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.name", is(planet.getName())))
				.andExpect(jsonPath("$.climate", is(planet.getClimate())))
				.andExpect(jsonPath("$.terrain", is(planet.getTerrain())))
				.andExpect(jsonPath("$.filmsCount", is(planet.getFilmsCount())));
		
		planetRepository.deleteAll();
	}
	
	@Test
	public void putFakePlanet() throws Exception {
		String planetJson = json(new Planet(WRONG_NAME_PLANET_TEST, "temperate, arid", "rock, desert, mountain, barren"));
		mockMvc.perform(put("/api/planets").contentType(contentType).content(planetJson)).andExpect(status().isNotFound());
	}

	@Test(expected = NoSuchElementException.class)
	public void deleteById() throws Exception {
		Planet planet = getRealTestPlanet(NAME_PLANET_TEST);
		mockMvc.perform(delete("/api/planets/" + planet.getId()).contentType(contentType))
				.andExpect(status().isNoContent());

		planetRepository.findById(planet.getId()).get();
	}

	@Test
	public void deleteByWrongId() throws Exception {
		mockMvc.perform(delete("/api/planets/" + WRONG_ID_PLANET_TEST).contentType(contentType))
				.andExpect(status().isNotFound());
	}
}
