package com.cfecweb.leon;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application configuration values from the server-side
 * app.properties file on the classpath.
 *
 * Values are loaded once at class initialization and accessed via
 * the static get() methods.
 */
public class AppProperties {

    private static final Properties PROPERTIES = new Properties();

    public static final String APP_VERSION = "app.version";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";

    public static final String CYBERSOURCE_REDIRECT_URL = "cybersource.redirect_url";
    public static final String CYBERSOURCE_ACCESS_KEY = "cybersource.access_key";
    public static final String CYBERSOURCE_PROFILE_ID = "cybersource.profile_id";
    public static final String CYBERSOURCE_SECRET_KEY = "cybersource.secret_key";

    public static final String RECAPTCHA_SITE_KEY = "recaptcha.site_key";
    public static final String RECAPTCHA_ACTION = "recaptcha.action";
    public static final String RECAPTCHA_SECRET_KEY = "recaptcha.secret_key";
    public static final String RECAPTCHA_HOSTNAME = "recaptcha.hostname";
    public static final String RECAPTCHA_MIN_SCORE = "recaptcha.min_score";
    public static final String RECAPTCHA_VERIFIER_URL = "recaptcha.verifier_url";

    static {
        try (InputStream input =
                     AppProperties.class.getClassLoader().getResourceAsStream("app.properties")) {

            if (input == null) {
                throw new RuntimeException("app.properties file not found in classpath");
            }

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load app.properties", e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
