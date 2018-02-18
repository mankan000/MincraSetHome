package mincrasethome;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MincraSetHome extends JavaPlugin{
	static JavaPlugin plugin;
	private File configFile;
	static FileConfiguration config;
	public void onEnable(){
		plugin = this;
		this.saveDefaultConfig();
		configFile= new File(getDataFolder(),"config.yml");
		config =new YamlConfiguration();
		try {
			config.load(this.configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		this.getCommand("home").setExecutor(new Home());
		this.getCommand("sethome").setExecutor(new Sethome());
		this.getCommand("mincrasethome").setExecutor(new MincraCommands());
	}
	public void onDisable(){}
}
