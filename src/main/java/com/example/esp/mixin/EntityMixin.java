package com.example.esp.mixin;

import com.example.esp.EspClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    // Laesst andere Spieler clientseitig "leuchten" -> Umriss ist durch Waende sichtbar
    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void esp$glow(CallbackInfoReturnable<Boolean> cir) {
        if (EspClient.enabled
                && (Object) this instanceof Player p
                && p != Minecraft.getInstance().player) {
            cir.setReturnValue(true);
        }
    }
}
