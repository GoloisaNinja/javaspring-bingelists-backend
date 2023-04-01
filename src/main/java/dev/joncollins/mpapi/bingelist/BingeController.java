package dev.joncollins.mpapi.bingelist;

import dev.joncollins.mpapi.BingeList;
import dev.joncollins.mpapi.MediaItem;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bingelist")
@AllArgsConstructor
public class BingeController {
    private BingeService bingeService;

    @PostMapping("/create")
    public ResponseEntity<BingeList> createBingeList(@RequestBody Map<String, String> body,
                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String name = body.get("name");
        return ResponseEntity.ok(bingeService.createBingeList(name, auth));
//        try {
//            String json = bingeService.createBingeList(name, auth);
//            return ResponseEntity.ok(json);
//        } catch(HttpClientErrorException e) {
//            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
//        }
    }
    @PostMapping("/add")
    public ResponseEntity<MediaItem> addMediaItemToBingeList(@RequestParam String id,
                                                          @RequestBody MediaItem item,
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(bingeService.addItemToBingeList(id, item, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }

    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeMediaItemFromBingeList(@RequestParam Map<String, String> params,
                                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String id = params.get("id");
        String item = params.get("item");
        try {
            return ResponseEntity.ok(bingeService.removeItemFromBingeList(id, item, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }
    @PostMapping("/add/user")
    public ResponseEntity<BingeList> addUserToBingeList(@RequestParam String id,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return ResponseEntity.ok(bingeService.addUserToBingeList(id, auth));
    }
    @GetMapping
    public ResponseEntity<BingeList> fetchBingeListById(@RequestParam String id,
                                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(bingeService.fetchBingeListById(id, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }

    }
    @GetMapping("/lists")
    public ResponseEntity<List<BingeList>> fetchAllBingeLists(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(bingeService.fetchBingeListsByOwnerAndListUser(auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }

    }
}
