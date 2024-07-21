package academy.devdojo.controller;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(AnimeController.class)
class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeData animeData;
    @SpyBean
    private AnimeHardCodedRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init(){
        var ShingekiNoKyoin = Anime.builder().id(1L).name("ShingekiNoKyoin").build();
        var SteinsGate = Anime.builder().id(2L).name("Steins Gate").build();
        var Mashle = Anime.builder().id(3L).name("Mashle").build();
        var animes = new ArrayList<>(List.of(ShingekiNoKyoin, SteinsGate, Mashle));

        BDDMockito.when(animeData.getAnimes()).thenReturn(animes);
    }

    @Test
    @DisplayName("findAll() returns a list with all animes")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() throws Exception{
        var response = readResourcesFile("get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findAll() returns a list with found animes when name is not null")
    @Order(2)
    void findAll_ReturnsFoundAnimes_WhenNameIsPassedAndFound() throws Exception{
        var name = "Mashle";
        var response =  readResourcesFile("get-anime-mashle-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findAll() returns an empty list when no animes is found by name")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenSuccessful() throws Exception{
        var name = "x";
        var response = readResourcesFile("get-anime-x-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findByID() returns a optional anime when id exists")
    @Order(5)
    void findByID_ReturnsOptinionalAnime_WhenIdExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}",1L)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("save() Creates anime")
    @Order(6)
    void save_createAnime_WhenSuccessful() throws Exception{
        var request = readResourcesFile("post-request-anime-201.json");
        var response = readResourcesFile("post-response-anime-201.json");
        var animeToSave = Anime.builder()
            .id(99L)
            .name("Hajime No Ippo")
            .build();
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/animes")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("update() update a anime")
    @Order(7)
    void update_UpdateAnime_WhenSuccessful() throws Exception{
        var request = readResourcesFile("put-request-anime-204.json");

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/animes")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("update() throw ResponseStatusException when no anime is found")
    @Order(8)
    void update_ThrowsResponseException_WhenNoAnimeIsFound() throws Exception{
        var request = readResourcesFile("put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/animes")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    @DisplayName("delete() removes a anime")
    @Order(9)
    void delete_removesAnime_WhenSuccessful() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/v1/animes/{id}", 1L)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("delete() throw ResponseStatusException when no anime is found")
    @Order(10)
    void delete_ThrowsResponseStatusException_WhenNoAnimeIsFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/v1/animes/{id}", 1111L)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    private String readResourcesFile(String fileName) throws Exception{
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }


}