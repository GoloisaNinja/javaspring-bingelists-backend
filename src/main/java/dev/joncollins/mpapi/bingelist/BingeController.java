package dev.joncollins.mpapi.bingelist;

import dev.joncollins.mpapi.BingeList;
import dev.joncollins.mpapi.MediaItem;
import dev.joncollins.mpapi.MinifiedBingeList;
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
        try {
            return ResponseEntity.ok(bingeService.createBingeList(name, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }

    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBingeList(@RequestParam String listId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(bingeService.deleteBingeList(listId, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
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
    public ResponseEntity<String> removeMediaItemFromBingeList(@RequestParam String id,
                                                               @RequestBody MediaItem item,
                                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {

        try {
            return ResponseEntity.ok(bingeService.removeItemFromBingeList(id, item, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }

    // A bingelist owner revokes a user that they previously invited successfully
    @PostMapping("/remove/user")
    public ResponseEntity<BingeList> removeUserFromBingeList(@RequestParam Map<String, String> params,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String userToRemoveId = params.get("userId");
        String listId = params.get("listId");
        try {
            return ResponseEntity.ok(bingeService.removeUserFromBingeList(userToRemoveId, listId, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }

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
    @GetMapping("/lists/minified")
    public ResponseEntity<List<MinifiedBingeList>> fetchAllMinifiedBingeLists(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(bingeService.fetchMinifiedBingeLists(auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }

    }
}
