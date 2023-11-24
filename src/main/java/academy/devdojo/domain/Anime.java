package academy.devdojo.domain;

import java.util.List;

public class Anime {

    private Long id;
    private String name;

    public Anime(Long id, String name){
        this.id = id;
        this.name = name;
    }
    public static List<Anime>  getAnimes(){
        var jader = new Anime(1L,"jader");
        var drstone = new Anime(2L,"drStone");
        var drDebora  = new Anime(3L,"drDebora");
        return List.of(jader, drstone, drDebora);

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
