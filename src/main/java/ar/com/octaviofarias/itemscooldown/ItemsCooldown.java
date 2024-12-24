package ar.com.octaviofarias.itemscooldown;

import ar.com.octaviofarias.itemscooldown.managers.CooldownManager;
import ar.com.octaviofarias.itemscooldown.managers.DataManager;
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

        long lastSavedTime = loadLastSavedTime();
        long elapsedTime = System.currentTimeMillis() - lastSavedTime;
        cooldownManager.adjustCooldowns(elapsedTime);
    }

    public void onDisable() {
        long currentTime = System.currentTimeMillis();
        dataManager.saveAllUsers(cooldownManager.getUsers());
        saveLastSavedTime(currentTime);
    }

    private long loadLastSavedTime() {
        try {
            File timeFile = new File(getDataFolder(), "lastSavedTime.txt");
            if (timeFile.exists()) {
                return Long.parseLong(Files.readString(timeFile.toPath()));
            }
        } catch (IOException e) {
            getLogger().warning("Could not load last saved time: " + e.getMessage());
        }
        return System.currentTimeMillis();
    }

    private void saveLastSavedTime(long currentTime) {
        try {
            File timeFile = new File(getDataFolder(), "lastSavedTime.txt");
            Files.writeString(timeFile.toPath(), String.valueOf(currentTime));
        } catch (IOException e) {
            getLogger().severe("Could not save last saved time: " + e.getMessage());
        }
    }
}