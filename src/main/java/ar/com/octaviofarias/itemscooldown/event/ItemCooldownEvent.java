package ar.com.octaviofarias.itemscooldown.event;

import ar.com.octaviofarias.itemscooldown.model.CooldownData;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class ItemCooldownEvent extends Event implements Cancellable {

    public static HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack item;
    private final Material material;
    private final CooldownData cooldownData;

    private boolean cancelled = false;

    public ItemCooldownEvent(Player player, ItemStack item, Material material, CooldownData cooldownData) {
        this.player = player;
        this.item = item;
        this.material = material;
        this.cooldownData = cooldownData;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
