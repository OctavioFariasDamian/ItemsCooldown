package ar.com.octaviofarias.itemscooldown.managers;

import ar.com.octaviofarias.itemscooldown.CooldownCommand;
import ar.com.octaviofarias.itemscooldown.ItemsCooldown;
import ar.com.octaviofarias.itemscooldown.event.ItemCooldownEvent;
import ar.com.octaviofarias.itemscooldown.model.CooldownData;
import ar.com.octaviofarias.itemscooldown.model.UserData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CooldownManager implements Listener {
    private final List<UserData> users = new ArrayList<>();

    public UserData getUserData(String name) {
        return users.stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void add(Player player, Material material, int seconds) {
        UserData user = getUserData(player.getName());
        if (user != null) {
            user.getCooldowns().add(new CooldownData(material, seconds));
        }
    }

    public int getTimeLeft(Player player, Material material) {
        UserData user = getUserData(player.getName());
        if (user != null && user.has(material)) {
            return user.getTimeLeft(material);
        }
        return -1;
    }

    public boolean has(Player player, Material material) {
        UserData user = getUserData(player.getName());
        return user != null && user.has(material);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getMaterial() == Material.AIR) return;

        Player player = event.getPlayer();
        Material material = event.getItem().getType();

        if (ItemsCooldown.getCooldownManager().has(player, material)) {
            ItemCooldownEvent event1 = new ItemCooldownEvent(player, event.getItem(), material, new CooldownData(material, ItemsCooldown.getCooldownManager().getTimeLeft(player, material)));

            Bukkit.getPluginManager().callEvent(event1);

            if(event1.isCancelled()) return;

            event.setCancelled(true);

            long timeLeft = getTimeLeft(player, material);

            CooldownCommand.sendMessage(
                    player,
                    "&cYou can't " +
                            (material.isBlock() ? "place" : "interact") +
                            " this " +
                            (material.isBlock() ? "block" : "item") +
                            " for " + timeLeft + " seconds!"
            );
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UserData user = getUserData(event.getPlayer().getName());
        if (user == null) {
            user = new UserData(new ArrayList<>(), event.getPlayer().getUniqueId(), event.getPlayer().getName());
            ItemsCooldown.getDataManager().loadUser(user);
            users.add(user);
        }
    }
}
