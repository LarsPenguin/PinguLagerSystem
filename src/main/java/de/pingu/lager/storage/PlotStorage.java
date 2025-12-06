package de.pingu.lager.storage;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlotStorage {

    private final String plotId;
    private final List<ItemStack> items;

    public PlotStorage(String plotId) {
        this.plotId = plotId;
        this.items = new ArrayList<>();
    }

    public PlotStorage(String plotId, List<ItemStack> items) {
        this.plotId = plotId;
        this.items = new ArrayList<>(items);
    }

    public String getPlotId() {
        return plotId;
    }

    public List<ItemStack> getItems() {
        return items;
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
