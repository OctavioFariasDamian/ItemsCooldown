# ItemsCooldown

### API-VERSION: 1.21
### Tested Server Version: Pufferfish 1.21.3

### Commands
 - /cooldown \<player> \<material> \<seconds>

### Data Storage

Each Cooldown of every user is saved in a JSON file, it is save when server is restarted, a cooldown ends or when a cooldown is created.

### Developer API Usage

### 1. Add the dependency to your project:

`Maven`:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.OctavioFariasDamian</groupId>
        <artifactId>ItemsCooldown</artifactId>
        <version>1.0-FINAL</version>
    </dependency>
</dependencies>
```

`Gradle`:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.OctavioFariasDamian:ItemsCooldown:1.0-FINAL'
}
```

### 2. Usage

**ItemCooldown Event usage**:

```java
@EventHandler
public void onCooldown(ItemCooldownEvent event) {
    Player player = event.getPlayer();
    if (player.isOp() && event.getCooldownData().getRemainingTime() < 10) {
        event.setCancelled(true);
        return;
    }

    if (vent.getMaterial() == Material.ENDER_PEARL){
        event.setCancelled(true);
        player.sendMessage("You bypass Ender Pearl item Cooldown!");
    }
}
```

**User Data example**

```java
public void sendInfo(Player player) {

    UserData userData = ItemsCooldown.getCooldownManager().getUserData(player.getName());

    player.sendMessage("You have a cooldown: " + (userData.getCooldowns().isEmpty() ? "No" : "Yes (" + userData.getCooldowns().size() + ")"));
    player.sendMessage("Your cooldowns:");
    for (CooldownData cooldownData : userData.getCooldowns()) {
        player.sendMessage(cooldownData.getMaterial().name()+": "+cooldownData.getRemainingTime()+" seconds reaming");
    }
}
```

### Check more functions in the [CooldownManager class](https://github.com/OctavioFariasDamian/ItemsCooldown/blob/master/src/main/java/ar/com/octaviofarias/itemscooldown/managers/CooldownManager.java)