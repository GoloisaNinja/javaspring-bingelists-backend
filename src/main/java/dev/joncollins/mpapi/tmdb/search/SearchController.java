package dev.joncollins.mpapi.tmdb.search;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.Map;

@RestController
@RequestMapping("api/v1/search")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping
    public ResponseEntity<Map<String, Map<String, Object>>> getSearchResults(@RequestParam String query) {
        try {
            return ResponseEntity.ok(service.getSearchResults(query));
        } catch(ServerErrorException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
