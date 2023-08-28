package dev.shadowsoffire.apotheosis.mixin.client;

import dev.shadowsoffire.apotheosis.Apotheosis;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {

    @ModifyConstant(method = "renderLabels(Lnet/minecraft/client/gui/GuiGraphics;II)V", constant = @Constant(intValue = 40))
    public int apoth_removeLevelCap(int old) {
        if (Apotheosis.enableEnch) return Integer.MAX_VALUE;
        return old;
    }

}