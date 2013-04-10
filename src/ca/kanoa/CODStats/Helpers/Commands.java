package ca.kanoa.CODStats.Helpers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.kanoa.CODStats.CODStats;
import ca.kanoa.CODStats.Killstreak;
import ca.kanoa.CODStats.Stats;

public class Commands implements CommandExecutor{

	CODStats plugin;

	//Colors
	final ChatColor green  = ChatColor.GREEN;
	final ChatColor red    = ChatColor.RED;
	final ChatColor blue   = ChatColor.BLUE;
	final ChatColor black  = ChatColor.BLACK;
	final ChatColor purple = ChatColor.DARK_PURPLE;

	public Commands(CODStats plugin){
		this.plugin = plugin;
	}

	private boolean pvpCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Stats stats;
		if(args.length == 0 && sender.hasPermission("cod.check.self"))
			stats = plugin.getStatsForPlayer(sender.getName());
		else if(args.length == 1 && sender.hasPermission("cod.check.other"))
			stats = plugin.getStatsForPlayer(args[0]);
		else if(args.length > 1 && (sender.hasPermission("cod.check.self") || sender.hasPermission("cod.check.other"))){
			sender.sendMessage(red + "Too many arguments");
			return false;
		}
		else{
			sender.sendMessage(red + "You don't have permission!");
			return false;
		}
	
		sender.sendMessage(
				blue + "Kills: " + red + stats.getKills() + ", " + 
						blue + "Deaths: " + red + stats.getDeaths() + ", " + 
						blue + "Highest Killstreak: " + red + stats.getKillstreak() + ", " +
						blue + "Current Killstreak: " + red + stats.getCurrentKillStreak() + ", " +
						blue + "KDR: " + red + stats.getKDR());
		return true;
	}

	private boolean pvpaCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
			sender.sendMessage(red + "Reloading config...");
			
			plugin.reloadConfig();
			plugin.config = plugin.getConfig();
			
			plugin.confighelp.saveConfig(plugin.killstreakConfig, "Killstreaks.yml");
			plugin.killstreakConfig = plugin.confighelp.getConfig("Killstreaks.yml");
			
			sender.sendMessage(green + "The configuration has been reloaded.");
			return true;
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("set")){
			if(args.length == 4){
				Stats stats = plugin.getStatsForPlayer(args[2]);
				String item = args[1];
				String value = args[3];

				try{
					if(item.equalsIgnoreCase("kills"))
						stats.setKills(Integer.valueOf(value));
					else if(item.equalsIgnoreCase("deaths"))
						stats.setDeaths(Integer.valueOf(value));
					else if(item.equalsIgnoreCase("killstreak"))
						stats.setKillstreak(Integer.valueOf(value));
					else{
						sender.sendMessage(red + "Unknown item type!");
						return true;
					}
					sender.sendMessage(blue + item + " has been set to: " + red + value);
				} catch (NumberFormatException e){
					sender.sendMessage(red + "The value needs to be a number!");
				}
				return true;
			}
			else{
				sender.sendMessage("/pvpa set <item> <player> <value>");
				return true;
			}
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("send")){
			sender.sendMessage(red + "Sending current stats to SQL database...");
			plugin.sqlhelp.sendStats(plugin.stats);
			sender.sendMessage(green + "Stats sent!");
			return true;
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("killstreaks")){
			for(Killstreak ks : plugin.killstreaks)
				sender.sendMessage(green + ks.getName() + ": " + red + ks.getRequired() + " kills needed, " + purple + ks.getStringCommand());
			return true;
		}
		else{
			sender.sendMessage(black + "Unknown Command and/or arguments!");
			sender.sendMessage(blue + "CODStats admin commands:");
			sender.sendMessage(red + "Set: " + green + "Used to set a players kills/deaths/killstreak");
			sender.sendMessage(red + "Send: " + green + "Sends the current stats to the database.");
			sender.sendMessage(red + "Reload: " + green + "Reloads the configuration file from disk.");
			sender.sendMessage(red + "Killstreaks: " + green + "Lists all the currently loaded killstreaks.");
			return true;
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			if(cmd.getName().equalsIgnoreCase("pvp")) return pvpCommand(sender, cmd, label, args);
			else if(cmd.getName().equalsIgnoreCase("pvpa")) return pvpaCommand(sender, cmd, label, args);
			else return false;
		}
		else{
			sender.sendMessage("Player only command");
			return true;
		}
	}

}
