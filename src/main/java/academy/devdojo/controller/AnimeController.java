package academy.devdojo.controller;


import academy.devdojo.domain.Anime;
import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"v1/animes", "v1/animes/"})
@Log4j2
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {
        log.info("Request received to list all animes, param name '{}'", name);
        var animes = Anime.getAnimes();
        var animesgetResponses = MAPPER.toAnimeGetResponses(animes);
        if (name == null) return ResponseEntity.ok(animesgetResponses);

        animesgetResponses = animesgetResponses
                .stream().
                filter(anime -> anime.getName().equalsIgnoreCase(name)).
                toList();

        return ResponseEntity.ok(animesgetResponses);

    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.info("Request received to by id '{}'", id);

        var animeFound = Anime.getAnimes()
                .stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElse(null);
        var response = MAPPER.toAnimeGetResponse(animeFound);

        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest request) {
        log.info("Request received to save anime '{}'", request);
        var anime = MAPPER.toAnime(request);
        var response = MAPPER.toAnimePostResponse(anime);

        Anime.getAnimes().add(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
