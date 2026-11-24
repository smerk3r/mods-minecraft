package ru.ghostboost;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class GhostBoostScreen extends Screen {
    private final Minecraft client = Minecraft.getInstance();

    public GhostBoostScreen() {
        super(Component.literal("GhostBoost"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int top = this.height / 2 - 65;

        addRenderableWidget(Button.builder(
                Component.literal("Fast preset"),
                button -> {
                    client.options.particles().set(ParticleStatus.MINIMAL);
                    client.options.renderDistance().set(8);
                    GhostBoostClient.notify("Fast preset enabled");
                }
        ).bounds(center - 100, top, 200, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Balanced preset"),
                button -> {
                    client.options.particles().set(ParticleStatus.DECREASED);
                    client.options.renderDistance().set(12);
                    GhostBoostClient.notify("Balanced preset enabled");
                }
        ).bounds(center - 100, top + 27, 200, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Toggle particles: minimal / all"),
                button -> {
                    ParticleStatus current = client.options.particles().get();
                    ParticleStatus next = current == ParticleStatus.MINIMAL
                            ? ParticleStatus.ALL : ParticleStatus.MINIMAL;
                    client.options.particles().set(next);
                    button.setMessage(Component.literal(
                            next == ParticleStatus.MINIMAL
                                    ? "Particles: minimal" : "Particles: all"
                    ));
                }
        ).bounds(center - 100, top + 54, 200, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Close"),
                button -> onClose()
        ).bounds(center - 100, top + 85, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);

        int center = this.width / 2;
        graphics.drawCenteredString(this.font, "GHOSTBOOST", center,
                this.height / 2 - 105, 0xFFB9A7FF);
        graphics.drawCenteredString(this.font, "Performance settings", center,
                this.height / 2 - 90, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font, "Right Shift: open menu", center,
                this.height / 2 + 115, 0xFFAAAAAA);

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
