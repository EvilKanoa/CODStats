package ca.kanoa.CODStats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Killstreak {

	private String name;
	private int required;
	private Command commands;
	
	public Killstreak(String name, int required, Command commands){
		this.name = name;
		this.setRequired(required);
		this.commands = commands;
	}

	public String getName() {
		return name;
	}

	public int getRequired() {
		return required;
	}

	public void setRequired(int required) {
		this.required = required;
	}
	
	public void runCommands(Player player){
		String command = commands.getCommand().replace("{player}", player.getName());
		if(command.startsWith("/")) command = command.substring(1);
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
	}
	
	public String getStringCommand(){
		return commands.getCommand();
	}
}
