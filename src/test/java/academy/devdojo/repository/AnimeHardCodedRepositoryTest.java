package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository repository;

    @Mock
    private AnimeData animeData;

    private List<Anime> animes;

    @BeforeEach
    void init() {
        var ShingekiNoKyoin = Anime.builder().id(1L).name("ShingekiNoKyoin").build();
        var SteinsGate = Anime.builder().id(2L).name("Steins Gate").build();
        var Mashle = Anime.builder().id(3L).name("Mashle").build();
        animes = new ArrayList<>(List.of(ShingekiNoKyoin, SteinsGate, Mashle));

        BDDMockito.when(animeData.getAnimes()).thenReturn(animes);
    }

    @Test
    @DisplayName("findAll() Returns All list of animes")
    @Order(1)
    void findAll_ReturnAllAnimes_WhenSuccessful() {
        var animes = repository.findAll();
        Assertions.assertThat(animes).hasSameElementsAs(this.animes);
    }

    @Test
    @DisplayName("findById() returns a object with given id")
    @Order(2)
    void findById_ReturnsAAnime_WhenSuccessful() {
        var animeOptionalAnime = repository.findById(3L);
        Assertions.assertThat(animeOptionalAnime).isPresent().contains(animes.get(2));
    }

    @Test
    @DisplayName("findByName() return all animes when name is null")
    @Order(3)
    void findByName_ReturnsALlAnimes_WhenNameIsNull() {
        var animes = repository.findByName(null);
        Assertions.assertThat(animes).hasSameElementsAs(this.animes);
    }

    @Test
    @DisplayName("findByName() return list with filtered animes when name is not  null")
    @Order(4)
    void findByName_ReturnsAnime_WhenNameIsNotNull() {
        var animes = repository.findByName("Mashle");
        Assertions.assertThat(animes).hasSize(1).contains(this.animes.get(2));

    }

    @Test
    @DisplayName("findByName() return empty list when no animes is found")
    @Order(5)
    void findByName_ReturnsAllAnime_WhenothingFound() {
        var animes = repository.findByName("XXXX");
        Assertions.assertThat(animes).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("save() create a anime")
    @Order(6)
    void save_CreateAnime_WhenSuccessful() {
       var animeTobeSaved = Anime.builder()
               .id(99l)
               .name("Hajime No Ippo")
               .build();
       var anime = repository.save(animeTobeSaved);

       Assertions.assertThat(anime)
               .isEqualTo(animeTobeSaved)
               .hasNoNullFieldsOrProperties();

       var animes = repository.findAll();

       Assertions.assertThat(animes).contains(animeTobeSaved);

    }

    @Test
    @DisplayName("Delete() Delete a anime")
    @Order(7)
    void delete_DeleteAnime_WhenSuccessful() {
        var animeToBeDeleted = this.animes.get(0);
        repository.delete(animeToBeDeleted);
        Assertions.assertThat(this.animes).doesNotContain(animeToBeDeleted);

    }

    @Test
    @DisplayName("Update() Update a Anime")
    @Order(8)
    void update_UpdateAANime_WhenSuccessful(){
        var animesToBeUpdate = this.animes.get(0);
        animesToBeUpdate.setName("MichaelJakcson");

        repository.update(animesToBeUpdate);

        Assertions.assertThat(this.animes).contains(animesToBeUpdate);

        this.animes
                .stream()
                .filter(anime -> anime.getId().equals(animesToBeUpdate.getId()))
                .findFirst()
                .ifPresent(anime -> Assertions.assertThat(anime.getName()).isEqualTo(animesToBeUpdate.getName()));
    }


}