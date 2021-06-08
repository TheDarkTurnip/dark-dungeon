package dev.forbit.library.menu.type;

import dev.forbit.library.Utils;
import dev.forbit.library.menu.MenuItem;
import jdk.internal.jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;

public class BasicItem implements MenuItem {
    @Getter @Setter Material material;
    @Getter @Setter String name;
    @Getter @Setter @Nullable List<String> lore;
    @Getter @Setter Runnable clickAction;

    public BasicItem(Material material, String itemName, @Nullable String lore, Runnable action) {
        setMaterial(material);
        setName("&r"+itemName);
        setLore(lore != null ? Utils.splitString("&8"+lore, 16) : null);
        setClickAction(action);
    }

}
