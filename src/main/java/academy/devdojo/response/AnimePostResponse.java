package academy.devdojo.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class AnimePostResponse {

    private Long id;
    private String name;
}
