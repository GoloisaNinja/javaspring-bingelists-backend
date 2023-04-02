package dev.joncollins.mpapi.invite;

import dev.joncollins.mpapi.BingeList;
import dev.joncollins.mpapi.Invite;
import dev.joncollins.mpapi.User;
import dev.joncollins.mpapi.auth.AuthService;
import dev.joncollins.mpapi.bingelist.BingeService;
import dev.joncollins.mpapi.repositories.InviteRepository;
import dev.joncollins.mpapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class InviteService {
    private InviteRepository inviteRepo;
    private UserRepository userRepo;
    private AuthService authService;
    private BingeService bingeService;

    public List<Invite> fetchAllInvitesByOriginatorId(String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        List<Invite> invites = inviteRepo.findInvitesByInvitedById(user.getId()).orElse(null);
        if (invites == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "no invites");
        }
        return invites;
    }
    public String sendBingelistInviteToUser(Invite invite, String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new NoSuchElementException();
        }
        User invitedUser = userRepo.findById(invite.getInvitedUserId()).orElse(null);
        if (invitedUser == null || invitedUser.getIsPrivate()) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        Invite inviteExists =
                inviteRepo.findInviteByBingeListIdAndInvitedUserId(invite.getBingeListId(), invitedUser.getId()).orElse(null);
        if (inviteExists != null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        boolean successfulInvite = invitedUser.addInvite(invite);
        if (!successfulInvite) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        inviteRepo.save(invite);
        userRepo.save(invitedUser);
        return "invite created - successfully sent to user!";
    }

    public String rescindBingelistInvite(String inviteId, String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        Invite validInvite = inviteRepo.findInviteByIdAndInvitedById(inviteId, user.getId()).orElse(null);
        if (validInvite == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "valid invite not found");
        }
        User invitedUser = userRepo.findById(validInvite.getInvitedUserId()).orElse(null);
        if (invitedUser == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        boolean canRescind = invitedUser.deleteInvite(validInvite);
        if (!canRescind) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        userRepo.save(invitedUser);
        inviteRepo.delete(validInvite);
        return "successfully rescinded invite";
    }

    public String invitedUserAcceptsInvite(String inviteId, String auth) throws HttpClientErrorException {
        User invitedUser = authService.returnUserDetailsByToken(auth);
        if (invitedUser == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        Invite validInvite = inviteRepo.findInviteByIdAndInvitedUserId(inviteId, invitedUser.getId()).orElse(null);
        if (validInvite == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "invalid invite");
        }
        boolean userAddedSuccessfully = bingeService.addUserToBingeList(validInvite.getBingeListId(),
                                                                        invitedUser.getId());
        if (!userAddedSuccessfully) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        boolean deleted = invitedUser.deleteInvite(validInvite);
        if (!deleted) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        userRepo.save(invitedUser);
        inviteRepo.delete(validInvite);
        return "accepted invitation";
    }

    public String invitedUserDeclinesInvite(String id, String auth) throws HttpClientErrorException {
        User invitedUser = authService.returnUserDetailsByToken(auth);
        if (invitedUser == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        Invite validInvite = inviteRepo.findInviteByIdAndInvitedUserId(id, invitedUser.getId()).orElse(null);
        if (validInvite == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "invalid invite");
        }
        boolean deleted = invitedUser.deleteInvite(validInvite);
        if (!deleted) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        userRepo.save(invitedUser);
        inviteRepo.delete(validInvite);
        return "declined invitation - invite removed";
    }
}
