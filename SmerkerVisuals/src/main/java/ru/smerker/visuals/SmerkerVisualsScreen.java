package ru.smerker.visuals;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SmerkerVisualsScreen extends Screen {
    public SmerkerVisualsScreen() {
        super(Component.literal("SmerkerVisuals"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int left = center - 104;
        int top = this.height / 2 - 58;

        addRenderableWidget(toggleButton("FPS HUD", SmerkerConfig.fpsHud,
                state -> SmerkerConfig.fpsHud = state).bounds(left, top, 208, 20).build());

        addRenderableWidget(toggleButton("Coordinates HUD", SmerkerConfig.coordinatesHud,
                state -> SmerkerConfig.coordinatesHud = state).bounds(left, top + 25, 208, 20).build());

        addRenderableWidget(toggleButton("Keystrokes", SmerkerConfig.keystrokesHud,
                state -> SmerkerConfig.keystrokesHud = state).bounds(left, top + 50, 208, 20).build());

        addRenderableWidget(toggleButton("Minimal particles", SmerkerConfig.minimalParticles,
                state -> {
                    SmerkerConfig.minimalParticles = state;
                    SmerkerVisualsClient.applyParticleSetting();
                }).bounds(left, top + 75, 208, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Save settings"), button -> {
            SmerkerConfig.save();
            button.setMessage(Component.literal("Saved!"));
        }).bounds(left, top + 105, 101, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Close"), button -> onClose())
                .bounds(left + 107, top + 105, 101, 20).build());
    }

    private Button.Builder toggleButton(String title, boolean initial,
                                        java.util.function.Consumer<Boolean> setter) {
        return Button.builder(toggleLabel(title, initial), button -> {
            boolean enabled = button.getMessage().getString().endsWith("ON");
            boolean next = !enabled;
            setter.accept(next);
            SmerkerConfig.save();
            button.setMessage(toggleLabel(title, next));
        });
    }

    private Component toggleLabel(String title, boolean enabled) {
        return Component.literal(title + "  " + (enabled ? "ON" : "OFF"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);

        int center = this.width / 2;
        graphics.fill(center - 122, this.height / 2 - 91,
                center + 122, this.height / 2 + 83, 0xE8101422);
        graphics.fill(center - 122, this.height / 2 - 91,
                center + 122, this.height / 2 - 88, 0xFF9B7BFF);

        graphics.drawCenteredString(this.font, "SMERKERVISUALS", center,
                this.height / 2 - 80, 0xFFBFA7FF);
        graphics.drawCenteredString(this.font, "Visual modules", center,
                this.height / 2 - 66, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font, "Owner: smerk3r", center,
                this.height / 2 + 68, 0xFFBFA7FF);

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
