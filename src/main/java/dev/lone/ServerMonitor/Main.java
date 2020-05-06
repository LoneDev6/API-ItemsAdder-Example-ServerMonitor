package dev.lone.ServerMonitor;

import dev.lone.ServerMonitor.commands.MainCommand;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin
{
    public static Main instance;
    public static PlayersManager playersManager;

    private static MainCommand mainCmd;

    @Override
    public synchronized void onEnable()
    {
        instance = this;
        init();
    }

    @Override
    public void onDisable()
    {
        clear();
    }

    public static void init()
    {
        Bukkit.getScheduler().runTaskAsynchronously(instance, () ->
        {
            //dirty
            while(!ItemsAdder.areItemsLoaded()){}
            Bukkit.getScheduler().runTask(instance, () ->
            {
                playersManager = new PlayersManager(instance);
                playersManager.scheduleHudsHandling();
                if(mainCmd == null)
                {
                    mainCmd = new MainCommand(instance);
                    Bukkit.getPluginCommand("monitor").setExecutor(mainCmd);
                }
            });
        });

    }

    public static void clear()
    {
        playersManager.taskRAM.cancel();
        playersManager.taskCPU.cancel();

        for(PlayerDataHolder entry : playersManager.playersData.values())
        {
            entry.hudTextRam.setVisible(false);
            entry.hudRam.setVisible(false);

            entry.hudCPU.setVisible(false);

            entry.hudTextRam.clearFontImagesAndRefresh();
            entry.hudCPU.clearFontImagesAndRefresh();
        }
        playersManager.playersData.clear();
        playersManager = null;
    }
}

/*
    var str = "";
    for(var i=0;i<25;i++)
    {
        str += `\n  graph_`+i+`:
        path: "font/hud/graph/`+i+`.png"
        y_position: -12`;

    }

    console.log(str);
*/
