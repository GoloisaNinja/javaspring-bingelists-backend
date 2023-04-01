package dev.joncollins.mpapi.repositories;

import dev.joncollins.mpapi.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    @Query("{'$and': [{'user': ?0}, {'expired': false}, {'revoked': false}]}")
    List<Token> findTokensByUser(String id);
    Optional<Token> findByToken(String token);
}
