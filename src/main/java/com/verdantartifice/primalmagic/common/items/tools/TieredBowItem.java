package com.verdantartifice.primalmagic.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of a repairable bow item made of a magical metal.
 * 
 * @author Daedalus4096
 */
public class TieredBowItem extends BowItem {
    protected final Tier tier;
    
    public TieredBowItem(Tier tier, Item.Properties properties) {
        super(properties.defaultDurability(tier.getUses()));
        this.tier = tier;
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        AbstractArrow newArrow = super.customArrow(arrow);
        double damageBonus = 0.0D;
        if (this.tier == ItemTierPM.PRIMALITE) {
            damageBonus = 0.5D;
        } else if (this.tier == ItemTierPM.HEXIUM) {
            damageBonus = 1.0D;
        } else if (this.tier == ItemTierPM.HALLOWSTEEL) {
            damageBonus = 1.5D;
        }
        newArrow.setBaseDamage(newArrow.getBaseDamage() + damageBonus);
        return newArrow;
    }
    
    public Tier getTier() {
        return this.tier;
    }

    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }
}
