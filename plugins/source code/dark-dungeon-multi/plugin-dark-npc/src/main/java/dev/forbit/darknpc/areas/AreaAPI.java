package dev.forbit.darknpc.areas;

import dev.forbit.darknpc.DarkNpc;
import dev.forbit.generator.plugin.Generator;
import dev.forbit.generator.plugin.Position;
import dev.forbit.library.Utils;
import dev.forbit.library.menu.DarkMenu;
import dev.forbit.library.menu.MenuItem;
import dev.forbit.library.menu.type.BasicItem;
import dev.forbit.library.menu.type.MenuNavItem;
import lombok.Getter;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.MineSkinFetcher;
import net.jitse.npclib.api.state.NPCSlot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class AreaAPI {
    @Getter DarkNpc main;
    @Getter HashMap<NPC, OfflinePlayer> npcs = new HashMap<>();

    public AreaAPI(DarkNpc main) { this.main = main; }

    public void initArea(UUID id) {
        Position pos = Generator.getAPI().getCell(id);
        Location center = Generator.getAPI().getAnchorPoint(pos);
        PlayerArea area = new PlayerArea();
        area.setCenterX(center.getX());
        area.setCenterY(center.getY());
        area.setCenterZ(center.getZ());
        Location npcLoc = area.getNPCManagerLocation();

        MineSkinFetcher.fetchSkinFromIdSync(524077545, (skin) -> {
            NPC npc = getMain().getLibrary().createNPC(Utils.splitString(ChatColor.LIGHT_PURPLE + "NPC Manager", 100));
            npc.setLocation(npcLoc);
            npc.create();
            npc.setSkin(skin);
            npc.setItem(NPCSlot.MAINHAND, new ItemStack(Material.GOLD_INGOT));
            npc.show(Bukkit.getOfflinePlayer(id).getPlayer());
            npcs.put(npc, Bukkit.getOfflinePlayer(id));
        });
    }


    /* UNLOCK NPC MENU */
    public DarkMenu getUnlockNPCMenu() {
        DarkMenu menu = new DarkMenu(ChatColor.LIGHT_PURPLE + "Unlock NPCs");
        menu.addItem(getUnlockItem(NPCType.FARMER), 2);
        menu.addItem(getUnlockItem(NPCType.SMITH), 3);
        menu.addItem(getUnlockItem(NPCType.HEALER), 4);
        menu.addItem(getUnlockItem(NPCType.ALCHEMIST), 5);
        menu.addItem(getUnlockItem(NPCType.GUILD_REP), 6);
        menu.addItem(getUnlockItem(NPCType.INFUSER), 12);
        menu.addItem(getUnlockItem(NPCType.ADV_ITEM_SMITH), 13);
        menu.addItem(getUnlockItem(NPCType.ENCHANTER), 14);
        return menu;
    }

    public MenuNavItem getUnlockItem(NPCType t) {
        return new MenuNavItem(t.getMenuMaterial(), "&d" + t.getTitle(), null, getUnlockMenu(t));
    }

    public DarkMenu getUnlockMenu(NPCType t) {
        DarkMenu menu = new DarkMenu(ChatColor.GREEN+"Unlock "+t.getTitle()+" NPC?");
        menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aConfirm", null, null), 2);
        menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cCancel", null, null), 6);
        return menu;
    }


    /* UPGRADE AREA MENU */
    public DarkMenu getUpgradeNPCMenu() {
        DarkMenu menu = new DarkMenu(ChatColor.LIGHT_PURPLE + "Upgrade NPCs");
        menu.addItem(getUpgradeItem(NPCType.FARMER), 2);
        menu.addItem(getUpgradeItem(NPCType.SMITH), 3);
        menu.addItem(getUpgradeItem(NPCType.HEALER), 4);
        menu.addItem(getUpgradeItem(NPCType.ALCHEMIST), 5);
        menu.addItem(getUpgradeItem(NPCType.GUILD_REP), 6);
        menu.addItem(getUpgradeItem(NPCType.INFUSER), 12);
        menu.addItem(getUpgradeItem(NPCType.ADV_ITEM_SMITH), 13);
        menu.addItem(getUpgradeItem(NPCType.ENCHANTER), 14);
        return menu;
    }

    public MenuNavItem getUpgradeItem(NPCType t) {
        return new MenuNavItem(t.getMenuMaterial(), "&d" + t.getTitle(), null, getUpgradeMenu(t));
    }

    public DarkMenu getUpgradeMenu(NPCType t) {
        DarkMenu menu = new DarkMenu(ChatColor.GREEN+"Upgrade "+t.getTitle()+" NPC to level x?");
        menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aConfirm", null, null), 2);
        menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cCancel", null, null), 6);
        menu.addItem(new BasicItem(Material.GRAY_STAINED_GLASS_PANE, "&8Cost: &7"+3000+" silver", null, null), 4);
        return menu;
    }

    /* MANAGE AREA MENU */
    public DarkMenu getManageAreasMenu() {
        DarkMenu menu = new DarkMenu(ChatColor.DARK_PURPLE+"Manage Areas");
        for (int i = 0; i < 7; i++) {
            menu.addItem(getAreaItem(i), i+1);
        }
        return menu;
    }

    public MenuItem getAreaItem(int id) {
        return new BasicItem(Material.GRAY_STAINED_GLASS_PANE, "Area "+id, null, null);
    }

    /* NPC MANAGER MENU */
    public DarkMenu getManagerMenu() {
        DarkMenu menu = new DarkMenu(ChatColor.RED+"Manage NPCs");
        menu.addItem(new MenuNavItem(Material.GOLD_NUGGET, "&6Unlock NPC", null, getUnlockNPCMenu()), 2);
        menu.addItem(new MenuNavItem(Material.IRON_BLOCK, "&fManage Areas", null, getManageAreasMenu()), 4);
        menu.addItem(new MenuNavItem(Material.GOLD_INGOT, "&dUpgrade NPCs", null, getUpgradeNPCMenu()), 6);

        return menu;
    }
}
