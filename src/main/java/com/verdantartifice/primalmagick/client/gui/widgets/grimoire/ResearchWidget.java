package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for showing a specific required research entry on the requirements page.
 * 
 * @author Daedalus4096
 */
public class ResearchWidget extends AbstractWidget {
    protected static final ResourceLocation BAG_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/research/research_bag.png");
    protected static final ResourceLocation TUBE_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/research/research_tube.png");
    protected static final ResourceLocation MAP_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/research/research_map.png");
    protected static final ResourceLocation UNKNOWN_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/research/research_unknown.png");
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/grimoire.png");
    
    protected final SimpleResearchKey key;
    protected final boolean isComplete;
    protected final boolean hasHint;
    
    public ResearchWidget(SimpleResearchKey key, int x, int y, boolean isComplete) {
        this(key, x, y, isComplete, false);
    }
    
    public ResearchWidget(SimpleResearchKey key, int x, int y, boolean isComplete, boolean hasHint) {
        super(x, y, 16, 16, Component.empty());
        this.key = key;
        this.isComplete = isComplete;
        this.hasHint = hasHint;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Pick the icon to show based on the prefix of the research key
        ResourceLocation loc;
        if (this.key.getRootKey().startsWith("m_")) {
            loc = MAP_TEXTURE;
        } else if (this.key.getRootKey().startsWith("b_")) {
            loc = BAG_TEXTURE;
        } else if (this.key.getRootKey().startsWith("t_")) {
            loc = TUBE_TEXTURE;
        } else {
            loc = UNKNOWN_TEXTURE;
        }
        
        // Render the icon
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderTexture(0, loc);
        matrixStack.translate(this.x, this.y, 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        matrixStack.popPose();
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            matrixStack.pushPose();
            matrixStack.translate(this.x + 8, this.y, 100.0F);
            RenderSystem.setShaderTexture(0, GRIMOIRE_TEXTURE);
            this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
            matrixStack.popPose();
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        // Render tooltip
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 200);
        
        List<Component> textLines;
        if (this.hasHint) {
            if (Screen.hasShiftDown()) {
                Component hintText = Component.translatable("primalmagick.research." + this.key.getRootKey() + ".hint");
                textLines = Collections.singletonList(hintText);
            } else {
                Component baseText = Component.translatable("primalmagick.research." + this.key.getRootKey() + ".text");
                Component shiftText = Component.translatable("tooltip.primalmagick.more_info").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                textLines = Arrays.asList(baseText, shiftText);
            }
        } else {
            Component baseText = Component.translatable("primalmagick.research." + this.key.getRootKey() + ".text");
            textLines = Collections.singletonList(baseText);
        }
        GuiUtils.renderCustomTooltip(matrixStack, textLines, mouseX, mouseY);
        
        matrixStack.popPose();
    }
}
