package org.neustupov.resources;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.neustupov.models.CatalogItem;
import org.neustupov.models.Movie;
import org.neustupov.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Qualifier("webClientBuilder")
  private final WebClient.Builder webClientbuilder;

  @Autowired
  public MovieCatalogResource(Builder webClientbuilder) {
    this.webClientbuilder = webClientbuilder;
  }

  @GetMapping("/{userId}")
  public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

    log.info("Request to: http://ratings-data-service/ratingsdata/users/" + userId);
    UserRating userRating = webClientbuilder.build()
        .get()
        .uri("http://ratings-data-service/ratingsdata/users/" + userId)
        .retrieve()
        .bodyToMono(UserRating.class)
        .block();

    return userRating.getUserRatings().stream().map(rating -> {
      log.info("Request to: http://movie-info-service/movies/" + rating.getMovieId());

      Movie movie = webClientbuilder.build()
          .get()
          .uri("http://movie-info-service/movies/" + rating.getMovieId())
          .retrieve()
          .bodyToMono(Movie.class)
          .block();

      return new CatalogItem(movie.getTitle(), movie.getOverview(), movie.getVote_average());
    }).collect(
        Collectors.toList());
  }
}
