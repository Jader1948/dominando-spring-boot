package academy.devdojo.service;


import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeHardCodedRepository repository;

    private List<Anime> animes;


    @BeforeEach
    void init(){
        var ShingekiNoKyoin = Anime.builder().id(1L).name("ShingekiNoKyoin").build();
        var SteinsGate = Anime.builder().id(2L).name("Steins Gate").build();
        var Mashle = Anime.builder().id(3L).name("Mashle").build();
        animes = new ArrayList<>(List.of(ShingekiNoKyoin, SteinsGate, Mashle));
    }
    @Test
    @DisplayName("findAll() returns a list with all animes")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        BDDMockito.when(repository.findByName(null)).thenReturn(this.animes);

        var producers = service.findAll(null);
        Assertions.assertThat(producers).hasSameElementsAs(this.animes);
    }

    @Test
    @DisplayName("findAll() returns a list with found animes when name is not null")
    @Order(2)
    void findAll_ReturnsFoundAnimes_WhenNameIsPassedAndFound() {
        var name = "Mashle";
        var animesFound = this.animes.stream()
                .filter(anime -> anime.getName().equals(name)).toList();
        BDDMockito.when(repository.findByName(name)).thenReturn(animesFound);

        var animes = service.findAll(name);
        Assertions.assertThat(animes).hasSize(1).contains(animesFound.get(0));
    }


    @Test
    @DisplayName("findAll() returns an empty list when no animes is found by name")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenSuccessful() {
        var name = "x";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var animes = service.findAll(name);
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByID() returns a optional anime when id exists")
    @Order(4)
    void findByID_ReturnsOptinionalAnime_WhenIdExists() {
        var id = 1L;
        var producerExpected = this.animes.get(0);

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producerExpected));

        var animeOptional = service.findById(id);

        Assertions.assertThat(animeOptional).isEqualTo(producerExpected);


    }

    @Test
    @DisplayName("findByID() throw ResponseStatusException when no animes is found")
    @Order(5)
    void findByID_ThrowsResponseStatusException_WhenNoAnimeIsFound() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions
                .assertThatException()
                .isThrownBy(()-> service.findById(id))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @DisplayName("save() Creates anime")
    @Order(6)
    void save_createAnime_WhenSuccessful() {
        var animeToBeSave = Anime.builder()
                .id(99L)
                .name("Hajime No Ippo")
                .build();
        BDDMockito.when(repository.save(animeToBeSave)).thenReturn(animeToBeSave);
        var anime = service.save(animeToBeSave);

        Assertions.assertThat(anime)
                .isEqualTo(animeToBeSave)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete() removes a anime")
    @Order(7)
    void delete_removesAnime_WhenSuccessful() {
        var id = 1L;
        var animeToDelete = this.animes.get(0);

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(id));
    }

    @Test
    @DisplayName("delete() throw ResponseStatusException when no anime is found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenNoAnimeIsFound() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(id))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update() update a anime")
    @Order(9)
    void update_UpdateAnime_WhenSuccessful() {
        var id = 1L;
        var animeToBeUpdate = this.animes.get(0);
        animeToBeUpdate.setName("DBZ");

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeToBeUpdate));
        BDDMockito.doNothing().when(repository).update(animeToBeUpdate);


        Assertions.assertThatNoException().isThrownBy(()-> service.update(animeToBeUpdate));

    }

    @Test
    @DisplayName("update() throw ResponseStatusException when no anime is found")
    @Order(10)
    void update_ThrowsResponseException_WhenNoAnimeIsFound() {
        var id = 1L;
        var animeToBeUpdate = this.animes.get(0);
        animeToBeUpdate.setName("Naruto");

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(()->service.update(animeToBeUpdate))
                .isInstanceOf(ResponseStatusException.class);

    }

}