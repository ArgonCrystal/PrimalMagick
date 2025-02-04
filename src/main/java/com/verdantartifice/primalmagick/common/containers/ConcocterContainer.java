package com.verdantartifice.primalmagick.common.containers;

import com.verdantartifice.primalmagick.common.containers.slots.GenericResultSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandSlot;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/**
 * Server data container for the concocter GUI.
 * 
 * @author Daedalus4096
 */
public class ConcocterContainer extends AbstractArcaneRecipeBookMenu<Container> {
    protected final Container concocterInv;
    protected final ContainerData concocterData;
    protected final Level world;
    protected final Slot wandSlot;
    
    public ConcocterContainer(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(11), new SimpleContainerData(4));
    }
    
    public ConcocterContainer(int id, Inventory playerInv, Container concocterInv, ContainerData concocterData) {
        super(ContainersPM.CONCOCTER.get(), id);
        checkContainerSize(concocterInv, 11);
        checkContainerDataCount(concocterData, 4);
        this.concocterInv = concocterInv;
        this.concocterData = concocterData;
        this.world = playerInv.player.level;
        
        // Slot 0: Output slot
        this.addSlot(new GenericResultSlot(playerInv.player, this.concocterInv, 10, 138, 35) {
            @Override
            protected void checkTakeAchievements(ItemStack stack) {
                super.checkTakeAchievements(stack);
                StatsManager.incrementValue(this.player, StatsPM.CRAFTED_MAGITECH, stack.getCount());
            }
        });
        
        // Slots 1-9: Input slots
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.concocterInv, j + i * 3, 44 + j * 18, 17 + i * 18));
            }
        }
        
        // Slot 10: Wand slot
        this.wandSlot = this.addSlot(new WandSlot(this.concocterInv, 9, 8, 62, false));
        
        // Slots 11-37: Player backpack
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 38-46: Player hotbar
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
        
        this.addDataSlots(this.concocterData);
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return this.concocterInv.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 11 && index < 38) {
                // If transferring from the player's backpack, put wands in the wand slot and everything else into the input slots or hotbar, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 10, false) && !this.moveItemStackTo(slotStack, 38, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 38 && index < 47) {
                // If transferring from the player's hotbar, put wands in the wand slot and everything else into the input slots or backpack, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 10, false) && !this.moveItemStackTo(slotStack, 11, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 11, 47, false)) {
                // Move all other transfers into the backpack or hotbar
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(playerIn, slotStack);
        }
        return stack;
    }

    public int getCookProgressionScaled() {
        // Determine how much of the cook arrow to show
        int i = this.concocterData.get(0);
        int j = this.concocterData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana() {
        return this.concocterData.get(2);
    }
    
    public int getMaxMana() {
        return this.concocterData.get(3);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
        if (this.concocterInv instanceof StackedContentsCompatible stackedContainer) {
            stackedContainer.fillStackedContents(stackedContents);
        }
    }

    @Override
    public void clearCraftingContent() {
        for (int index = 0; index <= 9; index++) {
            this.getSlot(index).set(ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(Recipe<? super Container> recipe) {
        return recipe.matches(this.concocterInv, this.world);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 3;
    }

    @Override
    public int getGridHeight() {
        return 3;
    }

    @Override
    public int getSize() {
        return 10;
    }

    @Override
    public ArcaneRecipeBookType getRecipeBookType() {
        return ArcaneRecipeBookType.CONCOCTER;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }
}
