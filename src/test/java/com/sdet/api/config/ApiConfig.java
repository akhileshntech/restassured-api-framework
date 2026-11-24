package com.sdet.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Resolution order: -Dkey=value  >  TEST_KEY environment variable  >  config.properties
 * Example: mvn test -DbaseUrl=https://staging.example.com
 */
public final class ApiConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ApiConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load config.properties", e);
        }
    }

    private ApiConfig() {
    }

    public static String baseUrl() {
        return get("baseUrl");
    }

    public static String authUsername() {
        return get("authUsername");
    }

    public static String authPassword() {
        return get("authPassword");
    }

    private static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv("TEST_" + key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase());
        }
        if (value == null || value.isBlank()) {
            value = PROPS.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration value: " + key);
        }
        return value.trim();
    }
}
