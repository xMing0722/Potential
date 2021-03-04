package teru.Potential;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	private static Plugin plugin;
	public static YamlConfiguration pMessages;
	
	public void onEnable() {
		plugin = (Plugin)this;
		if(!(new File(getDataFolder(), "potentials.yml")).exists()) {
			getPlugin().saveResource("potentials.yml", false);
		}
		if(!(new File(getDataFolder(), "messages.yml")).exists()) {
			getPlugin().saveResource("messages.yml", false);
		}
		pMessages = YamlConfiguration.loadConfiguration(new File(Main.getPlugin().getDataFolder() + File.separator + "messages.yml"));
		getCommand("poten").setExecutor((CommandExecutor) new Commands());
		getServer().getPluginManager().registerEvents(new CreatePotentialItem(), plugin);
		getServer().getPluginManager().registerEvents(new LockPotential(), plugin);
		CreatePotentialItem.init();
		LockPotential.init();
		Bukkit.getLogger().info("[Potential] Plugin is loaded");
	}

	public static Plugin getPlugin() {
		return plugin;
	}
}
