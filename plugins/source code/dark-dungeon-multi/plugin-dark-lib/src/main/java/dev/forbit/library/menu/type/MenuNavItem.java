package dev.forbit.library.menu.type;

import dev.forbit.library.Utils;
import dev.forbit.library.menu.DarkMenu;
import dev.forbit.library.menu.MenuItem;
import jdk.internal.jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuNavItem implements MenuItem {
    @Getter @Setter DarkMenu nextMenu;
    @Getter @Setter Player player;


    @Getter @Setter Material material;
    @Getter @Setter String name;
    @Getter @Setter @Nullable List<String> lore;
    @Getter @Setter Runnable clickAction;

    public MenuNavItem(Material material, String itemName, @Nullable String lore, DarkMenu menu) {
        setMaterial(material);
        setName("&r"+itemName);
        setLore(lore != null ? Utils.splitString("&8" + lore, 16) : null);
        setNextMenu(menu);
        setClickAction(() -> {
            getNextMenu().show(getPlayer());
        });
    }

}
