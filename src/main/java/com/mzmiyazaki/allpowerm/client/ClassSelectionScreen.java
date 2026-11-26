package com.mzmiyazaki.allpowerm.client;

import com.mzmiyazaki.allpowerm.classsystem.CombatClass;
import com.mzmiyazaki.allpowerm.classsystem.CombatClassRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectionScreen extends Screen {
    private static final int CARD_WIDTH = 58;
    private static final int CARD_HEIGHT = 76;
    private static final int GAP = 8;
    private final List<CombatClass> classes = new ArrayList<>();
    private int scroll;
    private int listLeft;
    private int listRight;
    private int listTop;

    public ClassSelectionScreen() {
        super(Component.empty());
        classes.addAll(CombatClassRegistry.getAll());
    }

    @Override
    protected void init() {
        super.init();
        listLeft = Math.max(18, (width - Math.min(width - 36, 5 * CARD_WIDTH + 4 * GAP)) / 2);
        listRight = width - listLeft;
        listTop = height / 2 - CARD_HEIGHT / 2;
        clampScroll();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.fill(0, 0, width, height, 0xA610141B);
        int panelWidth = Math.min(width - 24, 5 * CARD_WIDTH + 4 * GAP + 28);
        int panelLeft = (width - panelWidth) / 2;
        int panelTop = height / 2 - 62;
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + 124, 0xEE202832);
        graphics.fill(panelLeft + 1, panelTop + 1, panelLeft + panelWidth - 1, panelTop + 2, 0xFF8496A5);
        graphics.fill(panelLeft + 8, panelTop + 8, panelLeft + panelWidth - 8, panelTop + 9, 0x554D6475);

        int hovered = -1;
        for (int i = 0; i < classes.size(); i++) {
            int x = listLeft + i * (CARD_WIDTH + GAP) - scroll;
            if (x + CARD_WIDTH < panelLeft + 8 || x > panelLeft + panelWidth - 8) continue;
            CombatClass combatClass = classes.get(i);
            boolean inside = mouseX >= x && mouseX < x + CARD_WIDTH && mouseY >= listTop
                    && mouseY < listTop + CARD_HEIGHT;
            if (inside) hovered = i;
            int background = inside ? 0xFF46596A : 0xFF303D49;
            if (!combatClass.isUnlocked()) background = 0xFF292F35;
            graphics.fill(x, listTop, x + CARD_WIDTH, listTop + CARD_HEIGHT, background);
            graphics.fill(x + 1, listTop + 1, x + CARD_WIDTH - 1, listTop + 2,
                    combatClass.isUnlocked() ? 0xFF9FB5C5 : 0xFF555D65);
            ItemStack icon = combatClass.getIcon();
            int iconX = x + (CARD_WIDTH - 16) / 2;
            int iconY = listTop + 25;
            if (combatClass.isUnlocked()) {
                graphics.renderItem(icon, iconX, iconY);
            } else {
                graphics.pose().pushPose();
                graphics.setColor(0.35f, 0.35f, 0.35f, 1.0f);
                graphics.renderItem(icon, iconX, iconY);
                graphics.setColor(1f, 1f, 1f, 1f);
                graphics.pose().popPose();
                drawLock(graphics, x + CARD_WIDTH / 2, listTop + 54);
            }
            if (inside) graphics.fill(x + 2, listTop + CARD_HEIGHT - 3,
                    x + CARD_WIDTH - 2, listTop + CARD_HEIGHT - 2, 0xFFB8C9D5);
        }
        drawArrow(graphics, panelLeft + 7, height / 2 - 7, true, scroll > 0);
        drawArrow(graphics, panelLeft + panelWidth - 17, height / 2 - 7, false,
                scroll < maxScroll());
        if (hovered >= 0) graphics.renderTooltip(font, classes.get(hovered).getDisplayName(),
                mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawLock(GuiGraphics graphics, int centerX, int y) {
        graphics.fill(centerX - 4, y - 5, centerX + 4, y + 3, 0xFFB6BEC5);
        graphics.fill(centerX - 2, y - 9, centerX + 2, y - 4, 0xFFB6BEC5);
        graphics.fill(centerX - 1, y - 8, centerX + 1, y - 4, 0xFF414B54);
        graphics.fill(centerX - 1, y - 2, centerX + 1, y + 1, 0xFF414B54);
    }

    private void drawArrow(GuiGraphics graphics, int x, int y, boolean left, boolean active) {
        int color = active ? 0xFFD4DEE6 : 0xFF59636C;
        if (left) {
            graphics.fill(x + 5, y, x + 8, y + 2, color);
            graphics.fill(x + 2, y + 2, x + 7, y + 4, color);
            graphics.fill(x, y + 4, x + 5, y + 6, color);
            graphics.fill(x + 2, y + 6, x + 7, y + 8, color);
            graphics.fill(x + 5, y + 8, x + 8, y + 10, color);
        } else {
            graphics.fill(x, y, x + 3, y + 2, color);
            graphics.fill(x + 1, y + 2, x + 6, y + 4, color);
            graphics.fill(x + 3, y + 4, x + 8, y + 6, color);
            graphics.fill(x + 1, y + 6, x + 6, y + 8, color);
            graphics.fill(x, y + 8, x + 3, y + 10, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseY >= listTop && mouseY < listTop + CARD_HEIGHT) {
            for (int i = 0; i < classes.size(); i++) {
                int x = listLeft + i * (CARD_WIDTH + GAP) - scroll;
                if (mouseX >= x && mouseX < x + CARD_WIDTH) {
                    CombatClass combatClass = classes.get(i);
                    if (combatClass.isUnlocked()) {
                        CombatClassRegistry.setSelectedClass(combatClass.getId());
                        onClose();
                    }
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scroll -= (int) Math.signum(delta) * (CARD_WIDTH + GAP);
        clampScroll();
        return true;
    }

    private int maxScroll() {
        int contentWidth = classes.size() * (CARD_WIDTH + GAP) - GAP;
        return Math.max(0, contentWidth - (listRight - listLeft));
    }

    private void clampScroll() {
        scroll = Math.max(0, Math.min(scroll, maxScroll()));
    }

    @Override
    public void onClose() {
        minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
