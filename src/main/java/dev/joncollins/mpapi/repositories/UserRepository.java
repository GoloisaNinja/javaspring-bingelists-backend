package dev.joncollins.mpapi.repositories;

import dev.joncollins.mpapi.Token;
import dev.joncollins.mpapi.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Finds and returns a user for api response that does not include tokens or password
//    @Query(value = "{'email': ?0}", fields = "{'password': 0, 'tokens': 0}")
//    Optional<User> findUsersByEmailExcludePasswordAndTokens(String email);
    Optional<User> findUserByEmail(String email);

    @Query(value = "{'isPrivate': false}")
    Optional<List<User>> findUsersByIsPrivate();
}
