package dev.forbit.library.player;

import dev.forbit.library.DarkWorld;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public @Data class DarkPlayer {
    // UUID
    UUID id = null;

    // world based data
    HashMap<DarkWorld, DarkInventory> inventories = new HashMap<>();
    HashMap<DarkWorld, Integer> keys = new HashMap<>();
    HashMap<Currency, Double> currencies = new HashMap<>();

    // global data
    int xp = 0;

    // tags
    boolean alpha = false;
    boolean beta = false;
    boolean discord = false;

    // money $$$$
    double donationAmount = 0.0d;

    // shenanigans
    int longestArenaWave = 0;

    // time tracker
    long totalTimePlayed = 0;
    long monthlyTimePlayed = 0;
    long weeklyTimePlayed = 0;

    // trackers
    InetSocketAddress lastIP = null;
    long lastSeen = 0;
    Date firstJoined = null;

    // TODO talisman Inventory.

    public String getName() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(getId());
        return player.getName();
    }

    public boolean isOnline() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(getId());
        return player.isOnline();
    }

    public String getLastSeenString() {
        long difference = System.currentTimeMillis() - getLastSeen();
        long second = (difference / 1000) % 60;
        long minute = (difference / (1000 * 60)) % 60;
        long hour = (difference / (1000 * 60 * 60)) % 24;
        long days = (difference / (1000*60*60*24));
        return days+"d "+hour+"h "+minute+"m "+second+"s";
    }
}
