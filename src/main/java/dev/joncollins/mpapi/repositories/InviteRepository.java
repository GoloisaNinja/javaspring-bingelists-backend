package dev.joncollins.mpapi.repositories;

import dev.joncollins.mpapi.Invite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends MongoRepository<Invite, String> {
    Optional<Invite> findInviteByIdAndInvitedUserId(String id, String invitedId);
    Optional<Invite> findInviteByBingeListIdAndInvitedUserId(String listId, String invitedId);
    Optional<Invite> findInviteByIdAndInvitedById(String id, String invitedById);
    Optional<List<Invite>> findInvitesByInvitedById(String id);
}
