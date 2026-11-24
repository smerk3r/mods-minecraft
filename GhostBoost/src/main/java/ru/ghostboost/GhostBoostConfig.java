package ru.ghostboost;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ParticleStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class GhostBoostConfig {
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("ghostboost.properties");

    public static ParticleStatus particleStatus = ParticleStatus.ALL;

    private GhostBoostConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) {
            return;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(FILE)) {
            properties.load(input);
            String value = properties.getProperty("particles", ParticleStatus.ALL.name());
            try {
                particleStatus = ParticleStatus.valueOf(value);
            } catch (IllegalArgumentException ignored) {
                particleStatus = ParticleStatus.ALL;
            }
        } catch (IOException exception) {
            System.err.println("[GhostBoost] Could not read config: " + exception.getMessage());
        }
    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("particles", particleStatus.name());
        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream output = Files.newOutputStream(FILE)) {
                properties.store(output, "GhostBoost settings");
            }
        } catch (IOException exception) {
            System.err.println("[GhostBoost] Could not save config: " + exception.getMessage());
        }
    }
}
