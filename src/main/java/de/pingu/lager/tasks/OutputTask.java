package de.pingu.lager.tasks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

        // Schleife über alle Welten und Loaded Chunks
        Bukkit.getWorlds().forEach(world -> world.getLoadedChunks());

        for (Block block : plugin.getServer().getWorlds().stream()
                .flatMap(w -> java.util.Arrays.stream(w.getLoadedChunks()))
                .flatMap(c -> java.util.Arrays.stream(c.getBlocks()))
                .toList()) {

            if (block == null) continue;

            LagerKisteBlock kisteBlock = new LagerKisteBlock(plugin);
            if (kisteBlock.isLagerKiste(block) && kisteBlock.getMode(block) == LagerKisteBlock.Mode.OUTPUT) {
                String plotId = kisteBlock.getPlotId(block);
                if (plotId == null) continue;

                ItemStack[] contents = ((org.bukkit.block.Chest) block.getState()).getInventory().getContents();
                boolean hasSpace = false;
                for (ItemStack slot : contents) {
                    if (slot == null || slot.getType() == Material.AIR) {
                        hasSpace = true;
                        break;
                    }
                }

                if (!hasSpace) continue; // keine freien Slots

                // Nimm das erste Item aus Storage und lege in Kiste
                for (ItemStack item : storageManager.getItemsFromPlot(plotId)) {
                    if (item != null && item.getType() != Material.AIR) {
                        ((org.bukkit.block.Chest) block.getState()).getInventory().addItem(item.clone());
                        storageManager.getPlotStorage(plotId).removeItem(item);
                        storageManager.savePlot(plotId);
                        break; // nur 1 Item pro Tick pro Kiste
                    }
                }
            }
        }
    }

    /**
     * Startet den Task alle n Ticks
     */
    public void start(long ticks) {
        this.runTaskTimerAsynchronously(plugin, 0, ticks);
    }
}
