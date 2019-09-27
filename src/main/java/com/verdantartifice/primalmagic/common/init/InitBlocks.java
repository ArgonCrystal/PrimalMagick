package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.blocks.base.BlockPM;
import com.verdantartifice.primalmagic.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlocks {
    public static void initBlocks(IForgeRegistry<Block> registry) {
        registry.register(new BlockPM("marble_raw", Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F)));
        registry.register(new BlockPM("marble_enchanted", Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F)));
        registry.register(new ArcaneWorkbenchBlock());
        registry.register(new AncientManaFontBlock(Source.EARTH));
        registry.register(new AncientManaFontBlock(Source.SEA));
        registry.register(new AncientManaFontBlock(Source.SKY));
        registry.register(new AncientManaFontBlock(Source.SUN));
        registry.register(new AncientManaFontBlock(Source.MOON));
    }
}
