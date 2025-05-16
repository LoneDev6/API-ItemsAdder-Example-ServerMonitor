package dev.lone.ServerMonitor;

import dev.lone.ServerMonitor.commands.MainCommand;
import dev.lone.itemsadder.api.Events.ItemsAdderFirstLoadEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener
{
    public static Main instance;
    public static HudsHandler hudsHandler;

    private static MainCommand mainCmd;

    @Override
    public synchronized void onEnable()
    {
        instance = this;

        if(ItemsAdder.areItemsLoaded())
            init();
        else
            Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable()
    {
        clear();
    }

    @EventHandler
    void itemsadderLoad(ItemsAdderFirstLoadEvent e)
    {
       init();
    }

    private void init()
    {
        getLogger().info(ChatColor.GREEN + "ItemsAdder finished loading its stuff, now I load mine");
        hudsHandler = new HudsHandler(instance);
        hudsHandler.scheduleHudsHandling();
        if(mainCmd == null)
        {
            mainCmd = new MainCommand(instance);
            //noinspection DataFlowIssue
            getCommand("monitor").setExecutor(mainCmd);
            //noinspection DataFlowIssue
            getCommand("monitor").setTabCompleter(mainCmd);
        }
    }

    @EventHandler
    private void catchReloadIA_plugman(PluginDisableEvent e)
    {
        if(!e.getPlugin().getName().equals("ItemsAdder"))
            return;

        Main.clear();
    }

    public static void clear()
    {
        hudsHandler.taskRAM.cancel();
        hudsHandler.taskCPU.cancel();

        for(PlayerHudsData entry : hudsHandler.playersData.values())
        {
            entry.hudTextRam.setVisible(false);
            entry.hudRam.setVisible(false);

            entry.hudCPU.setVisible(false);

            entry.hudTextRam.clearFontImagesAndRefresh();
            entry.hudCPU.clearFontImagesAndRefresh();
        }
        hudsHandler.playersData.clear();
        hudsHandler = null;
    }
}