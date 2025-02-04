package com.verdantartifice.primalmagick.client.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.containers.AbstractRunescribingAltarContainer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Base GUI screen for runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRunescribingAltarScreen<T extends AbstractRunescribingAltarContainer> extends AbstractContainerScreen<T> {
    public AbstractRunescribingAltarScreen(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Nonnull
    protected abstract ResourceLocation getTextureLocation();

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Render background texture
        RenderSystem.setShaderTexture(0, this.getTextureLocation());
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
