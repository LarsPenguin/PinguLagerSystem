package de.pingu.lager.api;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultHandler {

    private final JavaPlugin plugin;
    private Economy economy;

    public VaultHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault nicht gefunden! Economy-Funktionen deaktiviert.");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("Keine Economy über Vault gefunden! Economy-Funktionen deaktiviert.");
            return false;
        }

        economy = rsp.getProvider();
        plugin.getLogger().info("Vault Economy erfolgreich geladen: " + economy.getName());
        return true;
    }

    public boolean hasEconomy() {
        return economy != null;
    }

    public boolean withdrawPlayer(String playerName, double amount) {
        if (economy == null) return false;
        return economy.withdrawPlayer(playerName, amount).transactionSuccess();
    }

    public boolean depositPlayer(String playerName, double amount) {
        if (economy == null) return false;
        return economy.depositPlayer(playerName, amount).transactionSuccess();
    }

    public double getBalance(String playerName) {
        if (economy == null) return 0;
        return economy.getBalance(playerName);
    }
}
