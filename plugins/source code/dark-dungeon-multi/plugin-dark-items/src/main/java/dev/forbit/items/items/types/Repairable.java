package dev.forbit.items.items.types;

public interface Repairable {

    int getUses();

    void setUses(int uses);

    int getRepair();

    void setRepair(int uses);

    default void repair() {
        setRepair(getUses());
    }

    default void addUse(int uses) {
        setUses(getUses() + uses);
    }

    default boolean isRepaired() {
        return getUses()-getRepair() < 100;
    }
    default boolean isBlunt() {
        return getUses()-getRepair() > 250;
    }
}
