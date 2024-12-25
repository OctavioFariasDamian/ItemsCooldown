package ar.com.octaviofarias.itemscooldown.tasks;

import ar.com.octaviofarias.itemscooldown.ItemsCooldown;
import ar.com.octaviofarias.itemscooldown.model.CooldownData;
import ar.com.octaviofarias.itemscooldown.model.UserData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class CooldownTask extends BukkitRunnable {

    @Override
    public void run() {
        for (UserData user : ItemsCooldown.getCooldownManager().getUsers()) {
            if (user.getCooldowns() == null || user.getCooldowns().isEmpty()) continue;

            Iterator<CooldownData> iterator = user.getCooldowns().iterator();
            while (iterator.hasNext()) {
                CooldownData cooldown = iterator.next();
                if (cooldown.isFinished()) {
                    iterator.remove();
                    ItemsCooldown.getDataManager().saveUserData(user);
                } else {
                    cooldown.reduceTime(1);
                }
            }
        }
    }
}