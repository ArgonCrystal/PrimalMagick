package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.fx.particles.AirCurrentParticle;
import com.verdantartifice.primalmagick.client.fx.particles.InfernalFlameParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ManaSparkleParticle;
import com.verdantartifice.primalmagick.client.fx.particles.NoteEmitterParticle;
import com.verdantartifice.primalmagick.client.fx.particles.OfferingParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PotionExplosionParticle;
import com.verdantartifice.primalmagick.client.fx.particles.PropMarkerParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellSparkleParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellcraftingRuneParticle;
import com.verdantartifice.primalmagick.client.fx.particles.WandPoofParticle;
import com.verdantartifice.primalmagick.client.gui.hud.WandHudOverlay;
import com.verdantartifice.primalmagick.client.tooltips.ClientAffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrationEvents {
    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.register(ParticleTypesPM.WAND_POOF.get(), WandPoofParticle.Factory::new);
        event.register(ParticleTypesPM.MANA_SPARKLE.get(), ManaSparkleParticle.Factory::new);
        event.register(ParticleTypesPM.SPELL_SPARKLE.get(), SpellSparkleParticle.Factory::new);
        event.register(ParticleTypesPM.SPELL_BOLT.get(), SpellBoltParticle.Factory::new);
        event.register(ParticleTypesPM.OFFERING.get(), OfferingParticle.Factory::new);
        event.register(ParticleTypesPM.PROP_MARKER.get(), PropMarkerParticle.Factory::new);
        event.register(ParticleTypesPM.POTION_EXPLOSION.get(), new PotionExplosionParticle.Factory());
        event.register(ParticleTypesPM.NOTE_EMITTER.get(), new NoteEmitterParticle.Factory());
        event.register(ParticleTypesPM.SPELLCRAFTING_RUNE_U.get(), SpellcraftingRuneParticle.Factory::new);
        event.register(ParticleTypesPM.SPELLCRAFTING_RUNE_V.get(), SpellcraftingRuneParticle.Factory::new);
        event.register(ParticleTypesPM.SPELLCRAFTING_RUNE_T.get(), SpellcraftingRuneParticle.Factory::new);
        event.register(ParticleTypesPM.SPELLCRAFTING_RUNE_D.get(), SpellcraftingRuneParticle.Factory::new);
        event.register(ParticleTypesPM.INFERNAL_FLAME.get(), InfernalFlameParticle.Factory::new);
        event.register(ParticleTypesPM.AIR_CURRENT.get(), AirCurrentParticle.Factory::new);
        event.register(ParticleTypesPM.VOID_SMOKE.get(), AirCurrentParticle.Factory::new);
    }
    
    /**
     * Register special model resource locations that must be loaded even if not tied to a block state.
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(new ResourceLocation(PrimalMagick.MODID, "mundane_wand_core"), ""));
        for (WandCore core : WandCore.getAllWandCores()) {
            event.register(core.getWandModelResourceLocation());
            event.register(core.getStaffModelResourceLocation());
        }
        for (WandCap cap : WandCap.getAllWandCaps()) {
            event.register(cap.getWandModelResourceLocation());
            event.register(cap.getStaffModelResourceLocation());
        }
        for (WandGem gem : WandGem.getAllWandGems()) {
            event.register(gem.getModelResourceLocation());
        }
        for (int index = 0; index <= 4; index++) {
            event.register(new ModelResourceLocation(new ResourceLocation(PrimalMagick.MODID, "arcanometer_" + index), ""));
        }
    }
    
    @SubscribeEvent
    public static void onClientReloadListenerRegister(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ItemsPM.PRIMALITE_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HEXIUM_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HALLOWSTEEL_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.FORBIDDEN_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.PRIMALITE_SHIELD.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HEXIUM_SHIELD.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HALLOWSTEEL_SHIELD.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.SPELLCRAFTING_ALTAR.get().getRenderProperties().getCustomRenderer());
    }
    
    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "wand_hud", new WandHudOverlay());
    }
    
    @SubscribeEvent
    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AffinityTooltipComponent.class, ClientAffinityTooltipComponent::new);
    }
}
