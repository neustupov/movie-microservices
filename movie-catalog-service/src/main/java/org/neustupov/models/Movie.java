package org.neustupov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

  private String movieId;
  private String title;
  private String overview;
  private Double vote_average;
}
