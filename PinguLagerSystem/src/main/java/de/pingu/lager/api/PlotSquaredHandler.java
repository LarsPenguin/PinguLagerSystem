package de.pingu.lager.api;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.UUID;

public class PlotSquaredHandler {

    private final JavaPlugin plugin;
    private PlotAPI plotAPI;

    public PlotSquaredHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            plotAPI = PlotAPI.get();
            plugin.getLogger().info("PlotSquared API initialisiert.");
        } catch (Exception e) {
            plugin.getLogger().warning("PlotSquared nicht verfügbar: " + e.getMessage());
            plotAPI = null;
        }
    }

    public Optional<Plot> getPlotAtPlayer(Player player) {
        if (plotAPI == null) return Optional.empty();
        Location loc = new Location(player.getWorld().getName(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ());
        return plotAPI.getPlot(loc);
    }

    public String getPlotId(Player player) {
        return getPlotAtPlayer(player).map(Plot::getId).map(Object::toString).orElse(player.getWorld().getUID().toString()+";"+player.getLocation().getBlockX()+";"+player.getLocation().getBlockZ());
    }
}
