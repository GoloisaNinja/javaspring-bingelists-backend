package dev.joncollins.mpapi.tmdb.search;

import dev.joncollins.mpapi.utils.ResReq;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class SearchService extends ResReq {
    private HttpClient client = HttpClient.newHttpClient();

    public Map<String, Map<String, Object>> getSearchResults(String query) throws ServerErrorException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String movieSearchEndpoint = "/search/movie?query=" + encodedQuery +
                         "&language=en-US&page=1&include_adult=false&api_key=";

        String tvSearchEndpoint = "/search/tv?query=" + encodedQuery +
                                     "&language=en-US&page=1&include_adult=false&api_key=";

        URI movieSearchURI = uriBuilder(movieSearchEndpoint);
        URI tvSearchURI = uriBuilder(tvSearchEndpoint);
        // Requests
        HttpRequest movieSearchReq = HttpRequest.newBuilder()
                .uri(movieSearchURI)
                .build();
        HttpRequest tvSearchReq = HttpRequest.newBuilder()
                .uri(tvSearchURI)
                .build();
        // Responses
        HttpResponse<String> movieSearchRes = responseHandler(client, movieSearchReq);
        HttpResponse<String> tvSearchRes = responseHandler(client, tvSearchReq);
        if (movieSearchRes == null || movieSearchRes.statusCode() != 200) {
            throw new ServerErrorException("internal server error", new Throwable());
        }
        if (tvSearchRes == null || tvSearchRes.statusCode() != 200) {
            throw new ServerErrorException("internal server error", new Throwable());
        }
        Map<String, String> respMap = new HashMap<>();
        respMap.put("movie", movieSearchRes.body());
        respMap.put("tv", tvSearchRes.body());
        return combineResponses(respMap);
    }
}
