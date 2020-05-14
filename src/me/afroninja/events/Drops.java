package me.afroninja.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import me.afroninja.CropHopperMain;


public class Drops implements Listener {

	CropHopperMain plugin;
	public Drops(CropHopperMain plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onItemDrop(ItemSpawnEvent e) {
		String item = e.getEntity().getItemStack().getType().name();
		
		if(plugin.items.contains(item)) {
			int spawnX = e.getEntity().getLocation().getChunk().getX();
			int spawnZ = e.getEntity().getLocation().getChunk().getZ();
			String hopperSave = String.valueOf(spawnX + String.valueOf(spawnZ));
			if(plugin.loc.getString("hopperlocs." + hopperSave) != null) {
				e.setCancelled(true);
				Material itemType = e.getEntity().getItemStack().getType();
				int itemAmount = e.getEntity().getItemStack().getAmount();
				int hopperX = plugin.loc.getInt("hopperlocs." + hopperSave + "." + "x");
				int hopperY = plugin.loc.getInt("hopperlocs." + hopperSave + "." + "y");
				int hopperZ = plugin.loc.getInt("hopperlocs." + hopperSave + "." + "z");
				String hopperWorld = plugin.loc.getString("hopperlocs." + hopperSave + "." + "world");
				Location hopperLoc = new Location(Bukkit.getWorld(hopperWorld), hopperX, hopperY, hopperZ);
				Hopper hopper = (Hopper) hopperLoc.getBlock().getState();
				hopper.getInventory().addItem(new ItemStack(itemType, itemAmount));
			}
		}
		return;
	}
}
