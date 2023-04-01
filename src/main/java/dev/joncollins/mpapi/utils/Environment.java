package dev.joncollins.mpapi.utils;

import io.github.cdimascio.dotenv.Dotenv;

public interface Environment {
    Dotenv dotenv = Dotenv.load();
}
