package de.pingu.lager.tasks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InputTask extends BukkitRunnable {

    private final PinguLagerSystem plugin;
    private final LagerKisteBlock lagerKisteBlock;

    public InputTask(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.lagerKisteBlock = new LagerKisteBlock(plugin);
    }

    @Override
    public void run() {
        StorageManager storageManager = plugin.getStorageManager();

        // Über ALLE registrierten Lagerkisten iterieren
        for (Block block : lagerKisteBlock.getAllRegisteredKisten()) {

            if (block == null || !(block.getState() instanceof Chest chest)) continue;

            // Nur INPUT-Kisten
            if (lagerKisteBlock.getMode(block) != LagerKisteBlock.Mode.INPUT) continue;

            String plotId = lagerKisteBlock.getPlotId(block);
            if (plotId == null) continue;

            // Ein Item pro Tick verschieben
            for (ItemStack item : chest.getInventory().getContents()) {

                if (item != null && item.getType() != Material.AIR) {

                    storageManager.addItemToPlot(plotId, item.clone());

                    // Item aus Kiste entfernen
                    chest.getInventory().removeItem(item);

                    break; // Pro Tick nur EIN Item
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
