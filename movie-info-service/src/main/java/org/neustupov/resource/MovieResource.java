package org.neustupov.resource;

import lombok.extern.log4j.Log4j2;
import org.neustupov.models.Movie;
import org.neustupov.models.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Log4j2
@RestController
@RequestMapping("/movies")
public class MovieResource {

  private final String DB_URI = "https://api.themoviedb.org/3/movie/";

  @Value("${api.key}")
  private String apiKey;

  @Qualifier("webClientBuilder")
  private final WebClient.Builder webClientbuilder;

  @Autowired
  public MovieResource(Builder webClientbuilder) {
    this.webClientbuilder = webClientbuilder;
  }

  @GetMapping("/{movieId}")
  public Movie getMovieInfo(@PathVariable("movieId") String movieId){

    log.info(DB_URI + movieId);
    MovieSummary movieSummary = webClientbuilder.build()
        .get()
        .uri(DB_URI + movieId + "?api_key=" + apiKey)
        .retrieve()
        .bodyToMono(MovieSummary.class)
        .block();
    return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview(), movieSummary.getVote_average());
  }
}
