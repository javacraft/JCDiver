package com.java_craft.mc.JCDiver;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import com.java_craft.mc.JCDiver.listeners.EntityListener;


public class JCDiver extends JavaPlugin {
	private Logger					logger;
	private PluginDescriptionFile	pdFile;
	private String					logPrefix;
	public final static short		DAMAGE			= 1;
	public final static String		PERM_ENABLED	= "kadiving.enabled";
	public final static String		PERM_UNLIMITED	= "kadiving.unlimited";

	public Map<Player, Boolean>		breathers		= new HashMap<Player, Boolean>();


	@Override
	public void onDisable() {
		logInfo( " Disabled!" );
	}


	@Override
	public void onEnable()
	{
		pdFile = getDescription();
		logger = Logger.getLogger( pdFile.getName() );
		logPrefix = "[" + pdFile.getName() + "] ";

		getServer().getPluginManager().registerEvents( new EntityListener( this ), this );

		logInfo( "Version " + pdFile.getVersion() + " enabled." );
	}


	/**
	 * Convenience INFO logging function that prefixes message with plugin's identifier.
	 * 
	 * @param msg message to log
	 */
	public void logInfo( String msg ) {
		logger.log( Level.INFO, logPrefix + " " + msg );
	}


	/**
	 * General convenience logging function that prefixes message with plugin's identifier.
	 * 
	 * @param level log level of message
	 * @param msg message to log
	 */
	public void log( Level level, String msg ) {
		logger.log( level, logPrefix + " " + msg );
	}

}
