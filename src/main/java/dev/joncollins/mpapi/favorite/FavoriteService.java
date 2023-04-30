package dev.joncollins.mpapi.favorite;

import dev.joncollins.mpapi.Favorites;
import dev.joncollins.mpapi.MediaItem;
import dev.joncollins.mpapi.MinifiedFavorites;
import dev.joncollins.mpapi.User;
import dev.joncollins.mpapi.auth.AuthService;
import dev.joncollins.mpapi.repositories.FavoriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@AllArgsConstructor
@Service
public class FavoriteService {
    private FavoriteRepository repo;
    private AuthService authService;

    public MinifiedFavorites fetchMinifiedFavorites(String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400));
        }
        Favorites favorites = repo.findFavoritesByOwnerId(user.getId()).orElse(null);
        if (favorites == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404));
        }
        return favorites.getMinifiedFavorites();
    }
    public String addToFavorites(MediaItem item, String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        Favorites favorites = repo.findFavoritesByOwnerId(user.getId()).orElse(null);
        if (favorites == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "bad request");
        }
        boolean added = favorites.addToFavorites(item);
        if (added) {
            repo.save(favorites);
            return "media item added to favorites";
        } else {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "media item not added");
        }
    }
    public String removeFromFavorites(MediaItem item, String auth) throws HttpClientErrorException {
        User user = authService.returnUserDetailsByToken(auth);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "bad request");
        }
        Favorites favorites = repo.findFavoritesByOwnerId(user.getId()).orElse(null);
        if (favorites == null) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "bad request");
        }
        boolean removed = favorites.removeFromFavorites(item);
        if (removed) {
            repo.save(favorites);
            return "media item removed from favorites";
        } else {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(400), "media item not removed");
        }
    }
}
