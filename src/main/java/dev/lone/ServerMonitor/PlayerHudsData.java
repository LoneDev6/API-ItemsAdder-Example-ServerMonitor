package dev.lone.ServerMonitor;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerCustomHudWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerQuantityHudWrapper;
import org.bukkit.entity.Player;

public class PlayerHudsData
{
    private static final String hudCPUName = "servermonitor:cpu_graph";
    private static final String hudRamName = "servermonitor:ram";
    private static final String hudTextRamName = "servermonitor:text_ram";
    private static final String hudTextCPUName = "servermonitor:text_cpu";

    public PlayerHudsHolderWrapper holder;

    public PlayerCustomHudWrapper hudCPU;
    public PlayerCustomHudWrapper hudTextCPU;
    public boolean isCPUShown;

    public PlayerQuantityHudWrapper hudRam;
    public PlayerCustomHudWrapper hudTextRam;
    public boolean isRAMShown;

    public PlayerHudsData(Player player)
    {
        holder = new PlayerHudsHolderWrapper(player);
        hudCPU = new PlayerCustomHudWrapper(holder, hudCPUName);
        hudRam = new PlayerQuantityHudWrapper(holder, hudRamName);
        hudTextRam = new PlayerCustomHudWrapper(holder, hudTextRamName);
        hudTextCPU = new PlayerCustomHudWrapper(holder, hudTextCPUName);

        if (hudCPU == null)
            throwHudNotFound(hudCPUName);
        if (hudRam == null)
            throwHudNotFound(hudRamName);
        if (hudTextRam == null)
            throwHudNotFound(hudTextRamName);
        if (hudTextCPU == null)
            throwHudNotFound(hudTextCPUName);
    }

    private void throwHudNotFound(String namespacedID)
    {
        try
        {
            throw new NullPointerException("Cannot find HUD: " + namespacedID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initHudCPU()
    {
        //TODO: check if exists before
        hudTextCPU.addFontImage(new FontImageWrapper(hudTextCPUName));
        hudCPU.addFontImage(new FontImageWrapper("servermonitor:graph_start"));
        for (int j = 0; j < 128; j++)
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
