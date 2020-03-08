package org.neustupov.resources;

import java.util.Arrays;
import java.util.List;
import org.neustupov.models.Rating;
import org.neustupov.models.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratingsdata")
public class RatingResource {

  @GetMapping("/{movieId}")
  public Rating getRating(@PathVariable("movieId") String movieId){
    return new Rating(movieId, 1.0);
  }

  @GetMapping("/users/{userId}")
  public UserRating getUserRating(@PathVariable("userId") String userId){

    List<Rating> ratings = Arrays.asList(
        new Rating("500", 5.0),
        new Rating("600", 4.0)
    );
    return new UserRating(ratings);
  }

}
