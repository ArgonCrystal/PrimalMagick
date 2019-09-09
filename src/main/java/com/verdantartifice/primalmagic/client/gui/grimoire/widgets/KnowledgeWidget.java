package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.research.Knowledge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KnowledgeWidget extends Widget {
    protected Knowledge knowledge;
    
    public KnowledgeWidget(Knowledge knowledge, int x, int y) {
        super(x, y, 16, 16, "");
        this.knowledge = knowledge;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        
        // Draw knowledge type icon
        mc.getTextureManager().bindTexture(this.knowledge.getType().getIconLocation());
        GlStateManager.translatef(this.x, this.y, 0.0F);
        GlStateManager.scaled(0.0625D, 0.0625D, 0.0625D);
        this.blit(0, 0, 0, 0, 255, 255);
        
        // Draw discipline icon
        mc.getTextureManager().bindTexture(this.knowledge.getDiscipline().getIconLocation());
        GlStateManager.pushMatrix();
        GlStateManager.translatef(32.0F, 32.0F, 1.0F);
        GlStateManager.scaled(0.75D, 0.75D, 0.75D);
        this.blit(0, 0, 0, 0, 255, 255);
        GlStateManager.popMatrix();
        
        GlStateManager.popMatrix();
        
        // Draw amount str
        ITextComponent amountText = new StringTextComponent(Integer.toString(this.knowledge.getAmount()));
        // TODO apply red color if not complete
        int width = mc.fontRenderer.getStringWidth(amountText.getFormattedText());
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.x + 16 - width / 2, this.y + 12, 5.0F);
        GlStateManager.scaled(0.5D, 0.5D, 0.5D);
        mc.fontRenderer.drawStringWithShadow(amountText.getFormattedText(), 0.0F, 0.0F, Color.WHITE.getRGB());
        GlStateManager.popMatrix();
        
        if (this.isHovered()) {
            // Render tooltip
            ITextComponent knowledgeText = new TranslationTextComponent(this.knowledge.getType().getNameTranslationKey());
            ITextComponent disciplineText = new TranslationTextComponent(this.knowledge.getDiscipline().getNameTranslationKey());
            ITextComponent labelText = new TranslationTextComponent("primalmagic.knowledge_type.label", knowledgeText, disciplineText);
            GuiUtils.renderCustomTooltip(Collections.singletonList(labelText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
