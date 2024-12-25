package ar.com.octaviofarias.itemscooldown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CooldownCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!commandSender.hasPermission("itemscooldown.command")){
            sendMessage(commandSender, "&cYou don't have permission to use this command.");
            return false;
        }
        if(args.length < 3){
            sendMessage(commandSender, "&cThe usage of the command is: &7/cooldown <player> <material> <seconds>");
            return false;
        }
        Player p = Bukkit.getPlayerExact(args[0]);
        Material material;
        int seconds;

        if(p == null || !p.isOnline()){
            sendMessage(commandSender, "&cThat player isn't online.");
            return false;
        }

        try{
            material = Material.valueOf(args[1].toUpperCase());
        }catch (IllegalArgumentException e){
            sendMessage(commandSender, "&cThat material doesn't exists.");
            return false;
        }

        try{
            seconds = Integer.parseInt(args[2]);
        }catch (NumberFormatException e){
            sendMessage(commandSender, "&c"+args[2]+" isn't a valid integer number.");
            return false;
        }

        if(material.isAir()){
            sendMessage(commandSender, "&cYou can't add a cooldown with a air block!");
            return false;
        }

        if(ItemsCooldown.getCooldownManager().has(p, material)){
            sendMessage(commandSender, "&c"+p.getName()+" already has a cooldown in that material!");
            return false;
        }

        ItemsCooldown.getCooldownManager().add(p, material, seconds);
        sendMessage(commandSender, "&fYou add "+ (material.isBlock() ? "block" : "item" )+" cooldown for &e"+seconds+" &fto &a" + p.getName()+"&f!");
        ItemsCooldown.getDataManager().saveUserData(ItemsCooldown.getCooldownManager().getUserData(p.getName()));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length == 1)
            return filterSuggestions(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[0]);
        else if(args.length == 2)
            return filterSuggestions(Arrays.stream(Material.values()).map(Material::name).toList(), args[1]);
        return List.of();
    }

    public static List<String> filterSuggestions(List<String> suggestions, String currentInput) {
        if (currentInput.isEmpty()) {
            return suggestions;
        }

        return suggestions.stream()
                .filter(s -> s.toLowerCase().startsWith(currentInput.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender sender, String content) {
        sender.sendMessage(color(content));
    }

    public static String color(String message) {
        Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);

        while (matcher.find()) {
            String hexColor = matcher.group();
            StringBuilder minecraftColor = new StringBuilder("§x");
            for (char c : hexColor.substring(1).toCharArray()) {
                minecraftColor.append("§").append(c);
            }
            message = message.replace(hexColor, minecraftColor.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
