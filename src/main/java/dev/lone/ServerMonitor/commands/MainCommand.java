package dev.lone.ServerMonitor.commands;

import dev.lone.ServerMonitor.Main;
import dev.lone.ServerMonitor.PlayerHudsData;
import dev.lone.ServerMonitor.HudsHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter
{
    Plugin plugin;

    public MainCommand(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if(!(commandSender instanceof Player))
            return true;

        Player player = (Player) commandSender;

        if(args[0].equals("ram"))
        {
            PlayerHudsData playerData = Main.hudsHandler.playersData.get(player);
            if(playerData == null)
                playerData = new PlayerHudsData(player);

            Main.hudsHandler.playersData.put(player, playerData);

            if (playerData.isRAMShown)
            {
                playerData.hudRam.setVisible(false);
                playerData.hudTextRam.setVisible(false);
                playerData.isRAMShown = false;
                playerData.hudTextRam.clearFontImagesAndRefresh();
            }
            else
            {
                playerData.hudRam.setVisible(true);
                playerData.initHUDTextRamFontImages();
                playerData.hudTextRam.setVisible(true);
                playerData.isRAMShown = true;
            }
            playerData.holder.recalculateOffsets();
            playerData.hudRam.setFloatValue(0);
        }
        else if(args[0].equals("cpu"))
        {
            PlayerHudsData playerData = Main.hudsHandler.playersData.get(player);
            if(playerData == null)
                playerData = new PlayerHudsData(player);

            Main.hudsHandler.playersData.put(player, playerData);

            if (playerData.isCPUShown)
            {
                playerData.hudTextCPU.setVisible(false);
                playerData.hudCPU.setVisible(false);
                playerData.isCPUShown = false;
                playerData.hudCPU.clearFontImagesAndRefresh();
                playerData.hudTextCPU.clearFontImagesAndRefresh();
            }
            else
            {
                playerData.hudTextCPU.setVisible(true);
                playerData.hudCPU.setVisible(true);
                playerData.initHudCPU();
                playerData.isCPUShown = true;
            }
            playerData.holder.recalculateOffsets();
            playerData.holder.sendUpdate();
        }
        else if(args[0].equals("debug"))
        {
            HudsHandler.DEBUG = !HudsHandler.DEBUG;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length == 1)
        {
            List<String> strings = new ArrayList<>();
            strings.add("ram");
            strings.add("cpu");
            strings.add("debug");
            return strings;
        }
        return new ArrayList<>();
    }
}