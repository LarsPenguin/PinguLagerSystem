package de.pingu.lager.tasks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class OutputTask extends BukkitRunnable {

    private final PinguLagerSystem plugin;
    private final LagerKisteBlock lagerKisteBlock;

    public OutputTask(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.lagerKisteBlock = new LagerKisteBlock(plugin);
    }

    @Override
    public void run() {

        StorageManager storageManager = plugin.getStorageManager();

        // Alle registrierten Lagerkisten durchgehen
        for (Block block : lagerKisteBlock.getAllRegisteredKisten()) {

            if (block == null || !(block.getState() instanceof Chest chest)) continue;

            // Nur OUTPUT-Kisten
            if (lagerKisteBlock.getMode(block) != LagerKisteBlock.Mode.OUTPUT) continue;

            String plotId = lagerKisteBlock.getPlotId(block);
            if (plotId == null) continue;

            // Prüfen, ob ein Slot frei ist
            boolean hasSpace = chest.getInventory().firstEmpty() != -1;
            if (!hasSpace) continue;

            // Hol dir das PlotStorage und gib das erste Item aus
            for (ItemStack item : storageManager.getItemsFromPlot(plotId)) {

                if (item != null && item.getType() != Material.AIR) {

                    // 1 Item pro Tick in die Kiste legen
                    chest.getInventory().addItem(item.clone());

                    // Item aus Storage entfernen
                    storageManager.getPlotStorage(plotId).removeItem(item);
                    storageManager.savePlot(plotId);

                    break; // Nur ein Item pro Tick
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
