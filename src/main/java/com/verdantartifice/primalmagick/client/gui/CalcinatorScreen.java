package com.verdantartifice.primalmagick.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.containers.CalcinatorContainer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the calcinator block.
 * 
 * @author Daedalus4096
 */
public class CalcinatorScreen extends AbstractContainerScreen<CalcinatorContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/calcinator.png");

    public CalcinatorScreen(CalcinatorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Animate burn indicator
        if (this.menu.isBurning()) {
            int burn = this.menu.getBurnLeftScaled();
            this.blit(matrixStack, this.leftPos + 34, this.topPos + 48 - burn, 176, 12 - burn, 14, burn + 1);
        }
        
        // Animate cook progress indicator
        int cook = this.menu.getCookProgressionScaled();
        this.blit(matrixStack, this.leftPos + 57, this.topPos + 34, 176, 14, cook + 1, 16);
    }
}
