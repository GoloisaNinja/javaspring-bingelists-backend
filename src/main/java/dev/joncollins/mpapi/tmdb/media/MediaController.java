package dev.joncollins.mpapi.tmdb.media;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("api/v1/media")
@AllArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    @GetMapping
    public ResponseEntity<Map<String, Map<String, Object>>> fetchAllMediaAttributes(@RequestParam Map<String,
            String> params) {
        String media_type = params.get("media_type");
        String media_id = params.get("media_id");
        return ResponseEntity.ok(mediaService.fetchAllMediaAttributes(media_type, media_id));
    }
}
