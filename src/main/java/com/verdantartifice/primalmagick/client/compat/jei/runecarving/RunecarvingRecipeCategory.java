package com.verdantartifice.primalmagick.client.compat.jei.runecarving;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.JeiHelper;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Recipe category for a runecarving recipe.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipeCategory extends RecipeCategoryPM<IRunecarvingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "runecarving_table");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/jei/runecarving_table.png");
    private static final ResourceLocation RESEARCH_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/item/grimoire.png");
    private static final int RESEARCH_X_OFFSET = 79;
    private static final int RESEARCH_Y_OFFSET = 19;

    private final IDrawableStatic researchIcon;

    public RunecarvingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.runecarving_table");
        this.researchIcon = guiHelper.drawableBuilder(RESEARCH_TEXTURE, 0, 0, 32, 32).setTextureSize(32, 32).build();
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 125, 36));
        this.setIcon(new ItemStack(ItemsPM.RUNECARVING_TABLE.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IRunecarvingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(IRunecarvingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        if (recipe.getRequiredResearch() != null && !recipe.getRequiredResearch().getKeys().isEmpty()) {
            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            this.researchIcon.draw(stack, RESEARCH_X_OFFSET * 2, RESEARCH_Y_OFFSET * 2);
            stack.popPose();
        }
    }

    @Override
    public List<Component> getTooltipStrings(IRunecarvingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        CompoundResearchKey compoundResearch = recipe.getRequiredResearch();
        if ( compoundResearch != null && !compoundResearch.getKeys().isEmpty() &&
             mouseX >= RESEARCH_X_OFFSET && mouseX < RESEARCH_X_OFFSET + this.researchIcon.getWidth() &&
             mouseY >= RESEARCH_Y_OFFSET && mouseY < RESEARCH_Y_OFFSET + this.researchIcon.getHeight() ) {
            return JeiHelper.getRequiredResearchTooltipStrings(compoundResearch);
        } else {
            return super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<IRunecarvingRecipe> getRecipeType() {
        return JeiRecipeTypesPM.RUNECARVING;
    }
}
