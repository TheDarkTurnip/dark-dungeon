package dev.forbit.library;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class Utils {
    public static final double leaniency = 1.0;
    // ROMAN NUMERALS
    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String namify(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1, string.length()).toLowerCase();
    }

    public static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }

    /**
     * Sends the hurt entity packet
     */
    public static void sendHurtPacket(Entity entity) {
        entity.playEffect(EntityEffect.HURT);
    }

    @SuppressWarnings("deprecation") public static void sendDeathPacket(LivingEntity entity) {
        entity.playEffect(EntityEffect.DEATH);
    }

    public static HashMap<DamageType, Double> getBlankDamageMap() {
        HashMap<DamageType, Double> map = new HashMap<>();
        for (DamageType type : DamageType.values()) {
            map.put(type, 0.0);
        }
        return map;
    }

    /**
     * Gets wether the entity is behind another entity
     *
     * @param origin the mob to check
     * @param target the entity that is behind
     */
    public static boolean isBehind(LivingEntity origin, LivingEntity target) {
        Location lo = origin.getEyeLocation();
        Location o = target.getEyeLocation();
        /*if (target instanceof Player) { // En is the other entity
            o = ((Player) target).getEyeLocation();
        }
        else {
            o = target.getLocation();
        }*/
        Vector c = lo.toVector().subtract(o.toVector()); // Get vector between
        // you and other
        Vector d = origin.getEyeLocation().getDirection(); // Get direction you
        // are looking at
        double delta = c.dot(d);
        return delta > 0;
    }

    /**
     * Gets the nearest living entity within the max range returns null if none
     * found.
     *
     * @param range
     */
    public static LivingEntity getNearestLivingEntity(LivingEntity livingEntity, Vector direction, double range) {
        Location start = livingEntity.getEyeLocation();
        LivingEntity closest = null;
        double closestDistance = range * 2;
        // first, get locations between start and end
        Location end = livingEntity.getEyeLocation().add((livingEntity.getEyeLocation().getDirection()).multiply(range));
        List<Location> locations = getLocationsBetween(livingEntity.getWorld(), start, end, range * 2);
        for (Location l : locations) {
            for (Entity e : livingEntity.getNearbyEntities(range + 2, range + 3, range + 2)) {
                if (e instanceof LivingEntity) {
                    LivingEntity close = (LivingEntity) e;
                    double lean = Math.min(close.getLocation().distance(l), close.getEyeLocation().distance(l));
                    double dist = Math.min(close.getLocation().distance(livingEntity.getLocation()), close.getEyeLocation().distance(livingEntity.getLocation()));
                    if (lean < leaniency && dist < closestDistance) {
                        closestDistance = dist;
                        closest = close;
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Gets the locations between the start point and end point
     *
     * @param world       the world of the points
     * @param start       the start point
     * @param end         the end point
     * @param numOfPoints the number of points to check from
     *
     * @return a list of locations, the size will be equal to numOfPoints...
     * maybe?
     */
    public static List<Location> getLocationsBetween(World world, Location start, Location end, double numOfPoints) {
        List<Location> locations = new ArrayList<Location>();
        double distance = start.distance(end);
        double velocity = distance / (numOfPoints * numOfPoints);
        Vector a = start.toVector();
        Vector b = end.toVector();
        Vector d = (new Vector().copy(a)).subtract(new Vector().copy(b));
        for (float i = 0; i < 1.0; i += velocity) {
            Vector p = new Vector();
            p = (new Vector().copy(a).subtract(new Vector().copy(d).multiply(i)));
            locations.add(p.toLocation(world));
        }
        return locations;
    }

    public static void knockback(Entity attacker, Entity attacked, float knockback) {
        if (attacker == null) { return; }
        attacked.setVelocity(attacked.getVelocity().add(attacked.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize().multiply(knockback)));
    }

    public static Location getMidpoint(Location loc1, Location loc2) {
        double x = (loc1.getX() + loc2.getX()) / 2.0;
        double y = (loc1.getY() + loc2.getY()) / 2.0;
        double z = (loc1.getZ() + loc2.getZ()) / 2.0;
        return new Location(loc1.getWorld(), x, y, z);
    }

    public static double round(double n) {
        return Math.floor(n * 100.0) / 100.0;
    }

    public static String toRoman(int number) {
        if (number == 0) { return "0"; }
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static HashMap<DamageType, Double> getDamageList(DamageType normal, double damage) {
        HashMap<DamageType, Double> map = getBlankDamageMap();
        map.put(normal, damage);
        return map;
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public static double calculateArmor(double damage, double armor, double piercingValue) {
        double piercedArmor = armor - (piercingValue * armor);
        // System.out.println("original armor: "+armor+" pierced armor:
        // "+piercedArmor);
        // piercing value: "+piercingValue);
        return damage * Math.pow(0.97, piercedArmor);
    }

    /**
     * Splits a string into multiple lines.
     * Use #{newline} with spaces on either side to indicate a new line.
     * @param string
     * @param length
     * @return
     */
    public static List<String> splitString(String string, int length) {
        String[] words = string.split(" ");
        int currentLength = 0;
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        for (String s : words) {
            if (s.equals("#{newline}")) {
                list.add(builder.toString().trim());
                builder = new StringBuilder();
                currentLength = 0;
                continue;
            }
            String str = ChatColor.translateAlternateColorCodes('&', s);
            currentLength += ChatColor.stripColor(str).length();
            if (currentLength > length) {
                list.add(builder.toString().trim());
                builder = new StringBuilder();
                currentLength = ChatColor.stripColor(str).length();
            }
            builder.append(str).append(" ");
        }
        list.add(builder.toString().trim());
        return list;
    }

    public static List<Player> getNearbyPlayers(Location location) {
        List<Player> players = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().distanceSquared(location) < 500) {
                players.add(p);
            }
        }
        return players;
    }
    public static void playSound(Player player, Sound sound, float pitch) {
        Random rand = new Random();
        switch (rand.nextInt(2)) {
            case 1:
                player.playSound(player.getLocation(), sound, 1f, pitch - 0.1f);
                break;
            case 2:
                player.playSound(player.getLocation(), sound, 1f, pitch + 0.1f);
                break;
            default:
                player.playSound(player.getLocation(), sound, 1f, pitch);
                break;

        }
    }

}
