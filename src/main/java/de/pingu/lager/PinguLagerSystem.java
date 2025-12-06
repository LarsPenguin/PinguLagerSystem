package de.pingu.lager;

import org.bukkit.plugin.java.JavaPlugin;
import de.pingu.lager.api.VaultHandler;
import de.pingu.lager.api.PlotSquaredHandler;
import de.pingu.lager.listeners.PlayerListener;
import de.pingu.lager.listeners.LagerServerListener;
import de.pingu.lager.listeners.LagerKistenListener;
import de.pingu.lager.storage.StorageManager;

public final class PinguLagerSystem extends JavaPlugin {

    private static PinguLagerSystem instance;
    private StorageManager storageManager;
    private VaultHandler vaultHandler;
    private PlotSquaredHandler plotHandler;

    @Override
    public void onEnable() {
        instance = this;

        // Config laden
        saveDefaultConfig();

        // Storage Manager initialisieren
        storageManager = new StorageManager(this);

        // Vault
        vaultHandler = new VaultHandler(this);
        vaultHandler.setupEconomy();

        // PlotSquared
        plotHandler = new PlotSquaredHandler(this);

        // Listener registrieren
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new LagerServerListener(this), this);
        getServer().getPluginManager().registerEvents(new LagerKistenListener(this), this);

        getLogger().info("PinguLagerSystem aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PinguLagerSystem deaktiviert!");
    }

    public static PinguLagerSystem getInstance() {
        return instance;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public PlotSquaredHandler getPlotHandler() {
        return plotHandler;
    }
}
