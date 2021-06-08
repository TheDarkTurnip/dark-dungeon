package dev.forbit.library.player;

import dev.forbit.library.DarkLib;
import dev.forbit.library.DarkWorld;
import jdk.internal.jline.internal.Nullable;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Class to interact with DarkPlayers
 */
public class PlayerAPI {
    private DarkLib lib;

    public PlayerAPI(DarkLib lib) {
        this.lib = lib;
    }

    /**
     * Gets the DarkPlayer instance
     *
     * @param id
     *
     * @return
     */
    @Nullable public DarkPlayer getPlayer(UUID id) {
        for (DarkPlayer player : lib.getPlayers()) {
            if (player.getId().equals(id)) { return player; }
        }
        return null;
    }

    /**
     * Will get a DarkPlayer, or create a new one if there's not one already.
     *
     * @param id
     *
     * @return
     */
    public DarkPlayer getPlayerOrCreate(UUID id) {
        if (getPlayer(id) == null) {
            return newPlayer(id);
        }
        else {
            return getPlayer(id);
        }
    }

    /**
     * Creates a new Player instance
     *
     * @param id
     *
     * @return
     */
    private DarkPlayer newPlayer(UUID id) {
        DarkPlayer player = new DarkPlayer();
        player.setId(id);
        player.setFirstJoined(Date.from(Instant.now()));
        lib.getPlayers().add(player);
        return player;
    }

    public void printData(OfflinePlayer data, CommandSender printee) {
        DarkPlayer player = getPlayer(data.getUniqueId());
        if (player == null) {
            printee.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Player data: &cnull."));
            return;
        }
        (new ArrayList<String>() {
            {
                add("&8--- &7Data for: &5" + player.getName() + "&8 ---");
                add("&8- &7UUID: &b" + player.getId());
                add("&8- &7First Joined: &b" + player.getFirstJoined().toString());
                add("&8- &7Last Address: &b" + (player.getLastIP() == null ? "null": player.getLastIP().toString()));
                if (player.isOnline()) {
                    add("&8- &7Last Seen: &3Online");
                }
                else {
                    add("&8- &7Last Seen: &b" + player.getLastSeenString());
                }
                add("&8- &7Total Time Played: &b" + player.getTotalTimePlayed() + " &3minutes");
                add("&8- &7Monthly Time Played: &b" + player.getMonthlyTimePlayed() + " &3minutes");
                add("&8- &7Weekly Time Played: &b" + player.getWeeklyTimePlayed() + "&3 minutes");
                add("&8- &7Alpha: &c" + player.isAlpha() + " &7Beta: &b" + player.isBeta() + " &7Discord: &d" + player.isDiscord());
                add("&8- &7Donation Amount: &6$" + player.getDonationAmount());
                add("&8- &7XP: &b" + player.getXp());
                add("&8- &7Silver: &f" + player.getCurrencies().get(Currency.SILVER) + " &7Gold: &6" + player.getCurrencies().get(Currency.GOLD) + " &7Gems: &a" + player.getCurrencies().get(Currency.GEM));
                add("&8- &7Longest Arena Wave: &b" + player.getLongestArenaWave());
            }
        }).forEach((s) -> {
            printee.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        });

    }

    /* ####################################### *\
       #           Applications of           #
       #              PlayerAPI              #
    \* ####################################### */

    public int getKeys(@NonNull OfflinePlayer player, @NonNull DarkWorld world) {
        return getKeys(player.getUniqueId(), world);
    }

    public int getKeys(@NonNull UUID id, @NonNull DarkWorld world) {
        DarkPlayer player = this.getPlayerOrCreate(id);
        return player.getKeys().getOrDefault(world, 0);
    }

    public void addKeys(@NonNull OfflinePlayer player, @NonNull DarkWorld world, int amount) {
        addKeys(player.getUniqueId(), world, amount);
    }

    public void addKeys(@NonNull UUID id, @NonNull DarkWorld world, int amount) {
        DarkPlayer player = this.getPlayerOrCreate(id);
        int keys = getKeys(id, world);
        player.getKeys().put(world, (Math.max(keys, 0)) + amount); // in case for some reason we have negative keys?? impossible btw.
    }

    public boolean withdrawKeys(@NonNull OfflinePlayer player, @NonNull DarkWorld world, int amount) {
        if (getKeys(player, world) < amount) { return false; }
        return withdrawKeys(player.getUniqueId(), world, amount);
    }

    public boolean withdrawKeys(@NonNull UUID id, @NonNull DarkWorld world, int amount) {
        if (getKeys(id, world) < amount) { return false; }
        int keys = getKeys(id, world);
        this.getPlayerOrCreate(id).getKeys().put(world, keys - amount);
        return true;
    }

    public double getCurrency(@NonNull UUID id, @NonNull Currency currency) {
        DarkPlayer player = this.getPlayerOrCreate(id);
        if (!player.getCurrencies().containsKey(currency)) {
            player.getCurrencies().put(currency, 0.0);
        }
        return player.getCurrencies().get(currency);
    }
    
    public void setCurrency(@NonNull UUID id, @NonNull Currency currency, double amount) {
        this.getPlayerOrCreate(id).getCurrencies().put(currency, amount);
    }

    public void addCurrency(@NonNull UUID id, @NonNull Currency currency, double amount) {
        setCurrency(id, currency, getCurrency(id, currency) + amount);
    }


    public int getXP(@NonNull UUID id) {
        return this.getPlayerOrCreate(id).getXp();
    }

    public void setXP(@NonNull UUID id, int amount) {
        this.getPlayerOrCreate(id).setXp(amount);
    }

    public void addXP(@NonNull UUID id, int amount) {
        setXP(id, getXP(id) + amount);
    }

}
