package com.cfecweb.leon.config;

import com.cfecweb.leon.AppProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.annotation.Nullable;

/**
 * Provides a lazily initialized singleton SessionFactory. Overrides connection properties
 * from environment variables (preferred) or system properties (fallback for tests).
 *
 * Environment variables checked:
 *  LEON_DB_URL, LEON_DB_USERNAME, LEON_DB_PASSWORD, LEON_DB_DRIVER
 */
public final class HibernateSessionFactoryProvider {
    private static final Logger LOGGER = LogManager.getLogger(HibernateSessionFactoryProvider.class);

    private static volatile SessionFactory INSTANCE;

    public static final String LEON_DB_URL = "LEON_DB_URL";
    public static final String LEON_DB_USERNAME = "LEON_DB_USERNAME";
    public static final String LEON_DB_PASSWORD = "LEON_DB_PASSWORD";
    public static final String LEON_DB_DRIVER = "LEON_DB_DRIVER";

    private HibernateSessionFactoryProvider() {}

    public static SessionFactory getSessionFactory() {
        if (INSTANCE == null) {
            synchronized (HibernateSessionFactoryProvider.class) {
                if (INSTANCE == null) {
                    Configuration cfg = new Configuration().configure();
                    overrideDbConfigFromEnv(cfg);
                    INSTANCE = cfg.buildSessionFactory();
                }
            }
        }
        return INSTANCE;
    }

    public static void overrideDbConfigFromEnv(Configuration cfg) {
        setConfigValueIfPresent(cfg, "hibernate.connection.url", getEnvOrDefault(LEON_DB_URL, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_URL)));
        setConfigValueIfPresent(cfg, "hibernate.connection.username", getEnvOrDefault(LEON_DB_USERNAME, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_USERNAME)));
        setConfigValueIfPresent(cfg, "hibernate.connection.password", getEnvOrDefault(LEON_DB_PASSWORD, AppProperties.get(AppProperties.HIBERNATE_CONNECTION_PASSWORD)));
        setConfigValueIfPresent(cfg, "hibernate.connection.driver_class", getEnv(LEON_DB_DRIVER));
    }

    public static @Nullable String getEnv(String key) {
        return getEnvOrDefault(key, null);
    }

    public static @Nullable String getEnvOrDefault(String key, @Nullable String defaultValue) {
        String env = System.getenv(key);
        return env == null ? defaultValue : env;
    }

    public static void setConfigValueIfPresent(Configuration cfg, String hibernateKey, @Nullable String value) {
        if (value != null && !value.isEmpty()) {
            LOGGER.info("Hibernate config {} set as {}", hibernateKey, value);
            cfg.setProperty(hibernateKey, value);
        } else {
            LOGGER.info("Hibernate config {} kept as {}", hibernateKey, cfg.getProperty(hibernateKey));
        }
    }
}
