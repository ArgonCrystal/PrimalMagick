package com.verdantartifice.primalmagic.common.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Wand transformation that turns a block into something else.
 * 
 * @author Daedalus4096
 */
public class WandTransformBlock extends AbstractWandTransform {
    protected final Block target;
    
    public WandTransformBlock(@Nonnull Block target, @Nonnull ItemStack result, @Nullable CompoundResearchKey research) {
        super(result, research);
        this.target = target;
    }

    @Override
    public boolean isValid(Level world, Player player, BlockPos pos) {
        // The expected block type must be at the given world position and the given player must know the right research
        return super.isValid(world, player, pos) && world.getBlockState(pos).getBlock() == this.target;
    }
}
