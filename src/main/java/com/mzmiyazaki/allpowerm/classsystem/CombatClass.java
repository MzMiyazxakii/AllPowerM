package com.mzmiyazaki.allpowerm.classsystem;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/** A combat class that can be supplied by this mod or another mod. */
public final class CombatClass {
    private final ResourceLocation id;
    private final Component displayName;
    private final ItemStack icon;
    private final boolean unlocked;

    public CombatClass(ResourceLocation id, Component displayName, ItemStack icon, boolean unlocked) {
        this.id = Objects.requireNonNull(id);
        this.displayName = Objects.requireNonNull(displayName);
        this.icon = Objects.requireNonNull(icon).copy();
        this.unlocked = unlocked;
    }

    public ResourceLocation getId() { return id; }
    public Component getDisplayName() { return displayName; }
    public ItemStack getIcon() { return icon.copy(); }
    public boolean isUnlocked() { return unlocked; }
}
