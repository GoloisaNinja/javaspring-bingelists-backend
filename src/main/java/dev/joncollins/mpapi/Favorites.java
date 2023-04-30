package dev.joncollins.mpapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Favorites {
    @Id
    private String id;
    private String ownerId;
    private List<MediaItem> favorites;

    public boolean addToFavorites(MediaItem item) {
        if (this.favorites.contains(item)) {
            return false;
        }
        this.favorites.add(item);
        return true;
    }
    public boolean removeFromFavorites(MediaItem item) {
        if (this.favorites.contains(item)) {
            this.favorites.remove(item);
            return true;
        }
        return false;
    }

    public MinifiedFavorites getMinifiedFavorites() {
        List<String> movie = new ArrayList<>();
        List<String> tv = new ArrayList<>();
        for(MediaItem item : this.favorites) {
            if (item.getType().equals("movie")) {
                movie.add(item.getMediaId());
            } else {
                tv.add(item.getMediaId());
            }
        }
        return new MinifiedFavorites(this.ownerId, movie, tv);
    }
}
