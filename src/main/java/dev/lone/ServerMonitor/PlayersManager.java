package dev.lone.ServerMonitor;

import com.sun.management.OperatingSystemMXBean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;

public class PlayersManager implements Listener
{
    private Plugin plugin;

    public LinkedHashMap<Player, PlayerDataHolder> playersData = new LinkedHashMap<>();
    private OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public BukkitTask taskRAM;
    public BukkitTask taskCPU;

    public PlayersManager(final Plugin plugin)
    {
        this.plugin = plugin;
        EventsUtil.registerEventOnce(this, plugin);
    }

    public void scheduleHudsHandling()
    {
        taskRAM = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            long totalMemory = Runtime.getRuntime().totalMemory();
            long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

            int usedRam = (int) (usedMemory * 16 / totalMemory);

            for(PlayerDataHolder playerDataHolder : playersData.values())
            {
                if(!playerDataHolder.isRAMShown)
                    continue;
                playerDataHolder.hudRam.setFloatValue(usedRam);
            }

        }, 0, 5L);


        taskCPU = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for(PlayerDataHolder playerDataHolder : playersData.values())
            {
                if(!playerDataHolder.isCPUShown)
                    continue;
                int cpuLoad = (int) (getProcessCpuLoad() * playerDataHolder.hudCPU.getFontImagesCount() / 100);

                playerDataHolder.hudCPU.removeFontImageByIndex(1);

                FontImageWrapper line = new FontImageWrapper("servermonitor:graph_" + cpuLoad);
                playerDataHolder.hudCPU.addFontImage(line);
            }

        }, 0, 5L);
    }

    private double getProcessCpuLoad()
    {
        return osBean.getProcessCpuLoad()*100;
    }

    @EventHandler
    private void catchReloadIA_plugman(PluginDisableEvent e)
    {
        if(!e.getPlugin().getName().equals("ItemsAdder"))
            return;

        Main.clear();
    }

    @EventHandler
    private void catchReloadIA_plugman(PluginEnableEvent e)//TODO: fix
    {
        if(!e.getPlugin().getName().equals("ItemsAdder"))
            return;

        Main.init();
    }

}
