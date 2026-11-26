package com.mzmiyazaki.allpowerm.classsystem;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Public registration point for combat classes contributed by other mods. */
public final class CombatClassRegistry {
    private static final Map<ResourceLocation, CombatClass> CLASSES = new LinkedHashMap<>();
    private static ResourceLocation selectedClass;

    private CombatClassRegistry() {}

    public static void register(CombatClass combatClass) {
        if (CLASSES.putIfAbsent(combatClass.getId(), combatClass) != null) {
            throw new IllegalArgumentException("Combat class already registered: " + combatClass.getId());
        }
    }

    public static Collection<CombatClass> getAll() {
        return Collections.unmodifiableCollection(CLASSES.values());
    }

    public static CombatClass get(ResourceLocation id) {
        return CLASSES.get(id);
    }

    public static ResourceLocation getSelectedClass() {
        return selectedClass;
    }

    public static void setSelectedClass(ResourceLocation id) {
        CombatClass combatClass = CLASSES.get(id);
        if (combatClass != null && combatClass.isUnlocked()) selectedClass = id;
    }
}
