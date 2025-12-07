package de.pingu.lager;

import de.pingu.lager.api.VaultHandler;
import de.pingu.lager.api.PlotSquaredHandler;
import de.pingu.lager.commands.LagerCommand;
import de.pingu.lager.listeners.LagerKistenListener;
import de.pingu.lager.listeners.LagerServerListener;
import de.pingu.lager.storage.StorageManager;
import de.pingu.lager.tasks.InputTask;
import de.pingu.lager.tasks.OutputTask;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
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
        saveDefaultConfig();

        vaultHandler = new VaultHandler(this);
        vaultHandler.setupEconomy();

        plotHandler = new PlotSquaredHandler(this);
        storageManager = new StorageManager(this);

        getServer().getPluginManager().registerEvents(new LagerServerListener(this), this);
        getServer().getPluginManager().registerEvents(new LagerKistenListener(this), this);

        new LagerCommand(this);

        // Register craft recipe: 2x2 redstone chests -> 2 Lagerkisten
        ItemStack result = new ItemStack(Material.CHEST, 2);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§a§lLagerKiste");
            result.setItemMeta(meta);
        }
        NamespacedKey key = new NamespacedKey(this, "lagerkiste_recipe");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("RR","RR");
        recipe.setIngredient('R', Material.REDSTONE_TORCH); // uses redstone torch as proxy for "redstone chest" ingredient
        try {
            getServer().addRecipe(recipe);
        } catch (Exception ignored) {}

        new InputTask(this).start(getConfig().getLong("tasks.inputInterval", 20));
        new OutputTask(this).start(getConfig().getLong("tasks.outputInterval", 20));

        getLogger().info("PinguLagerSystem aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PinguLagerSystem deaktiviert!");
    }
}
