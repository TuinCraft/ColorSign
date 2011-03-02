package org.tuincraft.blaatz0r.ColorSign;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


public class ColorSign extends JavaPlugin {
	private final ColorSignPlayerListener playerListener = new ColorSignPlayerListener();
    public static Logger log;    
    
    public String name;
    public String version;
	
	public void onDisable() {
        log = Logger.getLogger("Minecraft");
        log.info(name + " " + version + " disabled");
		
	}

	public void onEnable() {
		name = this.getDescription().getName();
		version = this.getDescription().getVersion();

        PluginManager pm = getServer().getPluginManager();        
        pm.registerEvent(Event.Type.PLAYER_ITEM_HELD, playerListener, Priority.Normal, this);
        log = Logger.getLogger("Minecraft");
        log.info(name + " " + version + " enabled");
		
	}

	
}
