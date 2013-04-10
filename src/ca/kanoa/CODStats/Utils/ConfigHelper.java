package ca.kanoa.CODStats.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigHelper {

	private final Plugin plugin;

	public ConfigHelper(Plugin plugin){
		this.plugin = plugin;
	}

	public boolean saveConfig(FileConfiguration config, String file){
		File configFile = new File(plugin.getDataFolder(), file);
		try{
			config.save(configFile);
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public FileConfiguration getConfig(String configName){
		File config = new File(plugin.getDataFolder(), configName);
		try{
			if(!config.exists()){
				config.createNewFile();
				saveDefaultConfig(config.getName());
			}
			return YamlConfiguration.loadConfiguration(config);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private void saveDefaultConfig(String configName){
		try {
			File toFile = new File(plugin.getDataFolder(), configName);
			InputStream stream = getClass().getClassLoader().getResourceAsStream(configName);
			if (stream == null) {
				throw new FileNotFoundException();
			}
			OutputStream resStreamOut;
			int readBytes;
			byte[] buffer = new byte[4096];
			try {
				resStreamOut = new FileOutputStream(toFile);
				while ((readBytes = stream.read(buffer)) > 0) {
					resStreamOut.write(buffer, 0, readBytes);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
