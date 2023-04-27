package dev.joncollins.mpapi;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MinifiedBingeList {
    private String id;
    private String name;
    private Map<String, List<String>> minifiedMediaItems;
    public MinifiedBingeList(String id, String name, Map<String, List<String>> mediaItems) {
        this.id = id;
        this.name = name;
        this.minifiedMediaItems = new HashMap<>(mediaItems);
    }
}
