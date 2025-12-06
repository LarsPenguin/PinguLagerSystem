package de.pingu.lager.listeners;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerServerBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;

public class LagerServerListener implements Listener {

    private final PinguLagerSystem plugin;
    private final LagerServerBlock lagerServerBlock;

    public LagerServerListener(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.lagerServerBlock = new LagerServerBlock(plugin);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        if (event.getItemInHand() == null ||
                event.getItemInHand().getItemMeta() == null ||
                event.getItemInHand().getItemMeta().getDisplayName() == null) {
            return;
        }

        // Prüfen, ob Spieler einen LagerServer platzieren will
        if (block.getType().name().equalsIgnoreCase("DISPENSER") &&
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
}
