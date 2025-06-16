package ru.DiamondDev.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.DiamondDev.Main;

public class CodeCommand implements CommandExecutor {
    private final Main plugin;

    public CodeCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cТолько для игроков!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("fcode.code")) {
            player.sendMessage(plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getConfig().getString("messages.code"));
            return true;
        }

        plugin.getCodeManager().processCode(player, args[0]);
        return true;
    }
}