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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * REFACTOR refactor the menu mess.
 */
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
        getMain().getPlayerAreas().put(id, area);
    }

    public PlayerArea getArea(OfflinePlayer oPlayer) {
        if (!getMain().getPlayerAreas().containsKey(oPlayer.getUniqueId())) {
            getMain().getPlayerAreas().put(oPlayer.getUniqueId(), new PlayerArea());
        }

        return getMain().getPlayerAreas().get(oPlayer.getUniqueId());
    }


    /* UNLOCK NPC MENU */
    public DarkMenu getUnlockNPCMenu(DarkMenu mainMenu, PlayerArea area) {
        DarkMenu menu = new DarkMenu(ChatColor.LIGHT_PURPLE + "Unlock NPCs");
        menu.addItem(new MenuNavItem(Material.RED_STAINED_GLASS_PANE, "&4Back", null, mainMenu), 0);
        menu.addItem(getUnlockItem(menu, NPCType.FARMER, area), 2);
        menu.addItem(getUnlockItem(menu, NPCType.SMITH, area), 3);
        menu.addItem(getUnlockItem(menu, NPCType.HEALER, area), 4);
        menu.addItem(getUnlockItem(menu, NPCType.ALCHEMIST, area), 5);
        menu.addItem(getUnlockItem(menu, NPCType.GUILD_REP, area), 6);
        menu.addItem(getUnlockItem(menu, NPCType.INFUSER, area), 12);
        menu.addItem(getUnlockItem(menu, NPCType.ADV_ITEM_SMITH, area), 13);
        menu.addItem(getUnlockItem(menu, NPCType.ENCHANTER, area), 14);
        return menu;
    }

    public MenuItem getUnlockItem(DarkMenu menu, NPCType t, PlayerArea area) {
        if (area.getArea(t).getLevel() > 0) {
            return new BasicItem(t.getMenuMaterial(), "&7" + t.getTitle(), "&8Already Unlocked", null);
        }
        else {
            return new MenuNavItem(t.getMenuMaterial(), "&a" + t.getTitle(), null, getUnlockMenu(menu, area, t));
        }
    }

    public DarkMenu getUnlockMenu(DarkMenu unlockNPCMenu, PlayerArea area, NPCType t) {
        DarkMenu menu = new DarkMenu(ChatColor.GREEN + "Unlock " + t.getTitle() + " NPC?");
        menu.addItem(new MenuNavItem(Material.RED_STAINED_GLASS_PANE, "&4Back", null, unlockNPCMenu), 0);
        menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aConfirm", null, () -> {
            area.unlockNPC(t);
            getUnlockNPCMenu(getManagerMenu(area), area).show(unlockNPCMenu.getPlayer());
        }), 5);
        menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cCancel", null, null), 3);
        return menu;
    }


    /* UPGRADE AREA MENU */
    public DarkMenu getUpgradeNPCMenu(DarkMenu mainMenu, PlayerArea area) {
        DarkMenu menu = new DarkMenu(ChatColor.LIGHT_PURPLE + "Upgrade NPCs");
        menu.addItem(new MenuNavItem(Material.RED_STAINED_GLASS_PANE, "&4Back", null, mainMenu), 0);
        menu.addItem(getUpgradeItem(menu, NPCType.FARMER, area), 2);
        menu.addItem(getUpgradeItem(menu, NPCType.SMITH, area), 3);
        menu.addItem(getUpgradeItem(menu, NPCType.HEALER, area), 4);
        menu.addItem(getUpgradeItem(menu, NPCType.ALCHEMIST, area), 5);
        menu.addItem(getUpgradeItem(menu, NPCType.GUILD_REP, area), 6);
        menu.addItem(getUpgradeItem(menu, NPCType.INFUSER, area), 12);
        menu.addItem(getUpgradeItem(menu, NPCType.ADV_ITEM_SMITH, area), 13);
        menu.addItem(getUpgradeItem(menu, NPCType.ENCHANTER, area), 14);
        return menu;
    }

    public MenuItem getUpgradeItem(DarkMenu upgradeMenu, NPCType t, PlayerArea area) {
        int currentLevel = area.getArea(t).getLevel();
        if (currentLevel <= 0) {
            return new BasicItem(t.getMenuMaterial(), "&c" + t.getTitle(), "&4You need to unlock &4this NPC first!", null);
        }
        else {
            return new MenuNavItem(t.getMenuMaterial(), "&a" + t.getTitle(), null, getUpgradeMenu(upgradeMenu, area, t, currentLevel));
        }
    }

    public DarkMenu getUpgradeMenu(DarkMenu upgradeMenu, PlayerArea area, NPCType t, int currentLevel) {
        DarkMenu menu = new DarkMenu(ChatColor.GREEN + "Upgrade to level " + (currentLevel + 1) + "?");
        menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aConfirm", null, () -> {
            boolean upgrade = (area.upgrade(t)); // shouldn't be able to upgrade an npc unless they've been unlocked.
            assert(upgrade);
            getUpgradeNPCMenu(getManagerMenu(area), area).show(upgradeMenu.getPlayer());
        }), 5);
        menu.addItem(new BasicItem(t.getMenuMaterial(), t.getTitle(), "&7Cost: " + (currentLevel * 1000) + " silver", null), 3);
        menu.addItem(new MenuNavItem(Material.RED_STAINED_GLASS_PANE, "&4Back", null, upgradeMenu), 0);
        return menu;
    }

    /* MANAGE AREA MENU */
    public DarkMenu getManageAreasMenu(DarkMenu mainMenu, PlayerArea area) {
        DarkMenu menu = new DarkMenu(ChatColor.DARK_PURPLE + "Manage Areas");
        for (int i = 0; i < 7; i++) {
            menu.addItem(getAreaItem(menu, i, area), i + 2);
        }
        menu.addItem(new BasicItem(Material.BLACK_STAINED_GLASS_PANE, "", null, null), 1);
        menu.addItem(new MenuNavItem(Material.RED_STAINED_GLASS_PANE, "&4Back", null, mainMenu), 0);
        return menu;
    }

    public MenuItem getAreaItem(DarkMenu previousMenu, int id, PlayerArea area) {
        if (area.getUnlockedAreas() < id) { return new BasicItem(Material.RED_STAINED_GLASS_PANE, "Area " + (id + 1), "&4You need to unlock &4this area first!", null); }
        if (!area.getCurrentAreas().containsKey(id) || area.getCurrentAreas().get(id) == null) {
            return new MenuNavItem(Material.GRAY_STAINED_GLASS_PANE, "Area " + (id + 1), "&8This area is empty!", getManageAreaMenu(previousMenu, null, area, id));
        }
        else {
            NPCType type = area.getCurrentAreas().get(id);
            return new MenuNavItem(type.getMenuMaterial(), "Area "+(id+1), "&7NPC: &5"+type.getTitle()+" #{newline} &7Level: &d"+area.getArea(type).getLevel(), getManageAreaMenu(previousMenu, area.getArea(type), area, id));
        }
    }

    /* SPECIFIC AREA MANAGEMENT MENU */
    public DarkMenu getManageAreaMenu(DarkMenu manageAreaMenu, @Nullable Area area, PlayerArea playerArea, int id) {
        DarkMenu menu;
        if (area == null || area.getType() == null) {
            menu = new DarkMenu("Managing area");
            menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cClear Area", "&4There is no NPC at &4this area!", null), 3);
            menu.addItem(new MenuNavItem(Material.GREEN_STAINED_GLASS_PANE, "&aAdd NPC", null, getAddNPCMenu(menu, id, playerArea)), 5);
        }
        else {
            menu = new DarkMenu("Manage " + area.getType().getTitle() + " Area");
            if (area.getType() != null) {
                menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cClear Area", null, () -> {
                    playerArea.clear(id);
                    getManageAreasMenu(getManagerMenu(playerArea), playerArea).show(manageAreaMenu.getPlayer());
                }), 3);
                // IDEA could just remove this?
                //menu.addItem(new BasicItem(Material.GRAY_STAINED_GLASS_PANE, "&7Change NPC", null, null), 4); // TODO change npc (select npc to add except remove old npc once selected)
                menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aAdd NPC", "&4There is already an &4NPC here!", null), 5);
            }
        }
        menu.addItem(new MenuNavItem(Material.RED_STAINED_GLASS_PANE, "&4Back", null, manageAreaMenu), 0);
        return menu;
    }

    /* ADD NPC MENU */
    public DarkMenu getAddNPCMenu(DarkMenu managerAreasMenu, int id, PlayerArea playerArea) {
        DarkMenu menu = new DarkMenu(ChatColor.GREEN + "Add NPC to Area");
        menu.addItem(new MenuNavItem(Material.RED_STAINED_GLASS_PANE, "&4Back", null, managerAreasMenu), 0);
        List<NPCType> list = playerArea.getAvailableNPCS();
        int pos = 1;
        for (NPCType type : list) {
            menu.addItem(new BasicItem(type.getMenuMaterial(), "&a" + type.getTitle(), "&7Level &d"+playerArea.getArea(type).getLevel(), () -> {
                playerArea.setArea(id, type);
                getManageAreasMenu(getManagerMenu(playerArea), playerArea).show(managerAreasMenu.getPlayer());
            }), pos++);
        }
        return menu;
    }

    /* NPC MANAGER MENU */
    public DarkMenu getManagerMenu(PlayerArea playerArea) {
        DarkMenu menu = new DarkMenu(ChatColor.RED + "Manage NPCs");
        menu.addItem(new MenuNavItem(Material.GOLD_NUGGET, "&6Unlock NPC", null, getUnlockNPCMenu(menu, playerArea)), 2);
        menu.addItem(new MenuNavItem(Material.IRON_BLOCK, "&fManage Areas", null, getManageAreasMenu(menu, playerArea)), 4);
        menu.addItem(new MenuNavItem(Material.GOLD_INGOT, "&dUpgrade NPCs", null, getUpgradeNPCMenu(menu, playerArea)), 6);
        return menu;
    }
}
