package com.example.esp;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class EspClient implements ClientModInitializer {
    public static boolean enabled = true;
    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("esp", "main"));
        toggleKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.esp.toggle", InputConstants.Type.KEYSYM, InputConstants.KEY_V, category));

        // V druecken = ESP an/aus
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
            }
        });

        // Spielerliste oben rechts
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath("esp", "player_list"),
                (graphics, deltaTracker) -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (!enabled || mc.player == null || mc.level == null) return;

                    int screenW = mc.getWindow().getGuiScaledWidth();
                    int y = 5;
                    String title = "Spieler in der Nähe";
                    graphics.text(mc.font, title, screenW - mc.font.width(title) - 5, y, 0xFFFFFF55, true);
                    y += 12;

                    for (Player p : mc.level.players()) {
                        if (p == mc.player) continue;
                        String line = p.getName().getString() + " [" + Math.round(mc.player.distanceTo(p)) + "m]";
                        graphics.text(mc.font, line, screenW - mc.font.width(line) - 5, y, 0xFFFFFFFF, true);
                        y += 10;
                    }
                });
    }
}
