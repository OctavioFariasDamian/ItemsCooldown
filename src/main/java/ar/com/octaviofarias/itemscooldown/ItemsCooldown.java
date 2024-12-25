package ar.com.octaviofarias.itemscooldown;

import ar.com.octaviofarias.itemscooldown.managers.CooldownManager;
import ar.com.octaviofarias.itemscooldown.managers.DataManager;
import ar.com.octaviofarias.itemscooldown.tasks.CooldownTask;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;


public class ItemsCooldown extends JavaPlugin {
    @Getter
    private static DataManager dataManager;
    @Getter
    private static CooldownManager cooldownManager;
    private static ItemsCooldown instance;
    private CooldownTask tasK;

    public static ItemsCooldown inst() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        dataManager = new DataManager();
        cooldownManager = new CooldownManager();

        Objects.requireNonNull(getCommand("cooldown")).setExecutor(new CooldownCommand());
        Objects.requireNonNull(getCommand("cooldown")).setTabCompleter(new CooldownCommand());
        getServer().getPluginManager().registerEvents(cooldownManager, this);

        dataManager.init();

        tasK = new CooldownTask();
        tasK.runTaskTimerAsynchronously(this, 0L, 20L);
    }

    public void onDisable() {
        if(tasK.isCancelled()) return;
        tasK.cancel();
        dataManager.saveAllUsers();
    }

}