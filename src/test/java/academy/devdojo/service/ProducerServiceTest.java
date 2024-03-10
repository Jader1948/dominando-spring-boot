package academy.devdojo.service;

import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService service;

    @Mock
    private ProducerHardCodedRepository repository;

    private List<Producer> producers;

    @BeforeEach
    void init() {
        var Ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var WitStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var StudioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
        producers = new ArrayList<>(List.of(Ufotable, WitStudio, StudioGhibli));
    }

    @Test
    @DisplayName("findAll() returns a list with all producers")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        BDDMockito.when(repository.findByName(null)).thenReturn(this.producers);

        var producers = service.findAll(null);
        Assertions.assertThat(producers).hasSameElementsAs(this.producers);
    }

    @Test
    @DisplayName("findAll() returns a list with found producers when name is not null")
    @Order(2)
    void findAll_ReturnsFoundProducers_WhenNameIsPassedAndFound() {
        var name = "Ufotable";
        var producersFound = this.producers.stream()
                .filter(producer -> producer.getName().equals(name)).toList();
        BDDMockito.when(repository.findByName(name)).thenReturn(producersFound);

        var producers = service.findAll(name);
        Assertions.assertThat(producers).hasSize(1).contains(producersFound.get(0));
    }

    @Test
    @DisplayName("findAll() returns an empty list when no producer is found by name")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenSuccessful() {
        var name = "x";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var producers = service.findAll(name);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByID() returns a optional producer when id exists")
    @Order(4)
    void findByID_ReturnsOptinionalProducer_WhenIdExists() {
        var id = 1L;
        var producerExpected = this.producers.get(0);

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producerExpected));

        var producerOptional = service.findById(id);

        Assertions.assertThat(producerOptional).isPresent().contains(producerExpected);


    }

    @Test
    @DisplayName("findByID() returns a empty optional when id does not exists")
    @Order(5)
    void findByID_ReturnsEmptyOptional_WhenIdExists() {
        var id = 1L;
        var producerExpected = this.producers.get(0);

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        var producerOptional = service.findById(id);

        Assertions.assertThat(producerOptional).isEmpty();


    }

    @Test
    @DisplayName("save() Creates producer")
    @Order(6)
    void save_createProducer_WhenSuccessful() {
        var producerToBeSave = Producer.builder()
                .id(99L)
                .name("MAPPA")
                .createdAt(LocalDateTime.now())
                .build();
        BDDMockito.when(repository.save(producerToBeSave)).thenReturn(producerToBeSave);

        var producer = service.save(producerToBeSave);

        Assertions.assertThat(producer)
                .isEqualTo(producerToBeSave)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete() removes a producer")
    @Order(7)
    void delete_removesProducer_WhenSuccessful() {
        var id = 1L;
        var producerToDelete = this.producers.get(0);

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producerToDelete));
        BDDMockito.doNothing().when(repository).delete(producerToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(id));
    }

    @Test
    @DisplayName("delete() removes throw ResponseStatusException when no producer is found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenNoProducerIsFound() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(id))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update() update a producer")
    @Order(9)
    void update_UpdateProducer_WhenSuccessful() {
        var id = 1L;
        var producerToBeUpdate = this.producers.get(0);
        producerToBeUpdate.setName("Aniplex");

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producerToBeUpdate));
        BDDMockito.doNothing().when(repository).update(producerToBeUpdate);


        Assertions.assertThatNoException().isThrownBy(()-> service.update(producerToBeUpdate));

    }

    @Test
    @DisplayName("update() throw ResponseStatusException when no producer is found")
    @Order(10)
    void update_ThrowsResponseException_WhenNoProduceIsFound() {
        var id = 1L;
        var producerToBeUpdate = this.producers.get(0);
        producerToBeUpdate.setName("Aniplex");

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(()->service.update(producerToBeUpdate))
                .isInstanceOf(ResponseStatusException.class);

    }

}