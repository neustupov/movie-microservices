package org.neustupov.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.neustupov.models.CatalogItem;
import org.neustupov.models.Movie;
import org.neustupov.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/{userId}")
  public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

    Movie movie = restTemplate.getForObject("http://localhost:8091/movies/1", Movie.class);

    List<Rating> ratings = Arrays.asList(
        new Rating("Terminator", 5),
        new Rating("Witcher", 4)
    );

    return ratings.stream().map(rating -> new CatalogItem("Transformers", "UFO Mechanism", 3)).collect(
        Collectors.toList());
  }
}
