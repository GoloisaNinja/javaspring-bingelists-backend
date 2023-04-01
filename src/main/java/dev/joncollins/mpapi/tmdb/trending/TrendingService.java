package dev.joncollins.mpapi.tmdb.trending;

import com.google.gson.Gson;
import dev.joncollins.mpapi.utils.ResReq;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


@Service
public class TrendingService extends ResReq {
    private HttpClient client = HttpClient.newHttpClient();

    private HttpResponse<String> getBaseResponse(String media_type, String page) {
        URI getTrendingURI = uriBuilder("/trending/" + media_type + "/week?page=" + page + "&api_key=");
        HttpRequest getRequest = HttpRequest.newBuilder().
                                            uri(getTrendingURI).
                                            build();
        return responseHandler(client, getRequest);
    }
    public String fetchTrending(String media_type, String page) throws ServerErrorException {
        HttpResponse<String> httpResponse = getBaseResponse(media_type, page);
        if (httpResponse != null && httpResponse.statusCode() == 200) {
           return httpResponse.body();
        }
        throw new ServerErrorException("internal server error", new Throwable());
    }

    public Map<String, Map<String, Object>> fetchLandingTrending() throws ServerErrorException {
        HttpResponse<String> tmResp = getBaseResponse("movie", "1");
        HttpResponse<String> tvResp = getBaseResponse("tv", "1");
        // null checks
        String tmBody = null;
        String tvBody = null;
        if (tmResp == null) {
            throw new ServerErrorException("server error", new Throwable());
        }
        else {
            tmBody = tmResp.body();
        }
        if (tvResp == null) {
            throw new ServerErrorException("server error", new Throwable());
        }
        else{
            tvBody = tvResp.body();
        }
        Map<String, Object> tmMap = returnJsonStringAsMap(tmBody);
        Map<String, Object> tvMap = returnJsonStringAsMap(tvBody);
        Map<String, Map<String, Object>> combined = new HashMap<>();
        combined.put("movie", tmMap);
        combined.put("tv", tvMap);
        return combined;
    }
}
