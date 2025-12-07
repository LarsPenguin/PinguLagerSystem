package de.pingu.lager.blocks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class LagerKisteBlock {

    private final PinguLagerSystem plugin;
    private final StorageManager storageManager;

    public enum Mode { INPUT, OUTPUT }

    public LagerKisteBlock(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
    }

    public boolean isLagerKiste(Block block) {
        if (block == null) return false;
        Material type = block.getType();
        BlockState state = block.getState();
        String name = null;
        if (state instanceof Chest chest) name = chest.getCustomName();
        return (type == Material.CHEST || type == Material.TRAPPED_CHEST) &&
                name != null && name.contains("§a§lLagerKiste");
    }

    public boolean placeLagerKiste(Player player, Block block, Mode mode, String plotId) {
        if (block == null) return false;

        block.setType(Material.CHEST);
        block.getState().update(true);

        Chest chest = (Chest) block.getState();
        chest.setCustomName("§a§lLagerKiste");
        chest.update();

        NamespacedKey keyMode = new NamespacedKey(plugin, "LagerKisteMode");
        NamespacedKey keyPlot = new NamespacedKey(plugin, "LagerKistePlotId");

        chest.getPersistentDataContainer().set(keyMode, PersistentDataType.STRING, mode.name());
        chest.getPersistentDataContainer().set(keyPlot, PersistentDataType.STRING, plotId);

        plugin.getLogger().info(player.getName() + " hat eine LagerKiste auf Plot " + plotId + " platziert (Modus: " + mode + ")");
        return true;
    }

    public Mode getMode(Block block) {
        if (!isLagerKiste(block)) return null;
        BlockState state = block.getState();
        if (state instanceof Chest chest) {
            String modeStr = chest.getPersistentDataContainer().get(new NamespacedKey(plugin, "LagerKisteMode"),
                    PersistentDataType.STRING);
            return modeStr != null ? Mode.valueOf(modeStr) : null;
        }
        return null;
    }

    public String getPlotId(Block block) {
        if (!isLagerKiste(block)) return null;
        BlockState state = block.getState();
        if (state instanceof Chest chest) {
            return chest.getPersistentDataContainer().get(new NamespacedKey(plugin, "LagerKistePlotId"),
                    PersistentDataType.STRING);
        }
        return null;
    }

    public boolean addItemToKiste(Block block, ItemStack item) {
        if (!isLagerKiste(block)) return false;
        BlockState state = block.getState();
        if (!(state instanceof BlockInventoryHolder holder)) return false;

        holder.getInventory().addItem(item);
        return true;
    }
}
