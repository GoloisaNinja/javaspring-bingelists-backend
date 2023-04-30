package dev.joncollins.mpapi.repositories;

import dev.joncollins.mpapi.Favorites;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<Favorites, String> {
    Optional<Favorites> findFavoritesByOwnerId(String ownerId);
}
