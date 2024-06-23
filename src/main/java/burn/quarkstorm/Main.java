package burn.quarkstorm;

import burn.quarkstorm.commands.Particle;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public final class Main extends JavaPlugin {






    final String pluginName = "QuarkStorm";
    private static Main plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getLogger().log(Level.INFO,pluginName + " => Status: Hello World");
        registerCommands();
        registerEvents();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO,pluginName + " => Goodbye cruel World");

    }

    public void registerCommands() {
        Objects.requireNonNull(getCommand("quark")).setExecutor(new Particle());
        getLogger().log(Level.INFO,pluginName + " => Status: Commands Loaded");
    }


    public void registerEvents() {
        //getServer().getPluginManager().registerEvents("", this);
        getLogger().log(Level.INFO,pluginName + " => Status: Events Loaded");
    }

    public static Main getPlugin() {
        return plugin;
    }
}
