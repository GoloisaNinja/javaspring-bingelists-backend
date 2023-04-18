package dev.joncollins.mpapi.tmdb.media;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("api/v1/media")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    @GetMapping
    public ResponseEntity<Map<String, Map<String, Object>>> fetchAllMediaAttributes(@RequestParam Map<String,
            String> params) {
        String media_type = params.get("media_type");
        String media_id = params.get("media_id");
        try {
            return ResponseEntity.ok(mediaService.fetchAllMediaAttributes(media_type, media_id));
        } catch(ServerErrorException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
}
