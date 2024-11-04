package org.xgaming.watchxr.Commands;

import kong.unirest.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.xgaming.watchxr.Utils.PostHogUtils;
import org.xgaming.watchxr.Watchxr;

public class SendEventCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only op players or console can use this
        if(!(sender.isOp() || sender instanceof ConsoleCommandSender)) return false;

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /sendEvent <playerName> <eventName> [key1 value1 key2 value2 ...]");
            return true;
        }

        String playerName = args[0];
        String eventName = args[1];
        JSONObject properties = new JSONObject();

        // Handle optional properties if provided
        if (args.length > 2) {
            try {
                // Process key-value pairs
                for (int i = 2; i < args.length; i += 2) {
                    if (i + 1 >= args.length) {
                        sender.sendMessage("§cMissing value for key: " + args[i]);
                        return true;
                    }

                    String key = args[i];
                    String valueStr = args[i + 1];
                    Object value = parseValue(valueStr);
                    properties.put(key, value);
                }
            } catch (Exception e) {
                sender.sendMessage("§cError parsing properties: " + e.getMessage());
                sender.sendMessage("§cUsage example: /sendEvent Alex player_death premium true lvl 20 rank elite");
                return true;
            }
        }

        // Validate player name
        if (Bukkit.getPlayer(playerName) == null) {
            sender.sendMessage("§cPlayer not found: " + playerName);
            return true;
        }

        try {
            // Send event using PostHogUtils
            PostHogUtils.sendEventToPostHog(playerName, eventName, properties);
            sender.sendMessage("§aEvent '" + eventName + "' sent successfully for player " + playerName);

            if (Watchxr.isDebug()) {
                sender.sendMessage("§7Properties: " + properties.toString());
            }
        } catch (Exception e) {
            sender.sendMessage("§cError sending event: " + e.getMessage());
            if (Watchxr.isDebug()) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Parses a string value into the appropriate data type
     * @param value The string value to parse
     * @return The parsed value as Boolean, Integer, Double, or String
     */

    //This part just handles parsing properties in command to json
    private Object parseValue(String value) {
        // Check for boolean
        if (value.equalsIgnoreCase("true")) return true;
        if (value.equalsIgnoreCase("false")) return false;

        // Check for integer
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {}

        // Check for double
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {}

        // Return as string if no other type matches
        return value;
    }
}