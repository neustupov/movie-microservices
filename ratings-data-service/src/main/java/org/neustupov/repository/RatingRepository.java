package org.neustupov.repository;

import org.neustupov.models.Rating;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends ReactiveMongoRepository<Rating, String> {

}
