package ar.com.octaviofarias.itemscooldown.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public class CooldownData {
    private final Material material;
    private int remainingTime;

    public boolean isFinished() {
        return remainingTime <= 0;
    }

    public void reduceTime(int elapsedTime) {
        this.remainingTime -= elapsedTime;
    }
}
