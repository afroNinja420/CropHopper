package me.afroninja;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.afroninja.cmds.CommandHandler;
import me.afroninja.events.Hopper;
import me.afroninja.events.Drops;

public class CropHopperMain extends JavaPlugin {

	public FileConfiguration cfg;
	public File cfgFile;
	
	public FileConfiguration loc;
	public File locFile;
	
	public List<String> items;
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new Drops(this), this);
		Bukkit.getPluginManager().registerEvents(new Hopper(this), this);
		getCommand("crophopper").setExecutor(new CommandHandler(this));
		
		locFile = new File("plugins/CropHopper", "hopper_data.yml");
		loc = YamlConfiguration.loadConfiguration(locFile);
		
		cfgFile = new File("plugins/CropHopper", "config.yml");
		cfg = YamlConfiguration.loadConfiguration(cfgFile);
		
		saveDefaultConfig();
		
		items = cfg.getStringList("items");
	}
	public FileConfiguration getSpecialConfig() {
		return cfg;
	}
	@Override
	public void reloadConfig() {
		cfg = YamlConfiguration.loadConfiguration(cfgFile);
		loc = YamlConfiguration.loadConfiguration(locFile);
	}
}