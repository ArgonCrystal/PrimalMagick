package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class GrimoireBackButton extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    protected GrimoireScreen screen;
    
    public GrimoireBackButton(int widthIn, int heightIn, GrimoireScreen screen) {
        super(widthIn, heightIn, 20, 12, "", new Handler());
        this.screen = screen;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO bob scale when hovered
        this.blit(this.x, this.y, 40, 204, this.width, this.height);
    }

    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof GrimoireBackButton) {
                GrimoireBackButton gbb = (GrimoireBackButton)button;
                gbb.getScreen().goBack();
            }
        }
    }
}
