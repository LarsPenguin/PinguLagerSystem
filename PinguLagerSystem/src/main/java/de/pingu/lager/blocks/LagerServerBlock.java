package de.pingu.lager.blocks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.storage.StorageManager;
import de.pingu.lager.api.PlotSquaredHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LagerServerBlock {

    private final PinguLagerSystem plugin;
    private final StorageManager storageManager;
    private final PlotSquaredHandler plotHandler;

    public LagerServerBlock(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
        this.plotHandler = new PlotSquaredHandler(plugin);
    }

    public boolean isLagerServerBlock(Block block) {
        if (block == null) return false;
        if (block.getType() != Material.DISPENSER) return false;
        BlockState state = block.getState();
        if (state instanceof Dispenser d) {
            String name = d.getCustomName();
            return name != null && name.contains("§b§lLagerServer");
        }
        return false;
    }

    public boolean placeLagerServer(Player player, Block block) {
        if (block == null) return false;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() != Material.DISPENSER) return false;

        block.setType(Material.DISPENSER);
        block.getState().update(true);
        BlockState state = block.getState();
        if (state instanceof Dispenser d) {
            d.setCustomName("§b§lLagerServer");
            d.update();
        }

        String plotId = plotHandler.getPlotAtPlayer(player).map(plot -> plot.getId().toString()).orElse(player.getWorld().getUID().toString()+";"+block.getX()+";"+block.getZ());
        storageManager.getPlotStorage(plotId);

        plugin.getLogger().info(player.getName() + " hat einen LagerServerBlock auf Plot " + plotId + " platziert.");
        return true;
    }

    public boolean removeLagerServer(Block block) {
        if (!isLagerServerBlock(block)) return false;

        block.setType(Material.AIR);
        plugin.getLogger().info("LagerServerBlock entfernt.");
        return true;
    }
}
