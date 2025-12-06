package de.pingu.lager.storage;

import de.pingu.lager.PinguLagerSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StorageManager {

    private final PinguLagerSystem plugin;
    private final File storageFolder;
    private final Map<String, PlotStorage> plotStorageMap;

    public StorageManager(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.storageFolder = new File(plugin.getDataFolder(), "storage");
        if (!storageFolder.exists()) storageFolder.mkdirs();
        this.plotStorageMap = new HashMap<>();
        loadAll();
    }

    /**
     * Speichert alle Items eines Plots in einer YML-Datei
     */
    public void savePlot(String plotId) {
        PlotStorage storage = plotStorageMap.get(plotId);
        if (storage == null) return;

        File file = new File(storageFolder, plotId + ".yml");
        YamlConfiguration yml = new YamlConfiguration();

        List<String> items = new ArrayList<>();
        for (ItemStack item : storage.getItems()) {
            if (item != null && item.getType() != Material.AIR)
                items.add(item.getType().name() + ":" + item.getAmount());
        }

        yml.set("items", items);

        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().warning("Fehler beim Speichern des Plots " + plotId);
        }
    }

    /**
     * Lädt alle Plot-Dateien beim Pluginstart
     */
    private void loadAll() {
        File[] files = storageFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            String plotId = file.getName().replace(".yml", "");

            List<ItemStack> items = new ArrayList<>();
            List<String> storedItems = yml.getStringList("items");

            for (String s : storedItems) {
                try {
                    String[] split = s.split(":");
                    Material mat = Material.valueOf(split[0]);
                    int amount = Integer.parseInt(split[1]);
                    items.add(new ItemStack(mat, amount));
                } catch (Exception ignored) {
                }
            }

            // WICHTIG: setItems() statt direkter Zuweisung
            PlotStorage ps = new PlotStorage(plotId);
            ps.setItems(items);

            plotStorageMap.put(plotId, ps);
        }
    }

    public PlotStorage getPlotStorage(String plotId) {
        return plotStorageMap.computeIfAbsent(plotId, id -> new PlotStorage(id));
    }

    public void addItemToPlot(String plotId, ItemStack item) {
        PlotStorage storage = getPlotStorage(plotId);
        storage.addItem(item);
        savePlot(plotId);
    }

    public List<ItemStack> getItemsFromPlot(String plotId) {
        return getPlotStorage(plotId).getItems(); // Rückgabe ist nun automatisch eine Kopie
    }
}
