package de.pingu.lager.tasks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class OutputTask extends BukkitRunnable {

    private final PinguLagerSystem plugin;

    public OutputTask(PinguLagerSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        StorageManager storageManager = plugin.getStorageManager();
        LagerKisteBlock kisteBlock = new LagerKisteBlock(plugin);

        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (org.bukkit.Chunk chunk : world.getLoadedChunks()) {
                int startX = chunk.getX() << 4;
                int startZ = chunk.getZ() << 4;
                for (int x = startX; x < startX + 16; x++) {
                    for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                        for (int z = startZ; z < startZ + 16; z++) {
                            Block block = world.getBlockAt(x, y, z);
                            if (block.getType() != Material.CHEST) continue;
                            if (!kisteBlock.isLagerKiste(block)) continue;
                            if (kisteBlock.getMode(block) != LagerKisteBlock.Mode.OUTPUT) continue;
                            String plotId = kisteBlock.getPlotId(block);
                            if (plotId == null) continue;
                            Chest chest = (Chest) block.getState();
                            if (chest.getInventory().firstEmpty() == -1) continue;
                            for (ItemStack item : storageManager.getItemsFromPlot(plotId)) {
                                if (item != null && item.getType() != Material.AIR) {
                                    // Wenn die Kiste ein assignedMaterial hat, nur dieses ausgeben
                                    String assigned = chest.getPersistentDataContainer().getOrDefault(new org.bukkit.NamespacedKey(plugin, "assignedMaterial"),
                                            org.bukkit.persistence.PersistentDataType.STRING, null);
                                    if (assigned != null) {
                                        if (!item.getType().name().equals(assigned)) continue;
                                    }
                                    chest.getInventory().addItem(item.clone());
                                    storageManager.getPlotStorage(plotId).removeItem(item);
                                    storageManager.savePlot(plotId);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void start(long ticks) {
        this.runTaskTimerAsynchronously(plugin, 0, ticks);
    }
}
