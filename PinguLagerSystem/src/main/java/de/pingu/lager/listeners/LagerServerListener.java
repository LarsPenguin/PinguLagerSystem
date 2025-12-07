package de.pingu.lager.listeners;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerServerBlock;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.storage.StorageManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class LagerServerListener implements Listener {

    private final PinguLagerSystem plugin;
    private final LagerServerBlock lagerServerBlock;
    private final LagerKisteBlock lagerKisteBlock;
    private final StorageManager storageManager;

    public LagerServerListener(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.lagerServerBlock = new LagerServerBlock(plugin);
        this.lagerKisteBlock = new LagerKisteBlock(plugin);
        this.storageManager = plugin.getStorageManager();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        if (block.getType().name().equalsIgnoreCase("DISPENSER") &&
                event.getItemInHand() != null &&
                event.getItemInHand().getItemMeta() != null &&
                event.getItemInHand().getItemMeta().getDisplayName() != null &&
                event.getItemInHand().getItemMeta().getDisplayName().contains("§b§lLagerServer")) {

            boolean success = lagerServerBlock.placeLagerServer(player, block);
            if (!success) {
                event.setCancelled(true);
                player.sendMessage("§cFehler beim Platzieren des LagerServers!");
            } else {
                player.sendMessage("§aLagerServer erfolgreich platziert!");
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (lagerServerBlock.isLagerServerBlock(block)) {
            boolean success = lagerServerBlock.removeLagerServer(block);
            if (!success) {
                event.setCancelled(true);
                player.sendMessage("§cDu kannst diesen LagerServer nicht abbauen!");
            } else {
                player.sendMessage("§eLagerServer abgebaut.");
            }
        }
    }

    /**
     * Wenn ein Trichter (Hopper) ein Item in den Dispenser (LagerServer) verschiebt,
     * fangen wir das ab und speichern das Item in der Plot-Storage.
     */
    @EventHandler
    public void onHopperTransfer(InventoryMoveItemEvent event) {
        Inventory dest = event.getDestination();
        if (dest == null) return;
        org.bukkit.block.Block block = ((org.bukkit.block.BlockState)dest.getLocation().getBlock().getState()).getBlock();
        // safer approach: check nearby blocks? Instead check if inventory holder is BlockInventoryHolder with dispenser type
        try {
            org.bukkit.Location loc = dest.getLocation();
            if (loc == null) return;
            org.bukkit.block.Block b = loc.getBlock();
            if (!lagerServerBlock.isLagerServerBlock(b)) return;

            String plotId = plugin.getPlotHandler().getPlotAtPlayer((org.bukkit.entity.Player) event.getSource().getViewers().get(0)).map(plot -> plot.getId().toString()).orElse(b.getWorld().getUID().toString()+";"+b.getX()+";"+b.getZ());
            ItemStack item = event.getItem();
            if (item != null && item.getType() != Material.AIR) {
                storageManager.addItemToPlot(plotId, item.clone());
                event.setCancelled(true); // verhindern, dass item in dispenser landet
            }
        } catch (Exception ignored) {}
    }
}
