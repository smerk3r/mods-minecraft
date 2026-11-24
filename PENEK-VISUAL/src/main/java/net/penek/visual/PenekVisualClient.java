package net.penek.visual;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class PenekVisualClient implements ClientModInitializer {
    public static boolean hudEnabled = true;
    public static boolean coordsEnabled = true;
    public static boolean fpsEnabled = true;
    public static boolean watermarkEnabled = true;
    private KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.penekvisual.open_menu", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                client.setScreen(client.currentScreen instanceof PenekVisualScreen
                        ? null : new PenekVisualScreen());
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (!hudEnabled || client.options.hudHidden || client.player == null) return;
            renderHud(drawContext, client);
        });
    }

    private static void renderHud(DrawContext ctx, MinecraftClient client) {
        int x = 8, y = 8;
        if (watermarkEnabled) {
            ctx.fill(x, y, x + 112, y + 14, 0xD9161922);
            ctx.fill(x, y, x + 2, y + 14, 0xFF9B72FF);
            ctx.drawTextWithShadow(client.textRenderer, Text.literal("PENEK VISUAL"),
                    x + 7, y + 3, 0xFFB99CFF);
            y += 18;
        }
        if (fpsEnabled) {
            drawLine(ctx, client, "FPS: " + client.getCurrentFps(), x, y);
            y += 13;
        }
        if (coordsEnabled) {
            String coords = String.format("XYZ: %.1f / %.1f / %.1f",
                    client.player.getX(), client.player.getY(), client.player.getZ());
            drawLine(ctx, client, coords, x, y);
        }
    }

    private static void drawLine(DrawContext ctx, MinecraftClient client,
                                 String value, int x, int y) {
        int width = client.textRenderer.getWidth(value) + 12;
        ctx.fill(x, y, x + width, y + 12, 0xB9161922);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal(value),
                x + 6, y + 2, 0xFFE9E7F2);
    }
}
