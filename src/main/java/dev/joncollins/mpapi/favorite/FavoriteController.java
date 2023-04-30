package dev.joncollins.mpapi.favorite;

import dev.joncollins.mpapi.MediaItem;
import dev.joncollins.mpapi.MinifiedFavorites;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/v1/favorite")
@AllArgsConstructor
public class FavoriteController {
    private FavoriteService favoriteService;

    @GetMapping("/favorites/minified")
    public ResponseEntity<MinifiedFavorites> fetchMinifiedFavorites(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(favoriteService.fetchMinifiedFavorites(auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
    @PostMapping("/favorites/add")
    public ResponseEntity<String> addToFavorites(@RequestBody MediaItem item,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(favoriteService.addToFavorites(item, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }
    @PostMapping("/favorites/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody MediaItem item,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(favoriteService.removeFromFavorites(item, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }
}
