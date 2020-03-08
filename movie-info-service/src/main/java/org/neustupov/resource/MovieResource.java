package org.neustupov.resource;

import java.util.Arrays;
import java.util.List;
import org.neustupov.models.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieResource {

  private List<Movie> movies = Arrays.asList(
      new Movie("1", "Terminator"),
      new Movie("2", "Hobbit")
  );

  @GetMapping("/{movieId}")
  public Movie getMovieInfo(@PathVariable("movieId") String movieId){
    return movies.stream()
        .filter(movie -> movie.getMovieId().equals(movieId))
        .findFirst()
        .orElseGet(null);
  }
}
