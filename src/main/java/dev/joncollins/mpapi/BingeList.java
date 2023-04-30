package dev.joncollins.mpapi;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document
public class BingeList {
    @Id
    private String id;
    private String name;
    private final String owner;
    private List<String> listUsers;
    private List<MediaItem> titles;
    private int mediaCount = 0;
    private int totalPages = 0;
    private LocalDateTime createdAt = LocalDateTime.now();
    @PersistenceConstructor
    public BingeList(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.listUsers = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    public boolean addMediaItemToTitles(MediaItem item) {
        if (this.titles.contains(item)) {
            return false;
        }
        this.titles.add(item);
        this.addToMediaCount();
        return true;
    }

    public boolean removeMediaItemFromTitles(MediaItem item) {
        if (this.titles.contains(item)) {
            this.titles.remove(item);
            reduceMediaCount();
            return true;
        }
        return false;
    }

    private void addToMediaCount() {
        this.mediaCount++;
        this.calculateTotalPages();
    }
    private void reduceMediaCount() {
        this.mediaCount--;
        this.calculateTotalPages();
    }
    private void calculateTotalPages() {
        //pagination will be 20 items per page
        int pagination = 20;
        float mc = (float) this.getMediaCount();
        int pages = (int) Math.ceil(mc / pagination);
        if (pages != this.totalPages) {
            this.setTotalPages(pages);
        }
    }

    public boolean addUserToBingeList(String id) {
        if (this.listUsers.contains(id)) {
            return false;
        }
        this.listUsers.add(id);
        return true;
    }

    public boolean removeUserFromBingeList(String id) {
        if (!this.listUsers.contains(id)) {
            return false;
        }
        this.listUsers.remove(id);
        return true;
    }
    public MinifiedBingeList getMinifiedBingeList() {
        List<String> movies = new ArrayList<>();
        List<String> tv = new ArrayList<>();
        for (MediaItem title : this.titles) {
            String id = title.getMediaId();
            if (title.getType().equals("movie")) {
                movies.add(id);
            } else {
                tv.add(id);
            }
        }
        return new MinifiedBingeList(this.id, this.name, movies, tv);
    }

}
