package de.pingu.lager.tasks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

        // Schleife über alle Welten und Loaded Chunks
        Bukkit.getWorlds().forEach(world -> world.getLoadedChunks());

        for (Block block : plugin.getServer().getWorlds().stream()
                .flatMap(w -> java.util.Arrays.stream(w.getLoadedChunks()))
                .flatMap(c -> java.util.Arrays.stream(c.getBlocks()))
                .toList()) {

            if (block == null) continue;

            LagerKisteBlock kisteBlock = new LagerKisteBlock(plugin);
            if (kisteBlock.isLagerKiste(block) && kisteBlock.getMode(block) == LagerKisteBlock.Mode.INPUT) {
                String plotId = kisteBlock.getPlotId(block);
                if (plotId == null) continue;

                // Ein Item pro Tick aus der Kiste in Storage verschieben
                ItemStack[] contents = ((org.bukkit.block.Chest) block.getState()).getInventory().getContents();
                for (ItemStack item : contents) {
                    if (item != null && item.getType() != Material.AIR) {
                        storageManager.addItemToPlot(plotId, item.clone());
                        // Item aus Kiste entfernen
                        ((org.bukkit.block.Chest) block.getState()).getInventory().removeItem(item);
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
