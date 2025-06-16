package ru.DiamondDev;

import org.bukkit.plugin.java.JavaPlugin;
import ru.DiamondDev.commands.CodeCommand;
import ru.DiamondDev.commands.ReloadCommand;
import ru.DiamondDev.core.CodeManager;

public class Main extends JavaPlugin {
    private CodeManager codeManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.codeManager = new CodeManager(this);

        getCommand("code").setExecutor(new CodeCommand(this));
        getCommand("codereload").setExecutor(new ReloadCommand(this));

        getLogger().info("Плагин fCode успешно загружен!");
    }

    public CodeManager getCodeManager() {
        return codeManager;
    }
}