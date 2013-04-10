package ca.kanoa.CODStats;

public class Stats {
	
	
	//The real stats are here ;)
	private String playerName;
	private int kills;
	private int deaths;
	private int killstreak;
	private int currentKillstreak;
	
	
	//Constructors
	public Stats(String playerName){
		this.setPlayerName(playerName);
		this.setKills(0);
		this.setDeaths(0);
		this.setKillstreak(0);
		this.setCurrentKillStreak(0);
	}
	
	public Stats(String playerName, int kills, int deaths, int killstreak, int currKillstreak){
		this.setPlayerName(playerName);
		this.setKills(kills);
		this.setDeaths(deaths);
		this.setKillstreak(killstreak);
		this.setCurrentKillStreak(currKillstreak);
	}

	
	//"Make thing easier" methods, gotta love em :P
	public float getKDR(){
		if(this.deaths == 0) 
			return (this.kills);
		else{
			return ((float)this.kills / this.deaths);
		}
	}
	
	public long addKill(){
		this.kills++;
		this.currentKillstreak++;
		if(this.currentKillstreak > this.killstreak)
			this.killstreak = this.currentKillstreak;
		return kills;
	}
	
	public long addDeath(){
		this.deaths++;
		resetKillstreak();
		return deaths;
	}
	
	private boolean resetKillstreak(){
		if(this.currentKillstreak > this.killstreak){
			this.killstreak = this.currentKillstreak;
			this.currentKillstreak = 0;
			return true;
		}
		else{
			this.currentKillstreak = 0;
			return false;
		}
	}
	
	
	/*
	 * returns a string with the stat info to use with a database eg:
	 * useLongString = true:
	 * 	"playerName='12323op', kills=232, deaths=6, killstreak=70, currentKillstreak=12"
	 * useLongString = false:
	 * 	"'12323op', 232, 6, 70, 12"
	 */
	public String getMySQL(boolean useLongString){
		if(useLongString)
			return "playerName='" + this.playerName + "', kills=" + this.kills + ", deaths=" + this.deaths + 
					", killstreak=" + this.killstreak + ", currentKillstreak=" + this.currentKillstreak;
		else
			return "'" + this.playerName + "', " + this.kills + ", " + this.deaths + ", " + this.killstreak + ", " + this.currentKillstreak;
	}
	
	//Getter and Setters, boring stuff...
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public long getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}

	public int getCurrentKillStreak() {
		return currentKillstreak;
	}

	public void setCurrentKillStreak(int currentKillstreak) {
		this.currentKillstreak = currentKillstreak;
	}
	
}
