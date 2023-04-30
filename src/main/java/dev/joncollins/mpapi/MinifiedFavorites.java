package dev.joncollins.mpapi;

import lombok.Data;

import java.util.List;

@Data
public class MinifiedFavorites {
    private String ownerId;
    private List<String> movieIds;
    private List<String> tvIds;

    public MinifiedFavorites(String ownerId, List<String> movie, List<String> tv) {
        this.ownerId = ownerId;
        this.movieIds = movie;
        this.tvIds = tv;
    }
}
