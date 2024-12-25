package ar.com.octaviofarias.itemscooldown.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

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
        return cooldowns.stream()
                .filter(cd -> cd.getMaterial() == material && !cd.isFinished())
                .findFirst()
                .map(CooldownData::getRemainingTime)
                .orElse(0) ;
    }

}
