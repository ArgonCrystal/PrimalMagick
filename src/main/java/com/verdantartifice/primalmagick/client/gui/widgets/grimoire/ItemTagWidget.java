package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Display widget for showing all the possible itemstacks for a given tag.  Used
 * on the requirements and recipe pages.
 * 
 * @author Daedalus4096
 */
public class ItemTagWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/grimoire.png");

    protected final ResourceLocation tag;
    protected final boolean isComplete;
    protected ItemStack toDisplay = ItemStack.EMPTY;
    
    public ItemTagWidget(ResourceLocation tag, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.tag = tag;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        TagKey<Item> itemTag = ItemTags.create(this.tag);
        List<Item> tagContents = new ArrayList<Item>();
        ForgeRegistries.ITEMS.tags().getTag(itemTag).forEach(i -> tagContents.add(i));
        if (tagContents != null && !tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            Item[] tagContentsArray = tagContents.toArray(new Item[tagContents.size()]);
            this.toDisplay = new ItemStack(tagContentsArray[index], 1);
            GuiUtils.renderItemStack(matrixStack, this.toDisplay, this.x, this.y, this.getMessage().getString(), false);
            if (this.isComplete) {
                // Render completion checkmark if appropriate
                matrixStack.pushPose();
                matrixStack.translate(this.x + 8, this.y, 200.0F);
                RenderSystem.setShaderTexture(0, GRIMOIRE_TEXTURE);
                this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
                matrixStack.popPose();
            }
        } else {
            this.toDisplay = ItemStack.EMPTY;
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
        // If hovered, show a tooltip with the display name of the current matching itemstack
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 200);
        
        GuiUtils.renderItemTooltip(matrixStack, this.toDisplay, mouseX, mouseY);
        
        matrixStack.popPose();
    }
}
