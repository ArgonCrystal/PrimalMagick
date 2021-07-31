package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagic.common.research.ResearchStage;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to tell the server to attempt to progress to the next stage of the current research entry in the grimoire.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ProgressButton extends Button {
    protected ResearchStage stage;
    protected GrimoireScreen screen;
    
    public ProgressButton(ResearchStage stage, int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 119, 20, text, new Handler());
        this.stage = stage;
        this.screen = screen;
    }
    
    public ResearchStage getStage() {
        return this.stage;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof ProgressButton) {
                // Send a packet to the server and tell the screen to update more frequently until resolved
                ProgressButton pb = (ProgressButton)button;
                PacketHandler.sendToServer(new SyncProgressPacket(pb.getStage().getResearchEntry().getKey(), false, true, true));
                pb.getScreen().setProgressing();
            }
        }
    }
}
