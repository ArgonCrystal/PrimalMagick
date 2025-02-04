package com.verdantartifice.primalmagick.client.util;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneRecipeUpdateListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

/**
 * Collection of utility methods for accessing client-side data.
 * 
 * @author Daedalus4096
 */
public class ClientUtils {
    /**
     * Gets the current client-side player.
     * 
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return the current client-side player
     */
    @Nullable
    public static Player getCurrentPlayer() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player;
    }

    /**
     * Gets the current client-side level.
     * 
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return the current client-side level
     */
    @Nullable
    public static Level getCurrentLevel() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level;
    }
    
    /**
     * Gets whether the player has the shift key held down.
     * 
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return whether the shift key is down
     */
    public static boolean hasShiftDown() {
        return Screen.hasShiftDown();
    }
    
    /**
     * Places a ghost recipe into the screen of the given IDed menu.
     * 
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @param containerId the ID of the menu whose screen to update
     * @param recipeId the ID of the recipe to be placed
     */
    public static void handlePlaceGhostRecipe(int containerId, ResourceLocation recipeId) {
        // TODO Find a better home for this method
        Minecraft mc = Minecraft.getInstance();
        AbstractContainerMenu menu = mc.player.containerMenu;
        if (menu.containerId == containerId) {
            mc.level.getRecipeManager().byKey(recipeId).ifPresent(recipe -> {
                if (mc.screen instanceof ArcaneRecipeUpdateListener listener) {
                    listener.getRecipeBookComponent().setupGhostRecipe(recipe, menu.slots);
                }
            });
        }
    }
}
