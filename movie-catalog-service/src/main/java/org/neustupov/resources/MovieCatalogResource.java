package org.neustupov.resources;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.neustupov.models.CatalogItem;
import org.neustupov.models.UserRating;
import org.neustupov.services.MovieInfo;
import org.neustupov.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

  @Autowired
  private MovieInfo movieInfo;

  @Autowired
  private UserRatingInfo userRatingInfo;

  @GetMapping("/{userId}")
  public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

    UserRating userRating = userRatingInfo.getUserRating(userId);

    return userRating.getUserRatings().stream().map(rating -> movieInfo.getCatalogItem(rating)).collect(
        Collectors.toList());
  }
}
