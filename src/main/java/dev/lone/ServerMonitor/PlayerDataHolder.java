package dev.lone.ServerMonitor;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerCustomHUDWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHUDsHolderWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerQuantityHudWapper;
import org.bukkit.entity.Player;

public class PlayerDataHolder
{
    private Player player;
    private static final String hudCPUName = "servermonitor:cpu_graph";
    private static final String hudRamName = "servermonitor:ram";
    private static final String hudTextRamName = "servermonitor:text_ram";
    private static final String hudTextCPUName = "servermonitor:text_cpu";

    public PlayerHUDsHolderWrapper playerHUDsHolder;

    public PlayerCustomHUDWrapper hudCPU;
    public PlayerCustomHUDWrapper hudTextCPU;
    public boolean isCPUShown;

    public PlayerQuantityHudWapper hudRam;
    public PlayerCustomHUDWrapper hudTextRam;
    public boolean isRAMShown;

    public PlayerDataHolder(Player player)
    {
        this.player = player;
        playerHUDsHolder = new PlayerHUDsHolderWrapper(player);
        hudCPU = new PlayerCustomHUDWrapper(playerHUDsHolder, hudCPUName);
        hudRam = new PlayerQuantityHudWapper(playerHUDsHolder, hudRamName);
        hudTextRam = new PlayerCustomHUDWrapper(playerHUDsHolder, hudTextRamName);
        hudTextCPU = new PlayerCustomHUDWrapper(playerHUDsHolder, hudTextCPUName);

        if(hudCPU == null)
            throwHudNotFound(hudCPUName);
        if(hudRam == null)
            throwHudNotFound(hudRamName);
        if(hudTextRam == null)
            throwHudNotFound(hudTextRamName);
        if(hudTextCPU == null)
            throwHudNotFound(hudTextCPUName);
    }

    private void throwHudNotFound(String namespacedID)
    {
        try
        {
            throw new NullPointerException("Cannot find HUD: " + namespacedID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initHudCPU()
    {
        //TODO: check if exists before
        hudTextCPU.addFontImage(new FontImageWrapper(hudTextCPUName));
        hudCPU.addFontImage(new FontImageWrapper("servermonitor:graph_start"));
        for(int j=0;j<128;j++)
        {
            //TODO: check if exists before
            hudCPU.addFontImage(new FontImageWrapper("servermonitor:graph_1"));
        }
    }

    public void initHUDTextRamFontImages()
    {
        //TODO: check if exists before
        FontImageWrapper line = new FontImageWrapper(hudTextRamName);
        hudTextRam.addFontImage(line);
    }
}
