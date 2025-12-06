package de.pingu.lager.storage;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlotStorage {

    private final String plotId;
    private List<ItemStack> items;

    public PlotStorage(String plotId) {
        this.plotId = plotId;
        this.items = new ArrayList<>();
    }

    public PlotStorage(String plotId, List<ItemStack> items) {
        this.plotId = plotId;
        this.items = new ArrayList<>(items != null ? items : new ArrayList<>());
    }

    public String getPlotId() {
        return plotId;
    }

    /**
     * Gibt eine Kopie zurück, damit nichts von außen kaputt gemacht wird.
     */
    public List<ItemStack> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Setzt alle Items neu (API erwartet das).
     */
    public void setItems(List<ItemStack> items) {
        this.items = (items != null) ? new ArrayList<>(items) : new ArrayList<>();
    }

    public void addItem(ItemStack item) {
        if (item == null || item.getType().isAir()) return;
        items.add(item);
    }

    public boolean removeItem(ItemStack item) {
        return items.remove(item);
    }

    public void clear() {
        items.clear();
    }
}
