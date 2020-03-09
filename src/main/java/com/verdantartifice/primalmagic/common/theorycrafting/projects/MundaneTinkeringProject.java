package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.item.Items;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class MundaneTinkeringProject extends AbstractProject {
    public static final String TYPE = "mundane_tinkering";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    
    static {
        OPTIONS.add(new ItemProjectMaterial(Items.CRAFTING_TABLE, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.ANVIL, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.FURNACE, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.BLAST_FURNACE, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.LOOM, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.SMOKER, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.SMITHING_TABLE, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.STONECUTTER, false), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.GRINDSTONE, false), 1);
        OPTIONS.add(new ObservationProjectMaterial(), 5);
    }
    
    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions() {
        return OPTIONS;
    }
}
