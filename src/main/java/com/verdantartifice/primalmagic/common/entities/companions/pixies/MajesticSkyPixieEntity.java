package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;

/**
 * Definition of a majestic sky pixie.  Greatest of the sky pixies.
 * 
 * @author Daedalus4096
 */
public class MajesticSkyPixieEntity extends AbstractSkyPixieEntity implements IMajesticPixie {
    public MajesticSkyPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 12.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D);
    }
    
    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.MAJESTIC_SKY_PIXIE.get();
    }
}
