package ar.com.octaviofarias.itemscooldown.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@AllArgsConstructor
@Getter
@Setter
public class UserData {

    private List<CooldownData> cooldowns;
    private final UUID uuid;
    private final String name;

    public boolean has(Material material) {
        return cooldowns.stream().anyMatch(cd -> cd.getMaterial() == material && !cd.isFinished());
    }

    public int getTimeLeft(Material material) {
        return (int) (cooldowns.stream()
                .filter(cd -> cd.getMaterial() == material && !cd.isFinished())
                .findFirst()
                .map(CooldownData::getRemainingTime)
                .orElse(0L) / 1000);
    }

    public boolean isFinished(Material material){
        return cooldowns.stream().filter(cooldownData -> cooldownData.getMaterial() == material).findFirst().get().isFinished();
    }
}
