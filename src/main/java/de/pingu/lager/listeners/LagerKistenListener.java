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
            event.setCancelled(true); // Normale Chest-Interaktion verhindern

            // Modus der Kiste bestimmen
            LagerKisteBlock.Mode mode = lagerKisteBlock.getMode(block);

            // GUI öffnen für Spieler
            LagerKisteGUI.openGUI(plugin, player, block, mode);

            player.sendMessage("§eLagerKiste geöffnet (" + (mode != null ? mode.name() : "Unbekannt") + ")");
        }
    }
}
