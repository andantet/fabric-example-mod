package me.andante.example.datagen.mixin;

import com.mojang.bridge.game.GameVersion;
import me.andante.example.Example;
import net.minecraft.MinecraftVersion;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {
    @Inject(method = "getGameVersion", at = @At("HEAD"), cancellable = true)
    private static void fixNullGameVersion(CallbackInfoReturnable<GameVersion> cir) {
        if (Boolean.parseBoolean(System.getProperty(Example.MOD_ID + ".datagen"))) {
            cir.setReturnValue(MinecraftVersion.GAME_VERSION);
        }
    }
}
