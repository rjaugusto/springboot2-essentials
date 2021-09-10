package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {

    public static void main(String[] args) {


        ResponseEntity<Anime> entity = new RestTemplate()
                .getForEntity("http://localhost:8081/animes/{id}", Anime.class,15);
        log.info(entity);

        var anime = new RestTemplate().getForObject("http://localhost:8081/animes/15", Anime.class);
        log.info(anime);

        var animes = new RestTemplate().getForObject("http://localhost:8081/animes/all", Anime[].class);

        ResponseEntity<List<Anime>> exchange =
                new RestTemplate()
                        .exchange("http://localhost:8081/animes/all",
                                        HttpMethod.GET, null,
                                        new ParameterizedTypeReference<>() {});
        log.info(exchange.getBody());

        Anime anime2 = Anime.builder().name("Iron Maiden").build();

        //Anime animePost = new RestTemplate().postForObject("http://localhost:8081/animes", anime2, Anime.class);

        ResponseEntity<Anime> animePostExchange =
                new RestTemplate()
                        .exchange("http://localhost:8081/animes",
                                HttpMethod.POST, new HttpEntity<>(anime2,createJsonHeader()), Anime.class);

        log.info("Anime POST" + animePostExchange);

        Anime animeToBeUpdated = animePostExchange.getBody();


        assert animeToBeUpdated != null;
        animeToBeUpdated.setName("Rafael");


        ResponseEntity<Void> animePutExchange =
                new RestTemplate()
                        .exchange("http://localhost:8081/animes",
                                HttpMethod.PUT, new HttpEntity<>(animeToBeUpdated,createJsonHeader()), Void.class);

        ResponseEntity<Void> animeDeleteExchange =
                new RestTemplate()
                        .exchange("http://localhost:8081/animes/{id}",
                                HttpMethod.DELETE, null, Void.class,animeToBeUpdated.getId());



    }

    public static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
