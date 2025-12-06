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

public class InputTask extends BukkitRunnable {

    private final PinguLagerSystem plugin;

    public InputTask(PinguLagerSystem plugin) {
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
                            if (kisteBlock.getMode(block) != LagerKisteBlock.Mode.INPUT) continue;

                            String plotId = kisteBlock.getPlotId(block);
                            if (plotId == null) continue;

                            Chest chest = (Chest) block.getState();
                            ItemStack[] contents = chest.getInventory().getContents();

                            for (ItemStack item : contents) {
                                if (item != null && item.getType() != Material.AIR) {

                                    storageManager.addItemToPlot(plotId, item.clone());
                                    chest.getInventory().removeItem(item);

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
