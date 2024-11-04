package org.xgaming.watchxr.Utils;

import com.posthog.java.PostHog;
import kong.unirest.json.JSONObject;
import org.bukkit.Bukkit;
import org.xgaming.watchxr.Watchxr;

import java.util.ArrayList;
import java.util.logging.Level;

public class PostHogUtils {

    public static void sendEventToPostHog(String distinctId, String eventName, JSONObject properties) {

        String POSTHOG_API_KEY = Watchxr.getPosthogkey();
        String POSTHOG_HOST = "https://us.i.posthog.com";

        String posthogKey = Watchxr.getPosthogkey();

        if(Watchxr.isDebug())
        {
            Watchxr.getPlugin().getLogger().info("Sending event to Posthog: " + eventName);
            Watchxr.getPlugin().getLogger().info("properties " + properties.toString());

        }
        if(Watchxr.isTest()){
            return;
        }
        if(posthogKey == null || posthogKey.isEmpty()){
//            Watchxr.getPlugin().getLogger().log(Level.SEVERE, "posthog key is not set in the config file.");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Watchxr.getPlugin(), () -> {
            PostHog posthog = new PostHog.Builder(POSTHOG_API_KEY).host(POSTHOG_HOST).build();

            posthog.capture(distinctId,eventName,properties.toMap());

            // Close the client to make sure all events are sent before app exits
            posthog.shutdown();
        });
    }

    public static void sendBatchEventsToPostHog(ArrayList<String> distinctIDs, ArrayList<String> eventNames, ArrayList<JSONObject> properties)
    {
        String POSTHOG_API_KEY = Watchxr.getPosthogkey();
        String POSTHOG_HOST = "https://us.i.posthog.com";
        if(Watchxr.isDebug())
        {
            Watchxr.getPlugin().getLogger().info("Sending " + distinctIDs.size() + " events to posthog: ");
        }
        if(Watchxr.isTest()){
            return;
        }
        if(POSTHOG_API_KEY == null || POSTHOG_API_KEY.isEmpty()){
            Watchxr.getPlugin().getLogger().log(Level.SEVERE, "posthog key is not set in the config file.");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Watchxr.getPlugin(),()->{
            PostHog posthog = new PostHog.Builder(POSTHOG_API_KEY).host(POSTHOG_HOST).build();
            for( int i = 0; i < distinctIDs.size(); i++)
            {
                posthog.capture(
                        distinctIDs.get(i),
                        eventNames.get(i),
                        properties.get(i).toMap());

                if(Watchxr.isDebug()) Watchxr.getPlugin().getLogger().info(eventNames.get(i) + " " + properties.get(i).toString());
            }
            posthog.shutdown();
            if(Watchxr.isDebug()) Watchxr.getPlugin().getLogger().info("Sent " + distinctIDs.size() + " events");
        });
    }
    public static void sendBatchEventsToPostHogSync(ArrayList<String> distinctIDs, ArrayList<String> eventNames, ArrayList<JSONObject> properties)
    {
        String POSTHOG_API_KEY = Watchxr.getPosthogkey();
        String POSTHOG_HOST = "https://us.i.posthog.com";
        if(Watchxr.isDebug())
        {
            Watchxr.getPlugin().getLogger().info("Sending " + distinctIDs.size() + " events to posthog: ");
        }
        if(Watchxr.isTest()){
            return;
        }
        if(POSTHOG_API_KEY == null || POSTHOG_API_KEY.isEmpty()){
            Watchxr.getPlugin().getLogger().log(Level.SEVERE, "posthog key is not set in the config file.");
            return;
        }

        PostHog posthog = new PostHog.Builder(POSTHOG_API_KEY).host(POSTHOG_HOST).build();
        for( int i = 0; i < distinctIDs.size(); i++)
        {
            posthog.capture(
                    distinctIDs.get(i),
                    eventNames.get(i),
                    properties.get(i).toMap());

            if(Watchxr.isDebug()) Watchxr.getPlugin().getLogger().info(eventNames.get(i) + " " + properties.get(i).toString());
        }
        posthog.shutdown();
        if(Watchxr.isDebug()) Watchxr.getPlugin().getLogger().info("Sent " + distinctIDs.size() + " events");
    }
}
