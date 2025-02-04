package com.verdantartifice.primalmagick.datagen.loot_modifiers;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.conditions.MatchBlockTag;
import com.verdantartifice.primalmagick.common.loot.modifiers.BloodNotesModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BloodyFleshModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BonusNuggetModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BountyFarmingModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.BountyFishingModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.FourLeafCloverModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.HummingArtifactModifier;
import com.verdantartifice.primalmagick.common.loot.modifiers.RelicFragmentsModifier;
import com.verdantartifice.primalmagick.common.tags.BlockTagsPM;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingHookPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

/**
 * Data provider for all of the mod's loot modifiers.
 * 
 * @author Daedalus4096
 */
public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(DataGenerator gen) {
        super(gen, PrimalMagick.MODID);
    }

    @Override
    protected void start() {
        this.add("bloody_flesh", new BloodyFleshModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOODY_FLESH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.1F).build()
                }));
        this.add("bounty_farming", new BountyFarmingModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(BlockTagsPM.BOUNTY_CROPS).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.25F));
        this.add("bounty_fishing", new BountyFishingModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(FishingHookPredicate.inOpenWater(true))).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.BOUNTY.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.25F));
        this.add("bonus_nugget_iron", new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_IRON).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.5F, Items.IRON_NUGGET));
        this.add("bonus_nugget_gold", new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_GOLD).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.5F, Items.GOLD_NUGGET));
        this.add("bonus_nugget_quartz", new BonusNuggetModifier(
                new LootItemCondition[] {
                        MatchBlockTag.builder(Tags.Blocks.ORES_QUARTZ).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantmentsPM.LUCKY_STRIKE.get(), MinMaxBounds.Ints.atLeast(1)))).build()
                }, 0.5F, ItemsPM.QUARTZ_NUGGET.get()));
        this.add("blood_notes_high", new BloodNotesModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOOD_NOTES_HIGH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }));
        this.add("blood_notes_low", new BloodNotesModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_BLOOD_NOTES_LOW)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.25F).build()
                }));
        this.add("relic_fragments_high", new RelicFragmentsModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_HIGH)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build()
                }, 3, 5, 1));
        this.add("relic_fragments_low", new RelicFragmentsModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_LOW)).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.05F).build()
                }, 1, 1, 0));
        this.add("four_leaf_clover_short_grass", new FourLeafCloverModifier(
                new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.004F, 0.003F).build()
                }));
        this.add("four_leaf_clover_tall_grass", new FourLeafCloverModifier(
                new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build(),
                        LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.004F, 0.003F).build()
                }));
        this.add("humming_artifact_abandoned_mineshaft", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }));
        this.add("humming_artifact_ancient_city", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }));
        this.add("humming_artifact_bastion_treasure", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.BASTION_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }));
        this.add("humming_artifact_buried_treasure", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }));
        this.add("humming_artifact_desert_pyramid", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.DESERT_PYRAMID).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }));
        this.add("humming_artifact_end_city_treasure", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.END_CITY_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }));
        this.add("humming_artifact_igloo_chest", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.IGLOO_CHEST).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }));
        this.add("humming_artifact_jungle_temple", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.JUNGLE_TEMPLE).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }));
        this.add("humming_artifact_nether_fortress", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.NETHER_BRIDGE).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }));
        this.add("humming_artifact_pillager_outpost", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.PILLAGER_OUTPOST).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }));
        this.add("humming_artifact_shipwreck_treasure", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE).build(),
                        LootItemRandomChanceCondition.randomChance(0.6F).build()
                }));
        this.add("humming_artifact_simple_dungeon", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON).build(),
                        LootItemRandomChanceCondition.randomChance(0.4F).build()
                }));
        this.add("humming_artifact_stronghold_library", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_LIBRARY).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }));
        this.add("humming_artifact_underwater_ruin_big", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG).build(),
                        LootItemRandomChanceCondition.randomChance(0.8F).build()
                }));
        this.add("humming_artifact_woodland_mansion", new HummingArtifactModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION).build(),
                        LootItemRandomChanceCondition.randomChance(1.0F).build()
                }));
    }
}
