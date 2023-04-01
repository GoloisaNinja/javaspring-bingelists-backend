package dev.joncollins.mpapi;

import dev.joncollins.mpapi.utils.Environment;

public class Constants implements Environment {
    public static final String baseTMDBURI = "https://api.themoviedb.org/3";
    public static final String api_key = dotenv.get("TMDB_APIKEY");
    public static final String SECRET = dotenv.get("JWT_SECRET");
    public static final long EXPIRATION_TIME = 86_400_000;

}
