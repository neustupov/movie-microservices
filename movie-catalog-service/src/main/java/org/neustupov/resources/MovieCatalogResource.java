package org.neustupov.resources;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.neustupov.models.CatalogItem;
import org.neustupov.models.Movie;
import org.neustupov.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Log4j2
@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

  private final String RATINGS_DATA_SERVICE_USERS_URI = "http://ratings-data-service/ratingsdata/users/";
  private final String MOVIE_INFO_SERVICE_MOVIES_URI = "http://movie-info-service/movies/";

  private final Integer timeout = 5000;

  @Qualifier("webClientBuilder")
  private final WebClient.Builder webClientbuilder;

  @Autowired
  public MovieCatalogResource(Builder webClientbuilder) {
    this.webClientbuilder = webClientbuilder;
  }

  @GetMapping("/{userId}")
  public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

    log.info("Request to: " + RATINGS_DATA_SERVICE_USERS_URI + userId);

    UserRating userRating = webClientbuilder.build()
        .get()
        .uri(RATINGS_DATA_SERVICE_USERS_URI + userId)
        .retrieve()
        .onStatus(HttpStatus::isError, clientResponse -> {
          log.error("Error while calling endpoint {} with status code {}",
              RATINGS_DATA_SERVICE_USERS_URI, clientResponse.statusCode());
          throw new RuntimeException("Error while calling  accounts endpoint");
        })
        .bodyToMono(UserRating.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnError(error -> log.error("Error signal detected", error))
        .block();

    return userRating.getUserRatings().stream().map(rating -> {

      log.info("Request to: " + MOVIE_INFO_SERVICE_MOVIES_URI + rating.getMovieId());

      Movie movie = webClientbuilder.build()
          .get()
          .uri(MOVIE_INFO_SERVICE_MOVIES_URI + rating.getMovieId())
          .retrieve()
          .onStatus(HttpStatus::isError, clientResponse -> {
            log.error("Error while calling endpoint {} with status code {}",
                MOVIE_INFO_SERVICE_MOVIES_URI, clientResponse.statusCode());
            throw new RuntimeException("Error while calling  accounts endpoint");
          })
          .bodyToMono(Movie.class)
          .timeout(Duration.ofMillis(timeout))
          .doOnError(error -> log.error("Error signal detected", error))
          .block();

      return new CatalogItem(movie.getTitle(), movie.getOverview(), movie.getVote_average());
    }).collect(
        Collectors.toList());
  }
}
