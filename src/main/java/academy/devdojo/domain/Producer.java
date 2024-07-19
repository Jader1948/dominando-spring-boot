package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Producer {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private static List<Producer> producers = new ArrayList<>();

    static {
        var mappa = Producer.builder().id(1L).name("MAPPA").createdAt(LocalDateTime.now()).build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
        var madHouse  = Producer.builder().id(1L).name("MadHouse").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(mappa, kyotoAnimation, madHouse));

    }

    public static List<Producer> getProducers() {
        return producers;
    }

}
