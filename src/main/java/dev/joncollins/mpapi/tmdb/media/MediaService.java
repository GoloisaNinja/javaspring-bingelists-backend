package dev.joncollins.mpapi.tmdb.media;

import com.google.gson.Gson;
import dev.joncollins.mpapi.utils.GsonTypeAdapter;
import dev.joncollins.mpapi.utils.ResReq;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class MediaService extends ResReq implements GsonTypeAdapter {
    private HttpClient client = HttpClient.newHttpClient();

    public Map<String,Map<String, Object>> fetchAllMediaAttributes(String media_type, String media_id) throws ServerErrorException {
        String base = "/" + media_type + "/" + media_id;
        // string endpoints
        String mediaEndpoint = base + "?append_to_response=videos&api_key=";
        String creditsEndpoint = base + "/credits?api_key=";
        String providersEndpoint = base + "/watch/providers?language=en-US&api_key=";
        String similarsEndpoint = base + "/similar?page=1&api_key=";
        // uri from endpoints
        URI mediaURI = uriBuilder(mediaEndpoint);
        URI creditsURI = uriBuilder(creditsEndpoint);
        URI providersURI = uriBuilder(providersEndpoint);
        URI similarsURI = uriBuilder(similarsEndpoint);
        // requests from uri
        HttpRequest mediaRequest = HttpRequest.newBuilder().uri(mediaURI).build();
        HttpRequest creditsRequest = HttpRequest.newBuilder().uri(creditsURI).build();
        HttpRequest providersRequest = HttpRequest.newBuilder().uri(providersURI).build();
        HttpRequest similarsRequest = HttpRequest.newBuilder().uri(similarsURI).build();
        // responses
        HttpResponse<String> mediaResponse = responseHandler(client, mediaRequest);
        HttpResponse<String> creditsResponse = responseHandler(client, creditsRequest);
        HttpResponse<String> providersResponse = responseHandler(client, providersRequest);
        HttpResponse<String> similarsResponse = responseHandler(client, similarsRequest);
        // null checks
        String mBody = null;
        String cBody = null;
        String pBody = null;
        String sBody = null;
        if (mediaResponse != null) {
            mBody = mediaResponse.body();
        }
        if (creditsResponse != null) {
            cBody = creditsResponse.body();
        }
        if (providersResponse != null) {
            pBody = providersResponse.body();
        }
        if (similarsResponse != null) {
            sBody = similarsResponse.body();
        }
        // create the map and response
        if (mediaResponse != null && mediaResponse.statusCode() == 200) {
            Map<String, String> respMap = new HashMap<>();
            respMap.put("media", mBody);
            respMap.put("credits", cBody);
            respMap.put("providers", pBody);
            respMap.put("similars", sBody);
            return combineResponses(respMap);
        } else {
            throw new ServerErrorException("internal server error", new Throwable());
        }


    }
}
