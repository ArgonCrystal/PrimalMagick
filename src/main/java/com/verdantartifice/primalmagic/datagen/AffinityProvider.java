package com.verdantartifice.primalmagic.datagen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;

public class AffinityProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;
    
    public AffinityProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, IFinishedAffinity> map = new HashMap<>();
        this.registerAffinities((affinity) -> {
            if (map.put(affinity.getId(), affinity) != null) {
                PrimalMagic.LOGGER.debug("Duplicate affinity in data generation: " + affinity.getId().toString());
            }
        });
        for (Map.Entry<ResourceLocation, IFinishedAffinity> entry : map.entrySet()) {
            IFinishedAffinity affinity = entry.getValue();
            this.saveAffinity(cache, affinity.getAffinityJson(), path.resolve("data/" + entry.getKey().getNamespace() + "/affinities/" + entry.getKey().getPath() + ".json"));
        }
    }
    
    private void saveAffinity(DirectoryCache cache, JsonObject json, Path path) {
        try {
            String jsonStr = GSON.toJson((JsonElement)json);
            String hash = HASH_FUNCTION.hashUnencodedChars(jsonStr).toString();
            if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(jsonStr);
                }
            }
            cache.recordHash(path, hash);
        } catch (IOException e) {
            PrimalMagic.LOGGER.error("Couldn't save affinity {}", path, e);
        }
    }
    
    protected void registerAffinities(Consumer<IFinishedAffinity> consumer) {
        SourceList auraUnit = new SourceList().add(Source.EARTH, 1).add(Source.SEA, 1).add(Source.SKY, 1).add(Source.SUN, 1).add(Source.MOON, 1);
        
        // Define vanilla item affinities
        ItemAffinityBuilder.itemAffinity(Items.STONE).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRANITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_GRANITE).base(Items.GRANITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIORITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_DIORITE).base(Items.DIORITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ANDESITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_ANDESITE).base(Items.ANDESITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRASS_BLOCK).base(Items.DIRT).add(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIRT).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COARSE_DIRT).base(Items.DIRT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PODZOL).base(Items.DIRT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_NYLIUM).base(Items.NETHERRACK).add(Source.MOON, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_NYLIUM).base(Items.NETHERRACK).add(Source.MOON, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COBBLESTONE).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_PLANKS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_PLANKS).base(Items.OAK_PLANKS).add(Source.MOON, 2).add(Source.INFERNAL, 2).remove(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_PLANKS).base(Items.CRIMSON_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_SAPLING).set(Source.EARTH, 10).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BEDROCK).set(Source.EARTH, 20).set(Source.VOID, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SAND).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_SAND).base(Items.SAND).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAVEL).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GOLD_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.IRON_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COAL_ORE).base(Items.STONE).add(Source.EARTH, 5).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_GOLD_ORE).base(Items.NETHERRACK).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_LOG).set(Source.EARTH, 10).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_STEM).base(Items.OAK_LOG).add(Source.MOON, 10).add(Source.INFERNAL, 10).remove(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_STEM).base(Items.CRIMSON_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_OAK_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_SPRUCE_LOG).base(Items.SPRUCE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_BIRCH_LOG).base(Items.BIRCH_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_JUNGLE_LOG).base(Items.JUNGLE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_ACACIA_LOG).base(Items.ACACIA_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_DARK_OAK_LOG).base(Items.DARK_OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_CRIMSON_STEM).base(Items.CRIMSON_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_WARPED_STEM).base(Items.WARPED_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_OAK_WOOD).base(Items.OAK_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_SPRUCE_WOOD).base(Items.SPRUCE_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_BIRCH_WOOD).base(Items.BIRCH_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_JUNGLE_WOOD).base(Items.JUNGLE_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_ACACIA_WOOD).base(Items.ACACIA_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_DARK_OAK_WOOD).base(Items.DARK_OAK_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_CRIMSON_HYPHAE).base(Items.CRIMSON_HYPHAE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_WARPED_HYPHAE).base(Items.WARPED_HYPHAE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_WOOD).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_WOOD).base(Items.SPRUCE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_WOOD).base(Items.BIRCH_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_WOOD).base(Items.JUNGLE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_WOOD).base(Items.ACACIA_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_WOOD).base(Items.DARK_OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_HYPHAE).base(Items.CRIMSON_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_HYPHAE).base(Items.WARPED_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_LEAVES).set(Source.EARTH, 5).set(Source.SKY, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPONGE).set(Source.SEA, 10).set(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WET_SPONGE).base(Items.SPONGE).add(Source.SEA, 10).remove(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LAPIS_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SANDSTONE).base(Items.STONE).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COBWEB).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRASS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FERN).base(Items.GRASS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BUSH).base(Items.GRASS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SEAGRASS).base(Items.GRASS).add(Source.SEA, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SEA_PICKLE).set(Source.EARTH, 5).set(Source.SEA, 5).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_WOOL).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MAGENTA_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_BLUE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.YELLOW_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIME_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAY_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_GRAY_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CYAN_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPLE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GREEN_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLACK_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DANDELION).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POPPY).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_ORCHID).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ALLIUM).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.AZURE_BLUET).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OXEYE_DAISY).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CORNFLOWER).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LILY_OF_THE_VALLEY).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WITHER_ROSE).base(Items.DANDELION).add(Source.INFERNAL, 5).remove(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_MUSHROOM).set(Source.EARTH, 5).set(Source.MOON, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_MUSHROOM).base(Items.BROWN_MUSHROOM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_FUNGUS).base(Items.BROWN_MUSHROOM).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_FUNGUS).base(Items.CRIMSON_FUNGUS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_ROOTS).set(Source.EARTH, 2).set(Source.MOON, 2).set(Source.INFERNAL, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_ROOTS).base(Items.CRIMSON_ROOTS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_SPROUTS).base(Items.CRIMSON_ROOTS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WEEPING_VINES).base(Items.VINE).add(Source.MOON, 5).add(Source.INFERNAL, 5).remove(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TWISTING_VINES).base(Items.WEEPING_VINES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SUGAR_CANE).set(Source.EARTH, 5).set(Source.SKY, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.KELP).set(Source.EARTH, 5).set(Source.SEA, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BAMBOO).set(Source.EARTH, 5).set(Source.SKY, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_SLAB).set(Source.EARTH, 1).set(Source.SUN, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_SLAB).base(Items.OAK_SLAB).add(Source.MOON, 1).add(Source.INFERNAL, 1).remove(Source.SUN, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_SLAB).base(Items.CRIMSON_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PETRIFIED_OAK_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MOSSY_COBBLESTONE).base(Items.COBBLESTONE).add(Source.MOON, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OBSIDIAN).set(Source.EARTH, 5).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TORCH).set(Source.EARTH, 2).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHORUS_PLANT).set(Source.EARTH, 5).set(Source.MOON, 5).set(Source.VOID, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHORUS_FLOWER).base(Items.CHORUS_PLANT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPUR_BLOCK).set(Source.EARTH, 5).set(Source.VOID, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPAWNER).set(Source.BLOOD, 20).set(Source.INFERNAL, 10).set(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIAMOND_ORE).base(Items.STONE).add(Source.EARTH, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FARMLAND).base(Items.DIRT).add(Source.SEA, 2).add(Source.SUN, 5).build(consumer);
        // TODO Append 5 infernal to Furnace
        ItemAffinityBuilder.itemAffinity(Items.REDSTONE_ORE).base(Items.STONE).add(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SNOW).set(Source.SEA, 2).set(Source.SKY, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ICE).set(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SNOW_BLOCK).set(Source.SEA, 5).set(Source.SKY, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CACTUS).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PUMPKIN).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CARVED_PUMPKIN).base(Items.PUMPKIN).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHERRACK).set(Source.EARTH, 5).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SOUL_SAND).base(Items.NETHERRACK).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SOUL_SOIL).base(Items.SOUL_SAND).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BASALT).base(Items.STONE).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SOUL_TORCH).base(Items.TORCH).add(Source.INFERNAL, 5).remove(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GLOWSTONE).set(Source.SUN, 20).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.INFESTED_STONE).base(Items.STONE).add(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.INFESTED_COBBLESTONE).base(Items.COBBLESTONE).add(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.INFESTED_STONE_BRICKS).base(Items.STONE_BRICKS).add(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.INFESTED_MOSSY_STONE_BRICKS).base(Items.MOSSY_STONE_BRICKS).add(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.INFESTED_CRACKED_STONE_BRICKS).base(Items.CRACKED_STONE_BRICKS).add(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.INFESTED_CHISELED_STONE_BRICKS).base(Items.CHISELED_STONE_BRICKS).add(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STONE_BRICKS).set(Source.EARTH, 3).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MOSSY_STONE_BRICKS).base(Items.STONE_BRICKS).add(Source.MOON, 3).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRACKED_STONE_BRICKS).base(Items.STONE_BRICKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHISELED_STONE_BRICKS).base(Items.STONE_BRICKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_MUSHROOM_BLOCK).base(Items.MUSHROOM_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_MUSHROOM_BLOCK).base(Items.MUSHROOM_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MUSHROOM_STEM).set(Source.EARTH, 5).set(Source.MOON, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MELON).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.VINE).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MYCELIUM).set(Source.EARTH, 5).set(Source.MOON, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LILY_PAD).set(Source.EARTH, 2).set(Source.SEA, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.END_PORTAL_FRAME).set(Source.EARTH, 5).set(Source.VOID, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.END_STONE).set(Source.EARTH, 5).set(Source.VOID, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DRAGON_EGG).set(Source.BLOOD, 20).set(Source.VOID, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.EMERALD_ORE).base(Items.STONE).add(Source.EARTH, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHIPPED_ANVIL).base(Items.ANVIL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DAMAGED_ANVIL).base(Items.ANVIL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRASS_PATH).base(Items.GRASS_BLOCK).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SUNFLOWER).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LILAC).base(Items.SUNFLOWER).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ROSE_BUSH).base(Items.SUNFLOWER).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PEONY).base(Items.SUNFLOWER).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TALL_GRASS).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LARGE_FERN).base(Items.TALL_GRASS).build(consumer);
        // TODO Append 15 sun to Sea Lantern
        ItemAffinityBuilder.itemAffinity(Items.RED_SANDSTONE).base(Items.SANDSTONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MAGMA_BLOCK).set(Source.INFERNAL, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BONE_BLOCK).set(Source.MOON, 5).set(Source.BLOOD, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_WART_BLOCK).base(Items.NETHER_WART_BLOCK).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MAGENTA_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_BLUE_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.YELLOW_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIME_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAY_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_GRAY_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CYAN_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPLE_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GREEN_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLACK_SHULKER_BOX).base(Items.SHULKER_BOX).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_CONCRETE).base(Items.WHITE_CONCRETE_POWDER).add(Source.SEA, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MAGENTA_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_BLUE_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.YELLOW_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIME_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAY_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_GRAY_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CYAN_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPLE_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GREEN_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLACK_CONCRETE).base(Items.WHITE_CONCRETE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TURTLE_EGG).set(Source.SEA, 5).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_TUBE_CORAL_BLOCK).base(Items.DEAD_TUBE_CORAL).add(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BRAIN_CORAL_BLOCK).base(Items.DEAD_BRAIN_CORAL).add(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BUBBLE_CORAL_BLOCK).base(Items.DEAD_BUBBLE_CORAL).add(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_FIRE_CORAL_BLOCK).base(Items.DEAD_FIRE_CORAL).add(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_HORN_CORAL_BLOCK).base(Items.DEAD_HORN_CORAL).add(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TUBE_CORAL_BLOCK).base(Items.DEAD_TUBE_CORAL_BLOCK).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BRAIN_CORAL_BLOCK).base(Items.DEAD_BRAIN_CORAL_BLOCK).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BUBBLE_CORAL_BLOCK).base(Items.DEAD_BUBBLE_CORAL_BLOCK).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FIRE_CORAL_BLOCK).base(Items.DEAD_FIRE_CORAL_BLOCK).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.HORN_CORAL_BLOCK).base(Items.DEAD_HORN_CORAL_BLOCK).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TUBE_CORAL).base(Items.DEAD_TUBE_CORAL).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BRAIN_CORAL).base(Items.DEAD_BRAIN_CORAL).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BUBBLE_CORAL).base(Items.DEAD_BUBBLE_CORAL).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FIRE_CORAL).base(Items.DEAD_FIRE_CORAL).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.HORN_CORAL).base(Items.DEAD_HORN_CORAL).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_TUBE_CORAL).set(Source.EARTH, 5).set(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BRAIN_CORAL).base(Items.DEAD_TUBE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BUBBLE_CORAL).base(Items.DEAD_TUBE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_FIRE_CORAL).base(Items.DEAD_TUBE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_HORN_CORAL).base(Items.DEAD_TUBE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TUBE_CORAL_FAN).base(Items.TUBE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BRAIN_CORAL_FAN).base(Items.BRAIN_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BUBBLE_CORAL_FAN).base(Items.BUBBLE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FIRE_CORAL_FAN).base(Items.FIRE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.HORN_CORAL_FAN).base(Items.HORN_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_TUBE_CORAL_FAN).base(Items.DEAD_TUBE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BRAIN_CORAL_FAN).base(Items.DEAD_BRAIN_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BUBBLE_CORAL_FAN).base(Items.DEAD_BUBBLE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_FIRE_CORAL_FAN).base(Items.DEAD_FIRE_CORAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_HORN_CORAL_FAN).base(Items.DEAD_HORN_CORAL).build(consumer);
        // TODO Append 15 sun to Conduit
        ItemAffinityBuilder.itemAffinity(Items.SCUTE).set(Source.SEA, 2).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.APPLE).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COAL).set(Source.EARTH, 5).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHARCOAL).base(Items.COAL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIAMOND).set(Source.EARTH, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.IRON_INGOT).set(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GOLD_INGOT).set(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHERITE_SCRAP).set(Source.EARTH, 5).set(Source.INFERNAL, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STICK).set(Source.EARTH, 1).set(Source.SUN, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRING).set(Source.SKY, 5).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FEATHER).set(Source.SKY, 10).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GUNPOWDER).set(Source.EARTH, 5).set(Source.INFERNAL, 15).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHEAT_SEEDS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHEAT).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHAINMAIL_HELMET).base(Items.IRON_HELMET).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHAINMAIL_CHESTPLATE).base(Items.IRON_CHESTPLATE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHAINMAIL_LEGGINGS).base(Items.IRON_LEGGINGS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHAINMAIL_BOOTS).base(Items.IRON_BOOTS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FLINT).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PORKCHOP).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ENCHANTED_GOLDEN_APPLE).base(Items.GOLDEN_APPLE).add(Source.HALLOWED, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WATER_BUCKET).base(Items.BUCKET).add(Source.SEA, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LAVA_BUCKET).base(Items.BUCKET).add(Source.INFERNAL, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SADDLE).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.REDSTONE).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SNOWBALL).set(Source.SEA, 2).set(Source.SKY, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LEATHER).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MILK_BUCKET).base(Items.BUCKET).add(Source.BLOOD, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PUFFERFISH_BUCKET).base(Items.BUCKET).add(Source.SEA, 5).add(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SALMON_BUCKET).base(Items.BUCKET).add(Source.SEA, 5).add(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COD_BUCKET).base(Items.BUCKET).add(Source.SEA, 5).add(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TROPICAL_FISH_BUCKET).base(Items.BUCKET).add(Source.SEA, 5).add(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BRICK).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CLAY_BALL).set(Source.EARTH, 2).set(Source.SEA, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SLIME_BALL).set(Source.SEA, 5).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.EGG).set(Source.SKY, 2).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GLOWSTONE_DUST).set(Source.SUN, 10).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COD).set(Source.SEA, 5).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SALMON).set(Source.SEA, 5).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TROPICAL_FISH).set(Source.SEA, 5).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PUFFERFISH).set(Source.SEA, 5).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.INK_SAC).set(Source.SEA, 5).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COCOA_BEANS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LAPIS_LAZULI).set(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_DYE).set(Source.SEA, 1).set(Source.SUN, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MAGENTA_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_BLUE_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.YELLOW_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIME_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAY_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_GRAY_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CYAN_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPLE_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GREEN_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLACK_DYE).base(Items.WHITE_DYE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BONE_MEAL).set(Source.MOON, 1).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BONE).set(Source.MOON, 5).set(Source.BLOOD, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_BED).set(Source.EARTH, 4).set(Source.SUN, 4).set(Source.BLOOD, 11).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MAGENTA_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_BLUE_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.YELLOW_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIME_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAY_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_GRAY_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CYAN_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPLE_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GREEN_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLACK_BED).base(Items.WHITE_BED).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FILLED_MAP).base(Items.MAP).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MELON_SLICE).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DRIED_KELP).base(Items.KELP).remove(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PUMPKIN_SEEDS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MELON_SEEDS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BEEF).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHICKEN).set(Source.SKY, 2).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ROTTEN_FLESH).set(Source.MOON, 5).set(Source.BLOOD, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ENDER_PEARL).set(Source.VOID, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLAZE_ROD).set(Source.INFERNAL, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GHAST_TEAR).set(Source.SKY, 5).set(Source.BLOOD, 5).set(Source.INFERNAL, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_WART).set(Source.EARTH, 5).set(Source.MOON, 5).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POTION).set(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPIDER_EYE).set(Source.MOON, 5).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.EXPERIENCE_BOTTLE).set(auraUnit.copy().multiply(5)).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WRITTEN_BOOK).base(Items.WRITABLE_BOOK).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.EMERALD).set(Source.EARTH, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FLOWER_POT).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CARROT).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POTATO).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POISONOUS_POTATO).base(Items.POTATO).add(Source.MOON, 5).add(Source.VOID, 2).remove(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SKELETON_SKULL).set(Source.MOON, 5).set(Source.BLOOD, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WITHER_SKELETON_SKULL).base(Items.SKELETON_SKULL).add(Source.INFERNAL, 10).remove(Source.MOON, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PLAYER_HEAD).set(Source.BLOOD, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ZOMBIE_HEAD).set(Source.MOON, 5).set(Source.BLOOD, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CREEPER_HEAD).set(Source.BLOOD, 20).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DRAGON_HEAD).set(Source.BLOOD, 20).set(Source.VOID, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_STAR).set(Source.EARTH, 25).set(Source.SEA, 25).set(Source.SKY, 25).set(Source.SUN, 25).set(Source.MOON, 25).set(Source.BLOOD, 25).set(Source.INFERNAL, 25).set(Source.VOID, 25).set(Source.HALLOWED, 25).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FIREWORK_ROCKET).set(Source.EARTH, 5).set(Source.SKY, 5).set(Source.SUN, 15).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FIREWORK_STAR).base(Items.GUNPOWDER).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ENCHANTED_BOOK).set(auraUnit.copy().multiply(5)).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_BRICK).base(Items.BRICK).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.QUARTZ).set(Source.EARTH, 10).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PRISMARINE_SHARD).set(Source.EARTH, 2).set(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PRISMARINE_CRYSTALS).set(Source.EARTH, 5).set(Source.SEA, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RABBIT).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RABBIT_FOOT).set(Source.MOON, 20).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RABBIT_HIDE).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.IRON_HORSE_ARMOR).set(Source.EARTH, 52).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GOLDEN_HORSE_ARMOR).set(Source.EARTH, 52).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIAMOND_HORSE_ARMOR).set(Source.EARTH, 105).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MUTTON).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHORUS_FRUIT).set(Source.EARTH, 5).set(Source.MOON, 5).set(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BEETROOT).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BEETROOT_SEEDS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DRAGON_BREATH).set(Source.SKY, 10).set(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPLASH_POTION).base(Items.POTION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TIPPED_ARROW).base(Items.ARROW).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LINGERING_POTION).base(Items.POTION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ELYTRA).set(Source.SKY, 25).set(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TOTEM_OF_UNDYING).set(Source.HALLOWED, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SHULKER_SHELL).set(Source.BLOOD, 5).set(Source.VOID, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TRIDENT).set(Source.EARTH, 15).set(Source.SEA, 15).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PHANTOM_MEMBRANE).set(Source.SKY, 10).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NAUTILUS_SHELL).set(Source.SEA, 10).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.HEART_OF_THE_SEA).set(Source.SEA, 25).build(consumer);
        // TODO Define suspicious stew
        ItemAffinityBuilder.itemAffinity(Items.GLOBE_BANNER_PATTERN).base(Items.PAPER).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PIGLIN_BANNER_PATTERN).base(Items.PAPER).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BELL).set(Source.EARTH, 10).build(consumer);
        // TODO Append 15 sun to Lantern
        // TODO Append 15 sun to Soul Lantern
        ItemAffinityBuilder.itemAffinity(Items.SWEET_BERRIES).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SHROOMLIGHT).set(Source.EARTH, 5).set(Source.SUN, 15).set(Source.MOON, 10).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.HONEYCOMB).set(Source.SEA, 5).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BEE_NEST).base(Items.BEEHIVE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.HONEY_BOTTLE).base(Items.GLASS_BOTTLE).add(Source.SEA, 5).add(Source.BLOOD, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ANCIENT_DEBRIS).base(Items.NETHERRACK).add(Source.EARTH, 5).add(Source.INFERNAL, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRYING_OBSIDIAN).base(Items.OBSIDIAN).add(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLACKSTONE).base(Items.COBBLESTONE).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GILDED_BLACKSTONE).base(Items.BLACKSTONE).add(Source.EARTH, 10).build(consumer);
        
        // Define mod affinities
        ItemAffinityBuilder.itemAffinity(ItemsPM.MARBLE_RAW.get()).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(ItemsPM.MARBLE_ENCHANTED.get()).base(ItemsPM.MARBLE_RAW.get()).add(auraUnit.copy()).build(consumer);
        
        // Define potion bonuses
        PotionBonusAffinityBuilder.potionBonusAffinity(Potions.NIGHT_VISION).bonus(Source.SUN, 2).build(consumer);
        
        // Define enchantment bonuses
        EnchantmentBonusAffinityBuilder.enchantmentBonusAffinity(Enchantments.PROTECTION).multiplier(Source.EARTH).build(consumer);
    }

    @Override
    public String getName() {
        return "Primal Magic Affinities";
    }
}
