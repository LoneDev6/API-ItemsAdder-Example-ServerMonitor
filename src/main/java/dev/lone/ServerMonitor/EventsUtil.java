package dev.lone.ServerMonitor;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class EventsUtil
{
    public static void registerEventOnce(Listener li, Plugin plugin)
    {
        for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin))
            if(li.getClass().isInstance(listener.getListener()))
                return;
        Bukkit.getPluginManager().registerEvents(li, plugin);
    }

    public static void unregisterEvent(Listener li)
    {
        HandlerList.unregisterAll(li);
    }
}
