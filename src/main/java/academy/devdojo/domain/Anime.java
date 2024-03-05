package academy.devdojo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    @EqualsAndHashCode.Include
    private Long id;
    @JsonProperty(value="name")
    private String name;
    private static List<Anime> animes = new ArrayList<>();

    static {
        var drstone =  Anime.builder().id(2L).name("drStone").build();
        var drDebora  =  Anime.builder().id(3L).name("drDebora").build();
        var jader =  Anime.builder().id(1L).name("jader").build();

        animes.addAll(List.of(jader, drstone, drDebora));

    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
