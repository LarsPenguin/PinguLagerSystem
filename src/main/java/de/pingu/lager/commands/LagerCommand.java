package de.pingu.lager.commands;

import de.pingu.lager.PinguLagerSystem;
import de.pingu.lager.api.VaultHandler;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LagerCommand implements CommandExecutor {

    private final PinguLagerSystem plugin;

    public LagerCommand(PinguLagerSystem plugin) {
        this.plugin = plugin;
        // Command registrieren
        plugin.getCommand("lager").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl nutzen!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§eBenutzung: /lager buy");
            return true;
        }

        if (args[0].equalsIgnoreCase("buy")) {
            VaultHandler vault = plugin.getVaultHandler();
            double price = plugin.getConfig().getDouble("economy.serverPrice", 15000.0);

            if (!vault.hasEconomy()) {
                player.sendMessage("§cEconomy nicht verfügbar!");
                return true;
            }

            if (vault.getBalance(player.getName()) < price) {
                player.sendMessage(plugin.getConfig().getString("messages.buyFail"));
                return true;
            }

            // Geld abziehen
            vault.withdrawPlayer(player.getName(), price);

            // LagerServer Dispenser erstellen
            ItemStack dispenser = new ItemStack(Material.DISPENSER);
            var meta = dispenser.getItemMeta();
            meta.displayName(org.bukkit.NamespacedKey.minecraft("§b§lLagerServer"));
            dispenser.setItemMeta(meta);

            player.getInventory().addItem(dispenser);
            player.sendMessage(plugin.getConfig().getString("messages.buySuccess"));
        } else {
            player.sendMessage("§eUnbekannter Unterbefehl: /lager " + args[0]);
        }

        return true;
    }
}
