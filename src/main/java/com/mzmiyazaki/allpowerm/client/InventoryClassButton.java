package com.mzmiyazaki.allpowerm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class InventoryClassButton extends AbstractButton {
    public InventoryClassButton(int x, int y) {
        super(x, y, 20, 20, Component.empty());
    }

    @Override
    public void onPress() {
        Minecraft.getInstance().setScreen(new ClassSelectionScreen());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int background = isHoveredOrFocused() ? 0xFF657D90 : 0xFF354451;
        graphics.fill(getX(), getY(), getX() + width, getY() + height, 0xFF141B22);
        graphics.fill(getX() + 1, getY() + 1, getX() + width - 1, getY() + height - 1, background);
        graphics.renderItem(Items.COMPASS.getDefaultInstance(), getX() + 2, getY() + 2);
        if (isHoveredOrFocused()) {
            graphics.fill(getX() + 1, getY() + height - 2, getX() + width - 1, getY() + height - 1, 0xFFD5E3ED);
        }
    }

    @Override
    protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
