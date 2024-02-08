package academy.devdojo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Anime {

    private Long id;
    @JsonProperty(value="name")
    private String name;
    private static List<Anime> animes = new ArrayList<>();

    static {
        var jader = new Anime(1L,"jader");
        var drstone = new Anime(2L,"drStone");
        var drDebora  = new Anime(3L,"drDebora");

        animes.addAll(List.of(jader, drstone, drDebora));

    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
