package me.afroninja.events;

import java.io.IOException;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.afroninja.CropHopperMain;
import me.afroninja.utils.Helpers;

public class Hopper implements Listener {

	CropHopperMain plugin;
	public Hopper(CropHopperMain plugin){
		this.plugin = plugin;
	}
	Helpers help = new Helpers();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onHopperPlace(BlockPlaceEvent e) {

		//Basic checks
		if(e.isCancelled()) return;
		if(e.getItemInHand() == null) return;
		ItemStack blockPlaced = e.getItemInHand();
		Player p = e.getPlayer();
		if(blockPlaced.getType() != Material.HOPPER) return;
		if(!blockPlaced.hasItemMeta() || !blockPlaced.getItemMeta().hasDisplayName()) return;
		if(!blockPlaced.getItemMeta().getDisplayName().equals(help.cc(plugin.getConfig().getString("hopper_name")))) return;
		if(!blockPlaced.getItemMeta().getLore().contains(help.cc(plugin.getConfig().getString("hopper_lore")))) return;

		int chunkX = e.getBlockPlaced().getChunk().getX();
		int chunkZ = e.getBlockPlaced().getChunk().getZ();
		int hopperX = e.getBlockPlaced().getX();
		int hopperY = e.getBlockPlaced().getY();
		int hopperZ = e.getBlockPlaced().getZ();
		String hopperSave = String.valueOf(chunkX) + String.valueOf(chunkZ);

		if(plugin.loc.getString("hopperlocs." + hopperSave) != null) {
			e.setCancelled(true);
			p.sendMessage(help.cc(plugin.getConfig().getString("messages.hopper_already_in_chunk")));
			return;
		}
		//Set and save values in .yml
		p.sendMessage(help.cc(plugin.getConfig().getString("messages.hopper_place_success")));
		plugin.loc.set("hopperlocs." + hopperSave + "." + "chunkx", chunkX);
		plugin.loc.set("hopperlocs." + hopperSave + "." + "chunkz", chunkZ);
		plugin.loc.set("hopperlocs." + hopperSave + "." + "x", hopperX);
		plugin.loc.set("hopperlocs." + hopperSave + "." + "y", hopperY);
		plugin.loc.set("hopperlocs." + hopperSave + "." + "z", hopperZ);
		plugin.loc.set("hopperlocs." + hopperSave + "." + "world", e.getBlockPlaced().getWorld().getName());
		try {
			plugin.loc.save(plugin.locFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onHopperBreak(BlockBreakEvent e) {

		//Basic checks
		if(e.isCancelled()) return;
		if(e.getBlock() == null) return;
		Block blockBroke = e.getBlock();
		Player p = e.getPlayer();
		if(blockBroke.getType() != Material.HOPPER) return;

		int chunkX = blockBroke.getChunk().getX();
		int chunkZ = blockBroke.getChunk().getZ();
		int hopperX = blockBroke.getX();
		int hopperY = blockBroke.getY();
		int hopperZ = blockBroke.getZ();
		String hopperSave = String.valueOf(chunkX) + String.valueOf(chunkZ);
		int cfgX = plugin.loc.getInt("hopperlocs." + hopperSave + "." + "x");
		int cfgY = plugin.loc.getInt("hopperlocs." + hopperSave + "." + "y");
		int cfgZ = plugin.loc.getInt("hopperlocs." + hopperSave + "." + "z");

		//Determine whether the hopper broken is a CropHopper
		if(plugin.loc.getString("hopperlocs." + hopperSave) == null) return;
		if(cfgX == hopperX && cfgY == hopperY && cfgZ == hopperZ) {
			//Cancel BlockBreakEvent and create CropHopper item
			ItemStack cropHopper = new ItemStack(Material.HOPPER);
			help.nameItemLore(cropHopper, help.cc(plugin.getConfig().getString("hopper_name")), help.cc(plugin.getConfig().getString("hopper_lore")));
			e.setCancelled(true);
			//Update values in .yml
			plugin.loc.set("hopperlocs." + hopperSave, null);
			try {
				plugin.loc.save(plugin.locFile);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			if(p.getGameMode() != GameMode.CREATIVE) {
				e.getBlock().getWorld().dropItem(blockBroke.getLocation(), cropHopper);
			}
			e.getBlock().setType(Material.AIR);
			p.sendMessage(help.cc(plugin.getConfig().getString("messages.hopper_break_success")));
		}
	}
}

