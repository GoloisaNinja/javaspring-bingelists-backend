package dev.joncollins.mpapi.bingelist;

import dev.joncollins.mpapi.BingeList;
import dev.joncollins.mpapi.Invite;
import dev.joncollins.mpapi.MediaItem;
import dev.joncollins.mpapi.User;
import dev.joncollins.mpapi.auth.AuthService;
import dev.joncollins.mpapi.repositories.BingeRepository;
import dev.joncollins.mpapi.repositories.InviteRepository;
import dev.joncollins.mpapi.repositories.UserRepository;
import dev.joncollins.mpapi.utils.GsonTypeAdapter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class BingeService implements GsonTypeAdapter {
    private BingeRepository repo;
    private AuthService authService;

    public BingeList createBingeList(String name, String auth) throws HttpStatusCodeException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user != null) {
            String owner = user.getId();
            BingeList list = new BingeList(name, owner);
            repo.insert(list);
            return list;
        } else {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
    }
    public List<BingeList> fetchBingeListsByOwnerAndListUser(String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(403), "forbidden");
        }
        List<BingeList> lists = repo.findBingeListsByOwnerAndUsers(user.getId(), Sort.by(Sort.Order.desc("createdAt")));
        if (lists.size() > 0) {
            return lists;
        } else {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "no lists");
        }
    }
    public BingeList fetchBingeListById(String id, String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(403), "forbidden");
        }
        BingeList list = repo.findBingeListById(id, user.getId()).orElse(null);
        if (list != null) {
            return list;
        } else {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "list not found");
        }
    }

    public MediaItem addItemToBingeList(String id, MediaItem item, String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(403), "forbidden");
        }
        BingeList list = repo.findBingeListById(id, user.getId()).orElse(null);
        if (list != null) {
            boolean added = list.addMediaItemToTitles(item);
            if (added) {
                repo.save(list);
                return item;
            } else {
                throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request - duplicate");
            }
        } else {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "list not found");
        }
    }

    public String removeItemFromBingeList(String id, String item, String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(403), "forbidden");
        }
        BingeList list = repo.findBingeListById(id, user.getId()).orElse(null);
        if (list != null) {
            boolean removed = list.removeMediaItemFromTitles(item);
            if (removed) {
                repo.save(list);
                return "item removed successfully";
            } else {
                throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "nothing to remove");
            }
        } else {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
    }

    public boolean addUserToBingeList(String listId, String userToAddId) {
        BingeList list = repo.findById(listId).orElse(null);
        if (list == null) {
            return false;
        }
        // Add the invited user to the bingelist
        boolean added = list.addUserToBingeList(userToAddId);
        if (!added) {
            return false;
        }
        repo.save(list);
        return true;
    }

    public BingeList removeUserFromBingeList(String id, String listId, String auth) throws HttpClientErrorException {
        User listOwner = authService.returnUserDetailsByToken(auth);
        if (listOwner == null) {
            throw new NoSuchElementException();
        }
        BingeList list = repo.findBingeListByIdAndOwner(listId, listOwner.getId()).orElse(null);
        if (list == null) {
            throw new NoSuchElementException();
        }
        // Add the invited user to the bingelist
        boolean removed = list.removeUserFromBingeList(id);
        if (!removed) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "couldn't remove user");
        }
        repo.save(list);
        return list;
    }
}
