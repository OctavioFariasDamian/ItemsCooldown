package ar.com.octaviofarias.itemscooldown.managers;

import ar.com.octaviofarias.itemscooldown.ItemsCooldown;
import ar.com.octaviofarias.itemscooldown.model.CooldownData;
import ar.com.octaviofarias.itemscooldown.model.UserData;
import org.bukkit.Material;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
public class DataManager {
    private JSONObject json;
    private File file;

    public void init() {
        try {
            file = new File(ItemsCooldown.inst().getDataFolder(), "data.json");
            if (!file.exists()) ItemsCooldown.inst().saveResource("data.json", false);
            json = new JSONObject(Files.readString(file.toPath()));
        } catch (IOException e) {
            ItemsCooldown.inst().getLogger().severe("An exception occurred while reading data.json file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadUser(UserData userData) {
        if (json.has(userData.getUuid().toString())) {
            try {
                List<CooldownData> cooldowns = new ArrayList<>();
                JSONObject data = json.getJSONObject(userData.getUuid().toString());
                data.keySet().forEach(key -> {
                    long remainingTimeInSeconds = data.getLong(key);
                    cooldowns.add(new CooldownData(Material.matchMaterial(key), remainingTimeInSeconds * 1000));
                });
                userData.setCooldowns(cooldowns);
            } catch (Exception e) {
                ItemsCooldown.inst().getLogger().warning("Can't read data for user " + userData.getName() + ". Check data.json!");
                e.printStackTrace();
            }
        }
    }

    public void saveUserData(UserData userData) {
        try {
            JSONObject userObject = new JSONObject();
            for (CooldownData cooldown : userData.getCooldowns()) {
                if (!cooldown.isFinished()) {
                    userObject.put(cooldown.getMaterial().name(), cooldown.getRemainingTime() / 1000);
                }
            }
            json.put(userData.getUuid().toString(), userObject);

            Files.write(file.toPath(), json.toString(4).getBytes());
        } catch (IOException e) {
            ItemsCooldown.inst().getLogger().severe("An exception occurred while saving data.json file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveAllUsers(List<UserData> users) {
        users.forEach(this::saveUserData);
    }
}
