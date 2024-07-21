package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {


    private final List<Anime> animes = new ArrayList<>();

     {
        var drstone =  Anime.builder().id(1L).name("drStone").build();
        var drDebora  =  Anime.builder().id(2L).name("drDebora").build();
        var jader =  Anime.builder().id(3L).name("jader").build();

        animes.addAll(List.of(jader, drstone, drDebora));

    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
