package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.LevelAccessor;

/**
 * Block definition for moonwood pillars.  They are decorative blocks that fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodPillarBlock extends AbstractPhasingPillarBlock {
    public MoonwoodPillarBlock() {
        super(Block.Properties.of(Material.WOOD, MaterialColor.METAL).strength(2.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
