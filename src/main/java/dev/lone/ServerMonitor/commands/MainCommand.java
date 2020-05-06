package dev.lone.ServerMonitor.commands;

import dev.lone.ServerMonitor.Main;
import dev.lone.ServerMonitor.PlayerDataHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MainCommand implements CommandExecutor
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
            PlayerDataHolder playerData = Main.playersManager.playersData.get(player);
            if(playerData == null)
                playerData = new PlayerDataHolder(player);

            Main.playersManager.playersData.put(player, playerData);

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
            playerData.playerHUDsHolder.recalculateOffsets();
            playerData.hudRam.setFloatValue(0);
        }
        else if(args[0].equals("cpu"))
        {
            PlayerDataHolder playerData = Main.playersManager.playersData.get(player);
            if(playerData == null)
                playerData = new PlayerDataHolder(player);

            Main.playersManager.playersData.put(player, playerData);

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
                playerData.playerHUDsHolder.recalculateOffsets();
            }
        }
        return true;
    }

}