package ca.kanoa.CODStats.Helpers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import ca.kanoa.CODStats.CODStats;
import ca.kanoa.CODStats.Killstreak;
import ca.kanoa.CODStats.Stats;

public class EventListener implements Listener{

	CODStats plugin;

	public EventListener(CODStats plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		if(player.getKiller() instanceof Player){
			Player killer = player.getKiller();
			Stats kStats = plugin.getStatsForPlayer(killer);

			if(player.hasPermission("cod.track")) plugin.getStatsForPlayer(player).addDeath();
			if(killer.hasPermission("cod.track")){
				kStats.addKill();

				for(Killstreak ks : plugin.killstreaks){
					if(plugin.getStatsForPlayer(killer).getCurrentKillStreak() == ks.getRequired()){
						ks.runCommands(killer);
						killer.sendMessage(plugin.killstreakMessage().replace("{kills}", ""+kStats.getCurrentKillStreak()).replace("{killstreak}", ks.getName()));
					}
				}
			}
		}
		else if(plugin.trackDeathByMob && player.hasPermission("cod.track"))
			plugin.getStatsForPlayer(player).addDeath();
	}
}
