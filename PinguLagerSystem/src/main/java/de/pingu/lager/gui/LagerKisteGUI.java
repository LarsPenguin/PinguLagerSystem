package de.pingu.lager.gui;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LagerKisteGUI {

    public static void openGUI(PinguLagerSystem plugin, Player player, Block block, LagerKisteBlock.Mode mode) {
        if (mode == null) return;

        StorageManager storageManager = plugin.getStorageManager();
        String plotId = new LagerKisteBlock(plugin).getPlotId(block);
        if (plotId == null) return;

        Inventory inv;

        if (mode == LagerKisteBlock.Mode.INPUT) {
            inv = Bukkit.createInventory(player, 27, "§aLagerKiste INPUT");
        } else {
            inv = Bukkit.createInventory(player, 27, "§aLagerKiste OUTPUT");
            for (ItemStack item : storageManager.getItemsFromPlot(plotId)) {
                inv.addItem(item);
            }
        }

        player.openInventory(inv);
    }
}
