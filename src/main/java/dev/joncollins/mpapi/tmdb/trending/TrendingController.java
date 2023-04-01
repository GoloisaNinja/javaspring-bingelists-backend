package dev.joncollins.mpapi.tmdb.trending;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import java.util.Map;

@RestController
@RequestMapping("api/v1/trending")
@AllArgsConstructor
public class TrendingController {
    private final TrendingService trendingService;
    @GetMapping
    public ResponseEntity<String> fetchTrendingByTypeAndPage(@RequestParam Map<String, String> params) {
        String type = params.getOrDefault("media_type", "movie");
        String page = params.getOrDefault("page", "1");
        try {
            return ResponseEntity.ok(trendingService.fetchTrending(type, page));
        } catch(ServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
    @GetMapping("/landing")
    public ResponseEntity<Map<String, Map<String, Object>>> fetchLandingTrending() {
        try {
            return ResponseEntity.ok(trendingService.fetchLandingTrending());
        } catch(ServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
}
