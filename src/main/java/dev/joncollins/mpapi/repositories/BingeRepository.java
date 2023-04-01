package dev.joncollins.mpapi.repositories;

import dev.joncollins.mpapi.BingeList;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BingeRepository extends MongoRepository<BingeList, String> {
   @Query("{'$or': [{'owner': ?0},{'listUsers' : {'$in': [?0]}}]}")
   List<BingeList> findBingeListsByOwnerAndUsers(String id, Sort sort);
   @Query("{'$and': [{'id': ?0}, {'$or': [{'owner': ?1},{'listUsers': {'$in': [?1]}}]}]}")
   Optional<BingeList> findBingeListById(String id, String user);
   Optional<BingeList> findBingeListByIdAndOwner(String id, String owner);
}
