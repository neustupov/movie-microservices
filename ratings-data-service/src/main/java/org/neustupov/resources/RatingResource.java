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
    return new Rating(movieId, 1);
  }

  @GetMapping("/users/{userId}")
  public UserRating getUserRating(@PathVariable("userId") String userId){

    List<Rating> ratings = Arrays.asList(
        new Rating("1", 5),
        new Rating("2", 4)
    );
    return new UserRating(ratings);
  }

}
