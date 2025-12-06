package de.pingu.lager;

import de.pingu.lager.api.VaultHandler;
import de.pingu.lager.commands.LagerCommand;
import de.pingu.lager.listeners.LagerKistenListener;
import de.pingu.lager.listeners.LagerServerListener;
import de.pingu.lager.storage.StorageManager;
import de.pingu.lager.api.PlotSquaredHandler;
import de.pingu.lager.tasks.InputTask;
import de.pingu.lager.tasks.OutputTask;
import org.bukkit.plugin.java.JavaPlugin;

public class PinguLagerSystem extends JavaPlugin {

    private VaultHandler vaultHandler;
    private StorageManager storageManager;
    private PlotSquaredHandler plotHandler;

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public PlotSquaredHandler getPlotHandler() {
        return plotHandler;
    }

    @Override
    public void onEnable() {
        // Config laden
        saveDefaultConfig();

        // Handler initialisieren
        vaultHandler = new VaultHandler(this);
        vaultHandler.setupEconomy();

        plotHandler = new PlotSquaredHandler(this);
        storageManager = new StorageManager(this);

        // Listener registrieren
        getServer().getPluginManager().registerEvents(new LagerServerListener(this), this);
        getServer().getPluginManager().registerEvents(new LagerKistenListener(this), this);

        // Command registrieren
        new LagerCommand(this);

        // InputTask starten (asynchron)
        new InputTask(this).start(getConfig().getLong("tasks.inputInterval", 20));

        // OutputTask starten (asynchron)
        new OutputTask(this).start(getConfig().getLong("tasks.outputInterval", 20));

        getLogger().info("PinguLagerSystem aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PinguLagerSystem deaktiviert!");
    }
}
