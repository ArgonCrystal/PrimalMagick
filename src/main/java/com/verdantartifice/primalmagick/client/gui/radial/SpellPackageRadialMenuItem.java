package com.verdantartifice.primalmagick.client.gui.radial;

import java.util.Collections;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;

public class SpellPackageRadialMenuItem extends TextRadialMenuItem {
    private final SpellPackage spellPackage;
    private final int slot;
    
    public SpellPackageRadialMenuItem(GenericRadialMenu owner, int slot, SpellPackage spell, Component altText) {
        super(owner, altText, 0x7FFFFFFF);
        this.slot = slot;
        this.spellPackage = spell;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public SpellPackage getSpellPackage() {
        return this.spellPackage;
    }
    
    @Override
    public void draw(DrawingContext context) {
        if (this.spellPackage == null) {
            super.draw(context);
        } else {
            PoseStack newStack = new PoseStack();
            newStack.pushPose();
            newStack.mulPoseMatrix(context.matrixStack.last().pose());
            newStack.translate(-8, -8, context.z + 200);
            newStack.scale(0.5F, 0.5F, 1F);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.spellPackage.getIcon());
            GuiComponent.blit(newStack, 2 * (int)context.x, 2 * (int)context.y, 0, 0, 32, 32, 32, 32);
            newStack.popPose();
        }
    }

    @Override
    public void drawTooltips(DrawingContext context) {
        if (this.spellPackage == null) {
            super.drawTooltips(context);
        } else {
            context.drawingHelper.renderTooltip(context.matrixStack, Collections.singletonList(this.spellPackage.getName()), (int)context.x, (int)context.y);
        }
    }
}

/*
 Note: This code was adapted from David Quintana's implementation in Tool Belt.
 Below is the copyright notice.
 
 Copyright (c) 2015, David Quintana <gigaherz@gmail.com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
     * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of the author nor the
       names of the contributors may be used to endorse or promote products
       derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
