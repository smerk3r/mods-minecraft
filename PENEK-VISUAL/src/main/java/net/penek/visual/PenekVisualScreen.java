package net.penek.visual;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class PenekVisualScreen extends Screen {
    private static final int PURPLE = 0xFF9B72FF;
    private static final int PANEL = 0xF0141720;
    private static final int TILE = 0xFF202431;
    private String category = "HUD";

    public PenekVisualScreen() { super(Text.literal("PENEK VISUAL")); }

    @Override
    protected void init() {
        clearChildren();
        int left = width / 2 - 190;
        int top = height / 2 - 112;

        addDrawableChild(ButtonWidget.builder(Text.literal("HUD"), b -> setCategory("HUD"))
                .dimensions(left + 14, top + 48, 76, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("VISUALS"), b -> setCategory("VISUALS"))
                .dimensions(left + 14, top + 76, 76, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("INTERFACE"), b -> setCategory("INTERFACE"))
                .dimensions(left + 14, top + 104, 76, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("SETTINGS"), b -> setCategory("SETTINGS"))
                .dimensions(left + 14, top + 132, 76, 22).build());

        addDrawableChild(ButtonWidget.builder(toggle("HUD", PenekVisualClient.hudEnabled),
                b -> { PenekVisualClient.hudEnabled = !PenekVisualClient.hudEnabled; refresh(); })
                .dimensions(left + 120, top + 62, 220, 24).build());
        addDrawableChild(ButtonWidget.builder(toggle("Watermark", PenekVisualClient.watermarkEnabled),
                b -> { PenekVisualClient.watermarkEnabled = !PenekVisualClient.watermarkEnabled; refresh(); })
                .dimensions(left + 120, top + 94, 220, 24).build());
        addDrawableChild(ButtonWidget.builder(toggle("FPS Counter", PenekVisualClient.fpsEnabled),
                b -> { PenekVisualClient.fpsEnabled = !PenekVisualClient.fpsEnabled; refresh(); })
                .dimensions(left + 120, top + 126, 220, 24).build());
        addDrawableChild(ButtonWidget.builder(toggle("Coordinates", PenekVisualClient.coordsEnabled),
                b -> { PenekVisualClient.coordsEnabled = !PenekVisualClient.coordsEnabled; refresh(); })
                .dimensions(left + 120, top + 158, 220, 24).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("CLOSE"), b -> close())
                .dimensions(left + 264, top + 194, 76, 22).build());
    }

    private Text toggle(String name, boolean enabled) {
        return Text.literal(name + "  " + (enabled ? "§aON" : "§cOFF"));
    }
    private void setCategory(String value) { category = value; refresh(); }
    private void refresh() { clearAndInit(); }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        int left = width / 2 - 190, top = height / 2 - 112;
        ctx.fill(left, top, left + 380, top + 230, PANEL);
        ctx.fill(left, top, left + 380, top + 2, PURPLE);
        ctx.fill(left + 8, top + 38, left + 100, top + 182, TILE);
        ctx.drawTextWithShadow(textRenderer, Text.literal("PENEK VISUAL"),
                left + 14, top + 14, 0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer, Text.literal("1.21.11 • FABRIC"),
                left + 250, top + 16, 0xFFAAA6B8);
        ctx.drawTextWithShadow(textRenderer, Text.literal(category),
                left + 120, top + 42, PURPLE);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Right Shift • Toggle menu"),
                left + 14, top + 210, 0xFF888B9A);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
