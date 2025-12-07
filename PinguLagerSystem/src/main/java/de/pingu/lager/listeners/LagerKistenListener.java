package de.pingu.lager.listeners;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.blocks.LagerKisteBlock;
import de.pingu.lager.gui.LagerKisteGUI;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class LagerKistenListener implements Listener {

    private final PinguLagerSystem plugin;
    private final LagerKisteBlock lagerKisteBlock;

    public LagerKistenListener(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.lagerKisteBlock = new LagerKisteBlock(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;

        if (lagerKisteBlock.isLagerKiste(block)) {
            event.setCancelled(true);

            LagerKisteBlock.Mode mode = lagerKisteBlock.getMode(block);

            // Wenn OUTPUT und Spieler hält ein Block-Item -> Kiste auf dieses Material einstellen
            if (mode == LagerKisteBlock.Mode.OUTPUT) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand != null && hand.getType() != Material.AIR && hand.getType().isBlock()) {
                    // set assigned material in persistent data
                    org.bukkit.block.BlockState state = block.getState();
                    if (state instanceof org.bukkit.block.Chest chest) {
                        chest.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "assignedMaterial"),
                                org.bukkit.persistence.PersistentDataType.STRING, hand.getType().name());
                        chest.update();
                        player.sendMessage("§aLagerKiste für " + hand.getType().name() + " eingestellt.");
                        return;
                    }
                }
            }

            LagerKisteGUI.openGUI(plugin, player, block, mode);

            player.sendMessage("§eLagerKiste geöffnet (" + (mode != null ? mode.name() : "Unbekannt") + ")");
        }
    }
}
