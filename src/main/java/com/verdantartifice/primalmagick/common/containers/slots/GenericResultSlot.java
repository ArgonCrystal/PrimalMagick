package com.verdantartifice.primalmagick.common.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Custom GUI slot for generic device outputs.
 * 
 * @author Daedalus4096
 */
public class GenericResultSlot extends Slot {
    protected final Player player;
    protected int removeCount = 0;
    
    public GenericResultSlot(Player player, Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }
    
    @Override
    public boolean mayPlace(ItemStack stack) {
        // Don't allow anything to be dropped into the slot
        return false;
    }
    
    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }
    
    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(thePlayer, stack);
    }
    
    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }
    
    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level, this.player, this.removeCount);
        this.removeCount = 0;
    }
}
