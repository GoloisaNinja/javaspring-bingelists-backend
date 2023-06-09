package dev.joncollins.mpapi.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.joncollins.mpapi.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class ResReq implements GsonTypeAdapter {
    public static URI uriBuilder(String endpoint) {
        try {
            return new URI(Constants.baseTMDBURI + endpoint + Constants.api_key);
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResponse<String> responseHandler(HttpClient c, HttpRequest r) {
        try {
            return c.send(r, HttpResponse.BodyHandlers.ofString());
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> returnJsonStringAsMap(String json) {
        Gson gson = returnAdaptedGson();
        return gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
    }

    public Map<String, Map<String, Object>> combineResponses(Map<String, String> respMap) {
        Map<String, Map<String, Object>> combinedResponseMap = new HashMap<>();
        for (String key : respMap.keySet()) {
            Map<String, Object> jsonObj = returnJsonStringAsMap(respMap.get(key));
            combinedResponseMap.put(key, jsonObj);
        }
        return combinedResponseMap;
    }
}
