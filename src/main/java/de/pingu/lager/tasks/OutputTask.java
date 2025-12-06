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

        Bukkit.getWorlds().forEach(world -> {

            world.getLoadedChunks();

            for (var chunk : world.getLoadedChunks()) {

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

                            boolean hasSpace = false;
                            for (ItemStack slot : chest.getInventory().getContents()) {
                                if (slot == null || slot.getType() == Material.AIR) {
                                    hasSpace = true;
                                    break;
                                }
                            }
                            if (!hasSpace) continue;

                            for (ItemStack item : storageManager.getItemsFromPlot(plotId)) {
                                if (item != null && item.getType() != Material.AIR) {

                                    chest.getInventory().addItem(item.clone());
                                    storageManager.getPlotStorage(plotId).removeItem(item);
                                    storageManager.savePlot(plotId);

                                    return; // exakt 1 Item pro Tick pro Kiste
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void start(long ticks) {
        this.runTaskTimerAsynchronously(plugin, 0, ticks);
    }
}
