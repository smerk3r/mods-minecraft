package ru.smerker.visuals;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SmerkerConfig {
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("smerkervisuals.properties");

    public static boolean fpsHud = true;
    public static boolean coordinatesHud = true;
    public static boolean keystrokesHud = false;
    public static boolean minimalParticles = false;

    private SmerkerConfig() {}

    public static void load() {
        Properties properties = new Properties();
        if (Files.exists(FILE)) {
            try (InputStream input = Files.newInputStream(FILE)) {
                properties.load(input);
                fpsHud = Boolean.parseBoolean(properties.getProperty("fpsHud", "true"));
                coordinatesHud = Boolean.parseBoolean(properties.getProperty("coordinatesHud", "true"));
                keystrokesHud = Boolean.parseBoolean(properties.getProperty("keystrokesHud", "false"));
                minimalParticles = Boolean.parseBoolean(properties.getProperty("minimalParticles", "false"));
            } catch (IOException exception) {
                System.err.println("[SmerkerVisuals] Could not read config: " + exception.getMessage());
            }
        }
    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("fpsHud", Boolean.toString(fpsHud));
        properties.setProperty("coordinatesHud", Boolean.toString(coordinatesHud));
        properties.setProperty("keystrokesHud", Boolean.toString(keystrokesHud));
        properties.setProperty("minimalParticles", Boolean.toString(minimalParticles));

        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream output = Files.newOutputStream(FILE)) {
                properties.store(output, "SmerkerVisuals settings");
            }
        } catch (IOException exception) {
            System.err.println("[SmerkerVisuals] Could not save config: " + exception.getMessage());
        }
    }
}
