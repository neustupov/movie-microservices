package org.neustupov.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.time.Duration;
import java.util.Collections;
import lombok.extern.log4j.Log4j2;
import org.neustupov.models.Rating;
import org.neustupov.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Log4j2
@Service
public class UserRatingInfo {

  private final String RATINGS_DATA_SERVICE_USERS_URI = "http://ratings-data-service/ratingsdata/users/";

  private final Integer timeout = 5000;

  @Qualifier("webClientBuilder")
  private final WebClient.Builder webClientbuilder;

  @Autowired
  public UserRatingInfo(Builder webClientbuilder) {
    this.webClientbuilder = webClientbuilder;
  }

  @HystrixCommand(fallbackMethod = "getFallbackUserRating",
      commandProperties = {
          @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
          @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
          @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
          @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
      },
      threadPoolKey = "movieInfoPool",
      threadPoolProperties = {
          @HystrixProperty(name = "coreSize", value = "20"),
          @HystrixProperty(name = "maxQueueSize", value = "10")
      })
  public UserRating getUserRating(String userId){

    log.info("Request to: " + RATINGS_DATA_SERVICE_USERS_URI + userId);

    return webClientbuilder.build()
        .get()
        .uri(RATINGS_DATA_SERVICE_USERS_URI + userId)
        .retrieve()
        .bodyToMono(UserRating.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnError(error -> log.error("Error signal detected", error))
        .block();
  }

  @SuppressWarnings("unused")
  public UserRating getFallbackUserRating(String userId){
    return new UserRating(Collections.singletonList(new Rating("777", 0)));
  }
}
