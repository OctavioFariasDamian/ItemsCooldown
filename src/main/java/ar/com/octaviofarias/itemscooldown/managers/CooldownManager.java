package ar.com.octaviofarias.itemscooldown.managers;

import ar.com.octaviofarias.itemscooldown.CooldownCommand;
import ar.com.octaviofarias.itemscooldown.ItemsCooldown;
import ar.com.octaviofarias.itemscooldown.model.CooldownData;
import ar.com.octaviofarias.itemscooldown.model.UserData;
import lombok.Getter;
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

    public void add(Player player, Material material, long seconds) {
        UserData user = getUserData(player.getName());
        if (user != null) {
            user.getCooldowns().add(new CooldownData(material, seconds * 1000));
        }
    }

    public long getTimeLeft(Player player, Material material) {
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

    public void adjustCooldowns(long elapsedTime) {
        for (UserData user : users) {
            for (CooldownData cooldown : user.getCooldowns()) {
                cooldown.reduceTime(elapsedTime);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getMaterial() == Material.AIR) return;

        Player player = event.getPlayer();
        Material material = event.getItem().getType();

        if (ItemsCooldown.getCooldownManager().has(player, material)) {
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
