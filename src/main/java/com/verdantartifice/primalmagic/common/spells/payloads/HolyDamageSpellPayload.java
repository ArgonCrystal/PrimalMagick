package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Definition of a holy damage spell.  Deals standard damage to the target, or double standard damage
 * to undead targets.  No secondary effects.
 * 
 * @author Daedalus4096
 */
public class HolyDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "holy_damage";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_HOLY"));
    
    public HolyDamageSpellPayload() {
        super();
    }
    
    public HolyDamageSpellPayload(int power) {
        super(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public Source getSource() {
        return Source.HALLOWED;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ANGELS.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell, ItemStack spellSource) {
        int damage = 3 + this.getModdedPropertyValue("power", spell, spellSource);
        if (target instanceof LivingEntity && ((LivingEntity)target).isInvertedHealAndHarm()) {
            // Deal double damage to undead entities
            damage *= 2;
        }
        return (float)damage;
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    @Override
    public int getBaseManaCost() {
        return 2 * this.getPropertyValue("power");
    }
}
