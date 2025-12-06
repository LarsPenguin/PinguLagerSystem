package de.pingu.lager.blocks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class LagerServerBlock {

    private final PinguLagerSystem plugin;
    private final StorageManager storageManager;

    public LagerServerBlock(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
    }

    /**
     * Prüft, ob der Block ein LagerServerBlock ist
     */
    public boolean isLagerServerBlock(Block block) {
        return block != null && block.getType() == Material.DISPENSER &&
                block.getCustomName() != null && block.getCustomName().contains("§b§lLagerServer");
    }

    /**
     * Erstellt einen LagerServerBlock für den Spieler
     */
    public boolean placeLagerServer(Player player, Block block) {
        if (block == null) return false;

        // Einmalplatzierung prüfen
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() != Material.DISPENSER) return false;

        // Block setzen
        block.setType(Material.DISPENSER);
        block.setCustomName("§b§lLagerServer");

        // PlotID speichern
        String plotId = plugin.getPlotHandler().getPlotId(player.getWorld().getUID(), block.getX(), block.getZ());
        // Init Storage für Plot
        storageManager.getPlotStorage(plotId);

        plugin.getLogger().info(player.getName() + " hat einen LagerServerBlock auf Plot " + plotId + " platziert.");
        return true;
    }

    /**
     * Entfernt den Block (nur für Korrektur)
     */
    public boolean removeLagerServer(Block block) {
        if (!isLagerServerBlock(block)) return false;

        block.setType(Material.AIR);
        plugin.getLogger().info("LagerServerBlock entfernt.");
        return true;
    }
}
