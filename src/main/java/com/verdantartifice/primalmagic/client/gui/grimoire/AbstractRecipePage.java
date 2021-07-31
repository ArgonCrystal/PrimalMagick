package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base class for all grimoire recipe pages.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractRecipePage extends AbstractPage {
    protected static final ResourceLocation OVERLAY = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire_overlay.png");
    
    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
    }

    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }
}
