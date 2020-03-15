package org.neustupov.ratingsdataservice;

import org.neustupov.models.Rating;
import org.neustupov.repository.RatingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan("org.neustupov")
@EnableReactiveMongoRepositories
public class RatingsDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingsDataServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RatingRepository ratingRepository) {
		return args -> ratingRepository.deleteAll()
				.thenMany(Flux.just(
						new Rating("500", 5.0),
						new Rating("600", 4.0)
				)
						.flatMap(ratingRepository::save))
				.thenMany(ratingRepository.findAll())
				.subscribe(System.out::println);
	}

}
