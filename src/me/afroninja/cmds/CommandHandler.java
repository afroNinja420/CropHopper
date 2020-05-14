package me.afroninja.cmds;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.afroninja.CropHopperMain;
import me.afroninja.utils.Helpers;

public class CommandHandler implements CommandExecutor{
	
	CropHopperMain plugin;
	public CommandHandler(CropHopperMain plugin) {
		this.plugin = plugin;
	}
	Helpers help = new Helpers();
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("crophopper")) {
			if (sender.hasPermission("crophopper.use") || sender.isOp()) {
				if (args.length == 1 && args[0].equalsIgnoreCase("give") && sender instanceof Player) {
					Player target = Bukkit.getPlayer(sender.getName());
					ItemStack cropHopper = new ItemStack(Material.HOPPER);
					help.nameItemLore(cropHopper, help.cc(plugin.getConfig().getString("hopper_name")), help.cc(plugin.getConfig().getString("hopper_lore")));
					target.getInventory().addItem(cropHopper);
					sender.sendMessage(help.cc(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.crophopper_given")));	
				}
				if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
					Player target = Bukkit.getPlayer(args[1]);
					ItemStack cropHopper = new ItemStack(Material.HOPPER);
					help.nameItemLore(cropHopper, help.cc(plugin.getConfig().getString("hopper_name")), help.cc(plugin.getConfig().getString("hopper_lore")));
					target.getInventory().addItem(cropHopper);
					sender.sendMessage(help.cc(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.crophopper_success")));
					target.sendMessage(help.cc(plugin.getConfig().getString("messages.prefix") + plugin.getConfig().getString("messages.crophopper_given")));
				}
				if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
					sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "CropHopper" + ChatColor.GRAY + " - By " + ChatColor.DARK_RED + "Afro" + ChatColor.DARK_GRAY + "Ninja");
					sender.sendMessage(ChatColor.AQUA + "/crophopper give" + ChatColor.GRAY + " Give yourself a CropHopper.");
					sender.sendMessage(ChatColor.AQUA + "/crophopper give [player]" + ChatColor.GRAY + " Give another player a CropHopper.");
					sender.sendMessage(ChatColor.AQUA + "/crophopper list" + ChatColor.GRAY + " List all collected items.");
					sender.sendMessage(ChatColor.AQUA + "/crophopper add [item]" + ChatColor.GRAY + " Add item to collection list.");
					sender.sendMessage(ChatColor.AQUA + "/crophopper remove [item]" + ChatColor.GRAY + " Remove item from collection list.");
					sender.sendMessage(ChatColor.AQUA + "/crophopper reload" + ChatColor.GRAY + " Reload cfguration file.");
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("list")) {
						sender.sendMessage(ChatColor.GOLD + "CropHopper - Collected items: ");
						sender.sendMessage(ChatColor.WHITE + plugin.items.toString().replace("[", "").replace("]", ""));
						return true;
					}
					if (args[0].equalsIgnoreCase("reload")) {
						plugin.reloadConfig();
						sender.sendMessage(ChatColor.GOLD + "CropHopper: Reloading cfguration.");
						return true;
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("add") && !plugin.items.contains(args[1].toUpperCase())) {
						plugin.items.add(args[1].toUpperCase());
						plugin.cfg.set("items", plugin.items);
						try {
							plugin.cfg.save(plugin.cfgFile);
							sender.sendMessage(ChatColor.DARK_RED + "Added '" + ChatColor.AQUA + args[1].toUpperCase() + ChatColor.DARK_RED + "' to collection list!");
						} catch (IOException e) {
							e.printStackTrace();
							sender.sendMessage(ChatColor.DARK_RED + "Could not add '" + ChatColor.AQUA + args[1].toUpperCase() + ChatColor.DARK_RED + "' to collection list!");
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("remove") && plugin.items.contains(args[1].toUpperCase())) {
						plugin.items.remove(args[1].toUpperCase());
						plugin.cfg.set("items", plugin.items);
						try {
							plugin.cfg.save(plugin.cfgFile);
							sender.sendMessage(ChatColor.DARK_RED + "Removed '" + ChatColor.AQUA + args[1].toUpperCase() + ChatColor.DARK_RED + "' from collection list!");
						} catch (IOException e) {
							e.printStackTrace();
							sender.sendMessage(ChatColor.DARK_RED + "Could not remove '" + ChatColor.AQUA + args[1].toUpperCase() + ChatColor.DARK_RED + "' from collection list!");
						}
						return true;
					}
				}
			}
		}
		return true;
	}
}
