package dev.joncollins.mpapi;

import dev.joncollins.mpapi.utils.Environment;

public class Constants {
    public static final String baseTMDBURI = "https://api.themoviedb.org/3";
    public static final String api_key = System.getenv("TMDB_APIKEY");
    public static final String SECRET = System.getenv("JWT_SECRET");
    public static final long EXPIRATION_TIME = 86_400_000;

}
