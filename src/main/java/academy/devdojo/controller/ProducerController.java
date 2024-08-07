package academy.devdojo.controller;


import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProduceGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import academy.devdojo.response.ProducerPutRequest;
import academy.devdojo.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = {"v1/producers", "v1/producers/"})
@Log4j2
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerMapper mapper;

    private final ProducerService producerService;


    @GetMapping
    public ResponseEntity<List<ProduceGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.info("Request received to list all Producers, param name '{}'", name);
        var producers = producerService.findAll(name);

        var producerGetResponses = mapper.toProducerGetResponse(producers);

        return ResponseEntity.ok(producerGetResponses);

    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-version=v1")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest request) {
        var producer = mapper.toProducer(request);

        producer = producerService.save(producer);

        var response = mapper.toProducerPostResponse(producer);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("Request received to delete the producer by id", id);

        producerService.delete(id);
        
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
        log.info("Request received to update the producer", request);

        var producerToUpdate = mapper.toProducer(request);

        producerService.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

}
