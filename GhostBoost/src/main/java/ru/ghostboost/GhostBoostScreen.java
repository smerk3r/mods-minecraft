package ru.ghostboost;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class GhostBoostScreen extends Screen {
    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 258;
    private final Minecraft client = Minecraft.getInstance();

    public GhostBoostScreen() {
        super(Component.literal("GhostBoost"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - PANEL_WIDTH / 2;
        int top = this.height / 2 - PANEL_HEIGHT / 2;

        addRenderableWidget(Button.builder(Component.literal("FPS BOOST"), button -> {
            client.options.renderDistance().set(8);
            GhostBoostClient.setParticles(ParticleStatus.MINIMAL);
            GhostBoostClient.notify("FPS Boost preset enabled");
        }).bounds(left + 16, top + 105, 138, 22).build());

        addRenderableWidget(Button.builder(Component.literal("BALANCED"), button -> {
            client.options.renderDistance().set(12);
            GhostBoostClient.setParticles(ParticleStatus.DECREASED);
            GhostBoostClient.notify("Balanced preset enabled");
        }).bounds(left + 166, top + 105, 138, 22).build());

        addRenderableWidget(Button.builder(particleLabel(), button -> {
            ParticleStatus current = client.options.particles().get();
            ParticleStatus next = current == ParticleStatus.MINIMAL
                    ? ParticleStatus.ALL : ParticleStatus.MINIMAL;
            GhostBoostClient.setParticles(next);
            button.setMessage(particleLabel());
        }).bounds(left + 16, top + 137, 288, 21).build());

        addRenderableWidget(Button.builder(Component.literal("−"), button -> {
            int distance = client.options.renderDistance().get();
            client.options.renderDistance().set(Math.max(2, distance - 2));
        }).bounds(left + 16, top + 168, 48, 21).build());

        addRenderableWidget(Button.builder(Component.literal("Reset distance"), button ->
                client.options.renderDistance().set(12)
        ).bounds(left + 74, top + 168, 172, 21).build());

        addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            int distance = client.options.renderDistance().get();
            client.options.renderDistance().set(Math.min(32, distance + 2));
        }).bounds(left + 256, top + 168, 48, 21).build());

        addRenderableWidget(Button.builder(Component.literal("SAVE SETTINGS"), button -> {
            GhostBoostConfig.save();
            button.setMessage(Component.literal("SAVED"));
        }).bounds(left + 16, top + 199, 138, 22).build());

        addRenderableWidget(Button.builder(Component.literal("CLOSE"), button -> onClose())
                .bounds(left + 166, top + 199, 138, 22).build());
    }

    private Component particleLabel() {
        ParticleStatus current = client.options.particles().get();
        String label = current == ParticleStatus.MINIMAL ? "MINIMAL" :
                current == ParticleStatus.DECREASED ? "DECREASED" : "ALL";
        return Component.literal("PARTICLES  •  " + label);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);

        int left = this.width / 2 - PANEL_WIDTH / 2;
        int top = this.height / 2 - PANEL_HEIGHT / 2;
        int right = left + PANEL_WIDTH;
        int bottom = top + PANEL_HEIGHT;

        long time = System.currentTimeMillis();
        int pulse = (int) (Math.sin(time / 420.0) * 18.0);
        int accent = 0xFF000000 | ((155 + pulse) << 16) | ((105 + pulse) << 8) | 255;

        graphics.fill(left - 2, top - 2, right + 2, bottom + 2, 0xFF080611);
        graphics.fill(left, top, right, bottom, 0xF0121020);
        graphics.fill(left, top, right, top + 3, accent);
        graphics.fill(left, top + 3, left + 3, bottom, 0xFF7544D8);
        graphics.fill(right - 3, top + 3, right, bottom, 0xFF422477);

        graphics.drawString(this.font, "GHOST", left + 17, top + 15, 0xFFFFFFFF, true);
        graphics.drawString(this.font, "BOOST", left + 17 + this.font.width("GHOST") + 5,
                top + 15, 0xFFB99AFF, true);
        graphics.drawString(this.font, "PERFORMANCE CONTROL", left + 17, top + 31,
                0xFF9D91B5, false);
        graphics.drawString(this.font, "OWNER  smerk3r", right - 112, top + 17,
                0xFFD5C4FF, false);

        graphics.fill(left + 15, top + 52, right - 15, top + 91, 0xB51D1830);
        graphics.fill(left + 15, top + 52, left + 18, top + 91, 0xFF9B70FF);
        graphics.drawString(this.font, "LIVE STATUS", left + 26, top + 59,
                0xFF9E8BBE, false);
        graphics.drawString(this.font, client.getFps() + " FPS", left + 26, top + 72,
                0xFFFFFFFF, true);
        String distance = client.options.renderDistance().get() + " CHUNKS";
        graphics.drawString(this.font, "RENDER DISTANCE", left + 150, top + 59,
                0xFF9E8BBE, false);
        graphics.drawString(this.font, distance, left + 150, top + 72,
                0xFFDFD2FF, true);

        graphics.drawString(this.font, "OPTIMIZATION PRESETS", left + 17, top + 94,
                0xFFD8CCF4, true);
        graphics.drawString(this.font, "Choose a preset or tune settings below",
                left + 17, top + 235, 0xFF817A95, false);

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
