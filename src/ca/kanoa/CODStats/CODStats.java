package ca.kanoa.CODStats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.kanoa.CODStats.Helpers.Commands;
import ca.kanoa.CODStats.Helpers.EventListener;
import ca.kanoa.CODStats.Utils.ConfigHelper;
import ca.kanoa.CODStats.Utils.MySQLInfo;
import ca.kanoa.CODStats.Utils.SQLHelper;

public class CODStats extends JavaPlugin{

	//Use this to control what messages to print
	public final boolean debug = true;
	
	public HashSet<Stats> stats;
	public ArrayList<Killstreak> killstreaks = new ArrayList<Killstreak>();

	public FileConfiguration config;
	public FileConfiguration killstreakConfig;

	public boolean trackDeathByMob;
	public boolean canTrackOtherStats;

	public SQLHelper sqlhelp;
	public ConfigHelper confighelp;

	public void onEnable(){
		saveDefaultConfig();
		config = getConfig();

		confighelp = new ConfigHelper(this);

		killstreakConfig = confighelp.getConfig("Killstreaks.yml");
		loadKillstreaksFromConfig();

		this.trackDeathByMob = config.getBoolean("trackDeathByMob");
		this.canTrackOtherStats = config.getBoolean("canTrackOtherStats");

		sqlhelp = new SQLHelper(config.getBoolean("useMySQL"), getMySQLInfo());
		getLogger().info("Getting stats!");
		stats = sqlhelp.getStats();

		Commands CE = new Commands(this);
		this.getCommand("pvp").setExecutor(CE);
		this.getCommand("pvpa").setExecutor(CE);
		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

		getLogger().info("Enabled!");
	}

	private void loadKillstreaksFromConfig() {
		List<String> list = killstreakConfig.getStringList("Killstreaks");
		final String split = ", ";

		for(String s : list){
			try{
				killstreaks.add(new Killstreak(s.split(split)[0], Integer.parseInt(s.split(split)[1]), new Command(s.split(split)[2])));
			} catch (ArrayIndexOutOfBoundsException aioobe){
				getLogger().warning("Found badly formated killstreak near: '" + s + "'");
			} catch (NumberFormatException nfe){
				getLogger().warning("Could not find a number in the needed kills section near: '" + s + "'");
			} catch (Exception e){
				if(debug) e.printStackTrace();
				getLogger().warning("Unknown error (" + e.toString() + ") while parsing killstreak: '" + s + "'");
			}
		}
	}

	public void onDisable(){
		getLogger().info("Disabling!");
		getLogger().info("Saving stats to file...");

		sqlhelp.sendStats(stats);
	}

	private MySQLInfo getMySQLInfo() {
		if(config.getBoolean("useMySQL")) 
			return new MySQLInfo(
					config.getString("MySQL.address"), 
					config.getString("MySQL.database"), 
					config.getString("MySQL.username"), 
					config.getString("MySQL.password"), 
					config.getString("MySQL.port"));
		else return new MySQLInfo();
	}


	public Stats getStatsForPlayer(String player){
		for(Stats s : stats)
			if(s.getPlayerName().equals(player))
				return s;
		Stats stat = new Stats(player);
		stats.add(stat);
		return stat;
	}

	public Stats getStatsForPlayer(Player player){
		return getStatsForPlayer(player.getName());
	}

	public String killstreakMessage() {
		return config.getString("killstreakMessage");
	}
}
