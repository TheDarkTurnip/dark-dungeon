package dev.forbit.loot;

import dev.forbit.library.DarkLib;
import dev.forbit.library.DarkWorld;
import dev.forbit.library.player.Currency;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Random;

public enum LootDrop {
    SILVER(20), GOLD(3), GEM(2), ITEM(10), EXP(10), KEY(6);

    @Getter @Setter int weight;

    LootDrop(int w) {
        setWeight(w);
    }

    public static LootDrop getRandom() throws Exception {
        // first calculate the sum of all weights for each drop
        int sumOfWeights = 0;
        for (LootDrop d : LootDrop.values()) {
            sumOfWeights += d.getWeight();
        }
        Random random = new Random();
        // generate a random number that exists within the range of 0 to the sum of weights.
        int n = random.nextInt(sumOfWeights);

        // loop through each drop until we get to the random drop.
        for (LootDrop d : LootDrop.values()) {
            n -= d.getWeight();
            if (n <= 0) {
                return d;
            }
        }
        throw new Exception("Overflow error in LootDrop.getRandom()");
    }

    public static LootDrop getDrop(Player player) {
        if (DarkLib.getPlayerAPI().getKeys(player, DarkWorld.DUNGEON) < 1) {
            return LootDrop.KEY;
        } else {
            try {
                return LootDrop.getRandom();
            } catch (Exception e) {
                return LootDrop.SILVER;
                // ignore exception and just give default exception
            }
        }
    }

    /**
     * Awards the loot drop to player
     * @param player
     * @return The message to send to the player
     */
    public String award(OfflinePlayer player) {
        // give the award to a player
        switch (this) {
            case SILVER:
                // add silver to player
                DarkLib.getPlayerAPI().addCurrency(player.getUniqueId(), Currency.SILVER, 100.0);
                return "+100 Silver";
            case GOLD:
                // add gold to player
                DarkLib.getPlayerAPI().addCurrency(player.getUniqueId(), Currency.GOLD, 2.0);
                return "+2 Gold";
            case GEM:
                // add gem to player
                DarkLib.getPlayerAPI().addCurrency(player.getUniqueId(), Currency.GEM, 1.0);
                return "+1 Gem";
            case ITEM:
                // drop an item
                break;
            case EXP:
                // give player some xp
                DarkLib.getPlayerAPI().addXP(player.getUniqueId(), 5);
                return "+5 XP";
            case KEY:
                // give player a key
                // TODO get the world that the drop is dropped from
                DarkLib.getPlayerAPI().addKeys(player, DarkWorld.DUNGEON, 1);
                return "+1 Key";
            default: return "null";
        }
        return "nothing";
    }
}
