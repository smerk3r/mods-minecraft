package ru.smerker.visuals;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ParticleStatus;
import org.lwjgl.glfw.GLFW;

public final class SmerkerVisualsClient implements ClientModInitializer {
    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        SmerkerConfig.load();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.smerkervisuals.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new SmerkerVisualsScreen());
                }
            }
        });

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("smerkervisuals", "main_hud"),
                SmerkerVisualsClient::renderHud
        );

        applyParticleSetting();
        System.out.println("[SmerkerVisuals] Loaded. Press Right Shift to open the menu.");
    }

    public static void applyParticleSetting() {
        Minecraft client = Minecraft.getInstance();
        client.options.particles().set(
                SmerkerConfig.minimalParticles ? ParticleStatus.MINIMAL : ParticleStatus.ALL
        );
    }

    private static void renderHud(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null || client.screen != null) {
            return;
        }

        int y = 8;
        if (SmerkerConfig.fpsHud) {
            y = drawPanel(graphics, "SMERKER  |  " + client.getFps() + " FPS", 8, y, 0xFFBFA7FF);
        }

        if (SmerkerConfig.coordinatesHud) {
            String coords = String.format(
                    "XYZ  %d  %d  %d",
                    client.player.blockPosition().getX(),
                    client.player.blockPosition().getY(),
                    client.player.blockPosition().getZ()
            );
            y = drawPanel(graphics, coords, 8, y, 0xFFFFFFFF);
        }

        if (SmerkerConfig.keystrokesHud) {
            drawKeystrokes(graphics, client, 8, y + 5);
        }
    }

    private static int drawPanel(GuiGraphics graphics, String text, int x, int y, int color) {
        Minecraft client = Minecraft.getInstance();
        int width = client.font.width(text);
        graphics.fill(x - 4, y - 3, x + width + 5, y + 13, 0xB8101422);
        graphics.fill(x - 4, y - 3, x - 2, y + 13, 0xFF9B7BFF);
        graphics.drawString(client.font, Component.literal(text), x, y, color, true);
        return y + 20;
    }

    private static void drawKeystrokes(GuiGraphics graphics, Minecraft client, int x, int y) {
        drawKey(graphics, client, "W", x + 22, y, client.options.keyUp.isDown());
        drawKey(graphics, client, "A", x, y + 20, client.options.keyLeft.isDown());
        drawKey(graphics, client, "S", x + 22, y + 20, client.options.keyDown.isDown());
        drawKey(graphics, client, "D", x + 44, y + 20, client.options.keyRight.isDown());
        drawKey(graphics, client, "JUMP", x, y + 40, client.options.keyJump.isDown());
    }

    private static void drawKey(GuiGraphics graphics, Minecraft client,
                                String label, int x, int y, boolean pressed) {
        int background = pressed ? 0xFF8064D8 : 0xB8101422;
        graphics.fill(x, y, x + 20, y + 18, background);
        int textWidth = client.font.width(label);
        graphics.drawString(client.font, label, x + (20 - textWidth) / 2, y + 5, 0xFFFFFFFF, true);
    }
}
