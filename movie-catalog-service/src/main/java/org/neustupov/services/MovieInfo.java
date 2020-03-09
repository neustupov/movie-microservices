package org.neustupov.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;
import org.neustupov.models.CatalogItem;
import org.neustupov.models.Movie;
import org.neustupov.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Log4j2
@Service
public class MovieInfo {

  private final String MOVIE_INFO_SERVICE_MOVIES_URI = "http://movie-info-service/movies/";

  private final Integer timeout = 5000;

  @Qualifier("webClientBuilder")
  private final WebClient.Builder webClientbuilder;

  @Autowired
  public MovieInfo(Builder webClientbuilder) {
    this.webClientbuilder = webClientbuilder;
  }

  @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
  public CatalogItem getCatalogItem(Rating rating){

    log.info("Request to: " + MOVIE_INFO_SERVICE_MOVIES_URI + rating.getMovieId());

    Movie movie = webClientbuilder.build()
        .get()
        .uri(MOVIE_INFO_SERVICE_MOVIES_URI + rating.getMovieId())
        .retrieve()
        .bodyToMono(Movie.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnError(error -> log.error("Error signal detected", error))
        .block();

    return new CatalogItem(movie.getTitle(), movie.getOverview(), movie.getVote_average());
  }

  @SuppressWarnings("unused")
  public CatalogItem getFallbackCatalogItem(Rating rating){
    return new CatalogItem("Movie name not found", "", (double) rating.getRating());
  }
}
