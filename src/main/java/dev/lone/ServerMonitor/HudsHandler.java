package dev.lone.ServerMonitor;

import com.sun.management.OperatingSystemMXBean;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;

public class HudsHandler implements Listener
{
    public static boolean DEBUG = false;
    private final Plugin plugin;

    public LinkedHashMap<Player, PlayerHudsData> playersData = new LinkedHashMap<>();
    private final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public BukkitTask taskRAM;
    public BukkitTask taskCPU;

    public static final int CPU_HEIGHT = 50;

    public HudsHandler(final Plugin plugin)
    {
        this.plugin = plugin;
        EventsUtil.registerEventOnce(this, plugin);
    }

    public void scheduleHudsHandling()
    {
        taskRAM = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            double usedRam = getUsedRam();
            for (PlayerHudsData playerHudsData : playersData.values())
            {
                if (!playerHudsData.isRAMShown)
                    continue;
                playerHudsData.hudRam.setFloatValue((float) usedRam);
                playerHudsData.holder.recalculateOffsets();
            }

        }, 0L, 5L);


        taskCPU = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (PlayerHudsData playerHudsData : playersData.values())
            {
                if (!playerHudsData.isCPUShown)
                    continue;
                int cpuLoad = (int) (getProcessCpuLoad() * CPU_HEIGHT / 100);
                if(cpuLoad == 0)
                    cpuLoad = 1;

                if(playerHudsData.hudCPU.getFontImagesCount() == 0)
                    continue;

                playerHudsData.hudCPU.removeFontImageByIndex(1);

                FontImageWrapper line = new FontImageWrapper("servermonitor:graph_" + cpuLoad);
                playerHudsData.hudCPU.addFontImage(line);
                playerHudsData.holder.recalculateOffsets();
            }

        }, 0L, 5L);
    }

    private double getUsedRam()
    {
        if(DEBUG)
            return Math.random() * 50.0;

        long totalMemory = osBean.getTotalPhysicalMemorySize();
        long freeMemory = osBean.getFreePhysicalMemorySize();
        long usedMemory = totalMemory - freeMemory;
        return (double) usedMemory / totalMemory * 100;
    }

    private double getProcessCpuLoad()
    {
        if(DEBUG)
            return (Math.sin(System.currentTimeMillis() / 1000.0) + 1) * 50.0;

        double processCpuLoad = osBean.getSystemCpuLoad();
        if(processCpuLoad < 0d)
            return 0d;
        return processCpuLoad * 100.0;
    }
}
