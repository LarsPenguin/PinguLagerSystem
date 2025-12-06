package de.pingu.lager.blocks;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;

public class LagerKisteBlock {

    private final PinguLagerSystem plugin;
    private final StorageManager storageManager;

    public enum Mode { INPUT, OUTPUT }

    public LagerKisteBlock(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
    }

    /**
     * Prüft, ob der Block eine LagerKiste ist
     */
    public boolean isLagerKiste(Block block) {
        if (block == null) return false;
        Material type = block.getType();
        return (type == Material.CHEST || type == Material.TRAPPED_CHEST) &&
                block.getCustomName() != null && block.getCustomName().contains("§a§lLagerKiste");
    }

    /**
     * Erstellt eine LagerKiste für den Spieler
     */
    public boolean placeLagerKiste(Player player, Block block, Mode mode, String plotId) {
        if (block == null || !(block.getState() instanceof Chest)) return false;

        block.setType(Material.CHEST);
        block.getState().update(true);
        block.setCustomName("§a§lLagerKiste");

        Chest chest = (Chest) block.getState();
        chest.update();

        // Modus und PlotID in Metadaten speichern
        block.getPersistentDataContainer().set(plugin.getNamespacedKey("LagerKisteMode"),
                new org.bukkit.persistence.PersistentDataType.STRING(), mode.name());
        block.getPersistentDataContainer().set(plugin.getNamespacedKey("LagerKistePlotId"),
                new org.bukkit.persistence.PersistentDataType.STRING(), plotId);

        plugin.getLogger().info(player.getName() + " hat eine LagerKiste auf Plot " + plotId + " platziert (Modus: " + mode + ")");
        return true;
    }

    /**
     * Gibt den Modus der LagerKiste zurück
     */
    public Mode getMode(Block block) {
        if (!isLagerKiste(block)) return null;
        String modeStr = block.getPersistentDataContainer().get(plugin.getNamespacedKey("LagerKisteMode"),
                org.bukkit.persistence.PersistentDataType.STRING);
        return modeStr != null ? Mode.valueOf(modeStr) : null;
    }

    /**
     * Liefert die PlotID der Kiste
     */
    public String getPlotId(Block block) {
        if (!isLagerKiste(block)) return null;
        return block.getPersistentDataContainer().get(plugin.getNamespacedKey("LagerKistePlotId"),
                org.bukkit.persistence.PersistentDataType.STRING);
    }

    /**
     * Fügt Item zur Kiste hinzu (INPUT)
     */
    public boolean addItemToKiste(Block block, ItemStack item) {
        if (!isLagerKiste(block)) return false;
        if (!(block.getState() instanceof BlockInventoryHolder)) return false;

        BlockInventoryHolder holder = (BlockInventoryHolder) block.getState();
        holder.getInventory().addItem(item);
        return true;
    }
}
