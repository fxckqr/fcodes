package ru.DiamondDev.core;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.DiamondDev.Main;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CodeManager {
    private final Main plugin;
    private YamlConfiguration dataConfig;
    private File dataFile;

    public CodeManager(Main plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать data.yml!");
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка сохранения data.yml!");
        }
    }

    public void processCode(Player player, String code) {
        ConfigurationSection codes = plugin.getConfig().getConfigurationSection("codes");
        if (codes == null || !codes.contains(code)) {
            player.sendMessage(plugin.getConfig().getString("messages.code-notfound"));
            return;
        }

        ConfigurationSection codeData = codes.getConfigurationSection(code);
        UUID playerId = player.getUniqueId();

        int playerUses = dataConfig.getInt("players." + playerId + "." + code, 0);
        int totalUses = dataConfig.getInt("codes." + code, 0);

        if (codeData.getInt("limit", 1) <= playerUses) {
            player.sendMessage(codeData.getString("message-limit"));
            return;
        }

        if (codeData.getInt("limit-players", 2) <= totalUses) {
            player.sendMessage(codeData.getString("message-limit-players"));
            return;
        }

        dataConfig.set("players." + playerId + "." + code, playerUses + 1);
        dataConfig.set("codes." + code, totalUses + 1);
        saveData();

        for (String command : codeData.getStringList("commands")) {
            plugin.getServer().dispatchCommand(
                    plugin.getServer().getConsoleSender(),
                    command.replace("%player%", player.getName())
            );
        }

        String[] messageParts = codeData.getString("message").split(";title:");
        player.sendMessage(plugin.getConfig().getString("messages.prefix") + messageParts[0]);
        if (messageParts.length > 1) {
            player.sendTitle("", messageParts[1].replace("%nl%", "\n"), 10, 70, 20);
        }
    }
}
