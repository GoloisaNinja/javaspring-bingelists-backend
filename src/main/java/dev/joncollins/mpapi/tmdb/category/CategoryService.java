package dev.joncollins.mpapi.tmdb.category;

import dev.joncollins.mpapi.utils.GsonTypeAdapter;
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
public class CategoryService extends ResReq implements GsonTypeAdapter {
    private HttpClient client = HttpClient.newHttpClient();

    private HttpResponse<String> getResponse(URI endpoint) {
        HttpRequest req = HttpRequest.newBuilder().uri(endpoint).build();
        return responseHandler(client, req);
    }
    private String getString(URI catListEndpoint) throws ServerErrorException {
        HttpResponse<String> catResp = getResponse(catListEndpoint);
        if (catResp.statusCode() == 200) {
           return catResp.body();
        }
        throw new ServerErrorException("internal server error", new Throwable());
    }
    public Map<String, Map<String, Object>> fetchCategoryList() {
        URI movieCatListEndpoint = uriBuilder("/genre/movie/list?api_key=");
        URI tvCatListEndpoint = uriBuilder("/genre/tv/list?api_key=");
        String mCatResult = getString(movieCatListEndpoint);
        String tvCatResult = getString(tvCatListEndpoint);
        Map<String, String> respMap = new HashMap<>();
        respMap.put("movie", mCatResult);
        respMap.put("tv", tvCatResult);
        return combineResponses(respMap);
    }

    public String fetchCategoryByTypeAndPage(CategoryRequest req) throws ServerErrorException {
        String sortStr = req.getSort_by().equals("rating") ?
                "sort_by=vote_average.desc&vote_count.gte=50" :
                "sort_by" + "=popularity.desc";
        String categoryBase =
                "/discover/" + req.getMedia_type() +
                "?language=en-US&include_adult=false&include_video=false&" + sortStr;
        String categoryGenreAndPage = "&with_genres=" +
                                      req.getGenre() + "&page=" +
                                      req.getPage() + "&api_key=";
        URI categoryTypePageEndpoint = uriBuilder(categoryBase + categoryGenreAndPage);
        HttpResponse<String> catResp = getResponse(categoryTypePageEndpoint);
        if (catResp.statusCode() == 200) {
            return catResp.body();
        } else {
            throw new ServerErrorException("internal server error", new Throwable());
        }
    }
}
