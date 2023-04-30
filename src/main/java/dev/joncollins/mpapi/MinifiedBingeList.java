package dev.joncollins.mpapi;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class MinifiedBingeList {
    private String id;
    private String name;
    private List<String> movie;
    private List<String> tv;
    public MinifiedBingeList(String id, String name, List<String> movie, List<String> tv) {
        this.id = id;
        this.name = name;
        this.movie = new ArrayList<>(movie);
        this.tv = new ArrayList<>(tv);
    }
}
