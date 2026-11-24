package ru.ghostboost;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class GhostBoostClient implements ClientModInitializer {
    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        GhostBoostConfig.load();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.ghostboost.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new GhostBoostScreen());
                }
            }
        });

        Minecraft.getInstance().options.particles().set(GhostBoostConfig.particleStatus);
        System.out.println("[GhostBoost] Loaded. Press Right Shift to open the menu.");
    }

    public static void notify(String message) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.player.displayClientMessage(
                    Component.literal("§bGhostBoost §8» §f" + message), true
            );
        }
    }

    public static void setParticles(net.minecraft.server.level.ParticleStatus status) {
        GhostBoostConfig.particleStatus = status;
        Minecraft.getInstance().options.particles().set(status);
        GhostBoostConfig.save();
    }
}
