package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AttunementMeterWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AttunementThresholdWidget;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.resources.ResourceLocation;

/**
 * Grimoire page showing the details of a discovered attunement.
 * 
 * @author Daedalus4096
 */
public class AttunementPage extends AbstractPage {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/attunement_meter.png");

    protected Source source;
    protected List<IPageElement> contents = new ArrayList<>();
    protected boolean firstPage;

    public AttunementPage(@Nonnull Source source) {
        this(source, false);
    }
    
    public AttunementPage(@Nonnull Source source, boolean first) {
        this.source = source;
        this.firstPage = first;
    }
    
    @Nonnull
    public List<IPageElement> getElements() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addElement(IPageElement element) {
        return this.contents.add(element);
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
            y += 53;
        } else {
            y += 25;
        }
        
        if (this.isFirstPage()) {
            // Render attunement meter
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderTexture(0, TEXTURE);
            
            // Render meter background
            this.blit(matrixStack, x + 51 + (side * 140), y, 12, 0, 16, 120);
        }

        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(matrixStack, side, x, y);
            y = content.getNextY(y);
        }
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.source.getNameTranslationKey();
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        if (this.isFirstPage()) {
            screen.addWidgetToScreen(new AttunementMeterWidget(this.source, x + 68 + (side * 140), y + 17));
            screen.addWidgetToScreen(new AttunementThresholdWidget(this.source, AttunementThreshold.MINOR, x + 83 + (side * 140), y + 79));
            screen.addWidgetToScreen(new AttunementThresholdWidget(this.source, AttunementThreshold.LESSER, x + 83 + (side * 140), y + 49));
            screen.addWidgetToScreen(new AttunementThresholdWidget(this.source, AttunementThreshold.GREATER, x + 83 + (side * 140), y + 19));
        }
    }
}
