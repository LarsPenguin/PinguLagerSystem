package de.pingu.lager.gui;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.kyori.adventure.text.Component;

public class LagerKisteGUI {

    /**
     * Öffnet die LagerKiste GUI für INPUT oder OUTPUT
     */
    public static void openGUI(PinguLagerSystem plugin, Player player, Block block, LagerKisteBlock.Mode mode) {
        if (mode == null) return;

        StorageManager storageManager = plugin.getStorageManager();
        String plotId = new LagerKisteBlock(plugin).getPlotId(block);
        if (plotId == null) return;

        Inventory inv;

        if (mode == LagerKisteBlock.Mode.INPUT) {

            inv = Bukkit.createInventory(
                    player,
                    27,
                    Component.text("§aLagerKiste INPUT") // neu für 1.21+
            );

        } else {

            inv = Bukkit.createInventory(
                    player,
                    27,
                    Component.text("§aLagerKiste OUTPUT") // neu für 1.21+
            );

            // OUTPUT: Items aus PlotStorage in GUI
            for (ItemStack item : storageManager.getItemsFromPlot(plotId)) {
                inv.addItem(item);
            }
        }

        player.openInventory(inv);
    }
}
