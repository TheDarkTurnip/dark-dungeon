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
        getMain().getPlayerAreas().put(id, area);
    }

    public PlayerArea getArea(OfflinePlayer oPlayer) {
        if (!getMain().getPlayerAreas().containsKey(oPlayer.getUniqueId())) {
            getMain().getPlayerAreas().put(oPlayer.getUniqueId(), new PlayerArea());
        }

        return getMain().getPlayerAreas().get(oPlayer.getUniqueId());
    }


    /* UNLOCK NPC MENU */
    public DarkMenu getUnlockNPCMenu(PlayerArea area) {
        DarkMenu menu = new DarkMenu(ChatColor.LIGHT_PURPLE + "Unlock NPCs");
        menu.addItem(getUnlockItem(NPCType.FARMER, area), 2);
        menu.addItem(getUnlockItem(NPCType.SMITH, area), 3);
        menu.addItem(getUnlockItem(NPCType.HEALER, area), 4);
        menu.addItem(getUnlockItem(NPCType.ALCHEMIST, area), 5);
        menu.addItem(getUnlockItem(NPCType.GUILD_REP, area), 6);
        menu.addItem(getUnlockItem(NPCType.INFUSER, area), 12);
        menu.addItem(getUnlockItem(NPCType.ADV_ITEM_SMITH, area), 13);
        menu.addItem(getUnlockItem(NPCType.ENCHANTER, area), 14);
        return menu;
    }

    public MenuItem getUnlockItem(NPCType t, PlayerArea area) {
        if (area.getArea(t).getLevel() > 0) {
            return new BasicItem(t.getMenuMaterial(), "&7" + t.getTitle(), "&8Already Unlocked", null);
        } else {
            return new MenuNavItem(t.getMenuMaterial(), "&a" + t.getTitle(), null, getUnlockMenu(t));
        }
    }

    public DarkMenu getUnlockMenu(NPCType t) {
        DarkMenu menu = new DarkMenu(ChatColor.GREEN+"Unlock "+t.getTitle()+" NPC?");
        menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aConfirm", null, null), 2);
        menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cCancel", null, null), 6);
        return menu;
    }


    /* UPGRADE AREA MENU */
    public DarkMenu getUpgradeNPCMenu(PlayerArea area) {
        DarkMenu menu = new DarkMenu(ChatColor.LIGHT_PURPLE + "Upgrade NPCs");
        menu.addItem(getUpgradeItem(NPCType.FARMER, area), 2);
        menu.addItem(getUpgradeItem(NPCType.SMITH, area), 3);
        menu.addItem(getUpgradeItem(NPCType.HEALER, area), 4);
        menu.addItem(getUpgradeItem(NPCType.ALCHEMIST, area), 5);
        menu.addItem(getUpgradeItem(NPCType.GUILD_REP, area), 6);
        menu.addItem(getUpgradeItem(NPCType.INFUSER, area), 12);
        menu.addItem(getUpgradeItem(NPCType.ADV_ITEM_SMITH, area), 13);
        menu.addItem(getUpgradeItem(NPCType.ENCHANTER, area), 14);
        return menu;
    }

    public MenuItem getUpgradeItem(NPCType t, PlayerArea area ) {
        int currentLevel = area.getArea(t).getLevel();
        if (currentLevel <= 0) {
            return new BasicItem(t.getMenuMaterial(), "&c" + t.getTitle(), "&4You need to unlock &4this NPC first!", null);
        } else {
            return new MenuNavItem(t.getMenuMaterial(), "&a" + t.getTitle(), null, getUpgradeMenu(t, currentLevel));
        }
    }

    public DarkMenu getUpgradeMenu(NPCType t, int currentLevel) {
        DarkMenu menu = new DarkMenu(ChatColor.GREEN+"Upgrade "+t.getTitle()+" to level "+(currentLevel+1)+"?");
        menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aConfirm", null, null), 2);
        menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cCancel", null, null), 6);
        menu.addItem(new BasicItem(Material.GRAY_STAINED_GLASS_PANE, "&7Cost", "&7"+(currentLevel*1000)+" silver", null), 4);
        return menu;
    }

    /* MANAGE AREA MENU */
    public DarkMenu getManageAreasMenu(PlayerArea area) {
        DarkMenu menu = new DarkMenu(ChatColor.DARK_PURPLE+"Manage Areas");
        for (int i = 0; i < 7; i++) {
            menu.addItem(getAreaItem(i, area), i+1);
        }
        menu.addItem(new BasicItem(Material.BLACK_STAINED_GLASS_PANE, "", null, null), 0);
        menu.addItem(new BasicItem(Material.BLACK_STAINED_GLASS_PANE, "", null, null), 8);
        return menu;
    }

    public MenuItem getAreaItem(int id, PlayerArea area) {
        if (area.getUnlockedAreas() < id) return new BasicItem(Material.RED_STAINED_GLASS_PANE, "Area "+(id+1), "&4You need to unlock &4this area first!", null);
        if (area.getCurrentAreas().containsKey(id) || area.getCurrentAreas().get(id) == null) {
            return new MenuNavItem(Material.GRAY_STAINED_GLASS_PANE, "Area "+(id+1), "&8This area is empty!", getManageAreaMenu(null, area));
        } else {
            NPCType type = area.getCurrentAreas().get(id);
            return new MenuNavItem(type.getMenuMaterial(), type.getTitle(), null, getManageAreaMenu(area.getArea(type), area));
        }
    }

    /* SPECIFIC AREA MANAGEMENT MENU */
    public DarkMenu getManageAreaMenu(@Nullable Area area, PlayerArea playerArea) {
        DarkMenu menu;
        if (area == null) {
            menu = new DarkMenu("Managing area");
            menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cClear Area", "&4There is no NPC at &4this area!", null), 2);
            menu.addItem(new BasicItem(Material.GRAY_STAINED_GLASS_PANE, "&7Change NPC", "&4There is no NPC at &4this area!", null), 4);
            menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aAdd NPC", null, null), 6);
        } else {
            menu = new DarkMenu("Manage " + area.getType().getTitle() + " Area");
            if (area.getType() != null) {
                menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cClear Area", null, null), 2);
                menu.addItem(new BasicItem(Material.GRAY_STAINED_GLASS_PANE, "&7Change NPC", null, null), 4);
                menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aAdd NPC", "&4There is already &4an NPC here!", null), 6);
            }
            else {
                menu.addItem(new BasicItem(Material.RED_STAINED_GLASS_PANE, "&cClear Area", "&4There is no NPC at &4this area!", null), 2);
                menu.addItem(new BasicItem(Material.GRAY_STAINED_GLASS_PANE, "&7Change NPC", "&4There is no NPC at &4this area!", null), 4);
                menu.addItem(new BasicItem(Material.GREEN_STAINED_GLASS_PANE, "&aAdd NPC", null, null), 6);
            }
        }
        return menu;
    }

    /* NPC MANAGER MENU */
    public DarkMenu getManagerMenu(PlayerArea playerArea) {
        DarkMenu menu = new DarkMenu(ChatColor.RED+"Manage NPCs");
        menu.addItem(new MenuNavItem(Material.GOLD_NUGGET, "&6Unlock NPC", null, getUnlockNPCMenu(playerArea)), 2);
        menu.addItem(new MenuNavItem(Material.IRON_BLOCK, "&fManage Areas", null, getManageAreasMenu(playerArea)), 4);
        menu.addItem(new MenuNavItem(Material.GOLD_INGOT, "&dUpgrade NPCs", null, getUpgradeNPCMenu(playerArea)), 6);
        return menu;
    }
}
