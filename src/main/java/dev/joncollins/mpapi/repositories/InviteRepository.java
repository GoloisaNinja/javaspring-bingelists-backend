package dev.joncollins.mpapi.repositories;

import dev.joncollins.mpapi.Invite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface InviteRepository extends MongoRepository<Invite, String> {
    Optional<Invite> findInviteByIdAndInvitedUserId(String id, String invitedId);
}
