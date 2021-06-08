package dev.forbit.library.group;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The Different MobGroups present on DarkDungeon
 */
public enum MobGroup {
    ZOMBIE("AdvDrowned", "AdvZombie", "BasicDrowned", "BasicHusk", "BasicZombie", "CorruptedZombie", "EnlightenedZombie", "TurnipZombie", "GoldZombie", "JungleMinion"),
    SKELETON("AdvArcher", "AdvSkeleton", "ArcherMinion", "BasicSkeleton"),
    SPIDER("AdvSpider", "BasicSpider"),
    SLIME("AdvSlime", "BasicSlime", "SlimeMinion"),
    POLAR_BEAR("BasicPolarBear", "BearMinion"),
    WOLF("BasicWolf"),
    TURNIP("TurnipZombie"),
    ENDGAME("AdvArcher", "AdvDrowned", "AdvSkeleton", "AdvSlime", "AdvSpider", "AdvZombie"),
    ENLIGHTENED("EnlightenedZombie"),
    CORRUPTED("CorruptedZombie"),
    BOSS("Boss_Cold", "Boss_Collapsing", "Boss_Hollow", "Boss_Jungle", "Boss_Maze", "Boss_Parkour", "Boss_Slime", "Boss_Spiral", "Boss_Tree"),
    BASIC("BasicZombie", "BasicSkeleton", "BasicSpider", "BasicSlime", "BasicWolf");

    @Getter @Setter private List<String> mobNames;

    MobGroup(String... mobs) {
        ArrayList<String> ids = new ArrayList<>();
        Collections.addAll(ids, mobs);
        setMobNames(ids);
    }

    /**
     * Gets a list of the groups that a mob is in.
     * @param mobName
     * @return
     */
    public static List<MobGroup> getGroups(String mobName) {
        ArrayList<MobGroup> list = new ArrayList<MobGroup>();
        for (MobGroup g : MobGroup.values()) {
            for (String s : g.getMobNames()) {
                if (s.equalsIgnoreCase(mobName)) {
                    if (!(list.contains(g))) {
                        list.add(g);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Checks to see if a mob is in a specified group
     * @param s mob name
     * @param g group
     * @return
     */
    public static boolean isInGroup(String s, MobGroup g) {
        for (String name : g.getMobNames()) {
            if (name.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public String getRandomMob() {
        Random random = new Random();
        return getMobNames().get(random.nextInt(getMobNames().size()));
    }
}
