package com.verdantartifice.primalmagick.common.enchantments;

import com.verdantartifice.primalmagick.common.effects.EffectsPM;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Definition of an enchantment that causes mobs to drop a sample of their essence when killed.
 * 
 * @author Daedalus4096
 */
public class EssenceThiefEnchantment extends AbstractRuneEnchantment {
    public EssenceThiefEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 5 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof TridentItem || item instanceof AxeItem ? true : super.canEnchant(stack);
    }

    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        super.doPostAttack(user, target, level);
        if (target instanceof LivingEntity) {
            ((LivingEntity)target).addEffect(new MobEffectInstance(EffectsPM.STOLEN_ESSENCE.get(), 200, Math.max(0, level - 1)));
        }
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}
