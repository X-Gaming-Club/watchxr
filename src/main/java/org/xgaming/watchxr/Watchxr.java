package org.xgaming.watchxr;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.xgaming.watchxr.Commands.SendEventCommand;
import org.xgaming.watchxr.Utils.ConfigUtil;

import java.util.logging.Level;

public final class Watchxr extends JavaPlugin {

    private static Plugin plugin;
    private static boolean test;
    private static boolean debug;
    private static String posthogkey;

    @Override
    public void onEnable() {
        plugin = this;
        loadConfig();
        getCommand("sendEvent").setExecutor(new SendEventCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig(){

        ConfigUtil configUtil = new ConfigUtil(this, "config.yml");
        FileConfiguration configuration = configUtil.getConfig();

        test = configuration.getBoolean("test",true);
        debug = configuration.getBoolean("debug",true);
        posthogkey = configuration.getString("posthogkey",null);

        plugin.getLogger().log(Level.INFO,"------------------------------------------------------------");
        plugin.getLogger().log(Level.WARNING,test? "Server Running in TEST mode" : "Server running in PROD Mode");
        plugin.getLogger().log(Level.INFO,"------------------------------------------------------------");

        plugin.getLogger().log(Level.INFO,debug?" Printing logs to console":"NOT Printing logs to console");


    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isTest() {
        return test;
    }

    public static String getPosthogkey() {
        return posthogkey;
    }
}
