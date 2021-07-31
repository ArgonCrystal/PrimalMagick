package com.verdantartifice.primalmagic.client.gui;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarBasicContainer;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for basic runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class RunescribingAltarBasicScreen extends AbstractRunescribingAltarScreen<RunescribingAltarBasicContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_3.png");

    public RunescribingAltarBasicScreen(RunescribingAltarBasicContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
