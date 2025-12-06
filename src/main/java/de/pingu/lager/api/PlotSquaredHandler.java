package de.pingu.lager.api;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotId;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.UUID;

public class PlotSquaredHandler {

    private final JavaPlugin plugin;
    private PlotAPI plotAPI;

    public PlotSquaredHandler(JavaPlugin plugin) {
        this.plugin = plugin;

        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            try {
                // PlotAPI korrekt instanziieren
                plotAPI = new PlotAPI();
                plugin.getLogger().info("PlotSquared erfolgreich initialisiert.");
            } catch (Exception e) {
                plugin.getLogger().warning("Fehler beim Initialisieren von PlotSquared: " + e.getMessage());
            }
        } else {
            plugin.getLogger().warning("PlotSquared nicht gefunden! Plot-spezifische Funktionen deaktiviert.");
        }
    }

    /**
     * Liefert die PlotID als String für einen Spieler an der gegebenen Position
     */
    public String getPlotId(UUID worldUUID, int x, int z) {
        if (plotAPI == null) return worldUUID.toString() + ";" + x + ";" + z;
        Optional<Plot> plotOpt = plotAPI.getPlot(worldUUID, x, z);
        return plotOpt.map(Plot::getId)
                      .map(PlotId::toString)
                      .orElse(worldUUID.toString() + ";" + x + ";" + z);
    }

    /**
     * Liefert die PlotID eines Plots direkt
     */
    public String getPlotId(Plot plot) {
        if (plot == null) return "null";
        PlotId id = plot.getId();
        return id.toString();
    }

    /**
     * Liefert den Plot an der Position eines Spielers (Optional)
     */
    public Optional<Plot> getPlotAt(UUID worldUUID, int x, int z) {
        if (plotAPI == null) return Optional.empty();
        return plotAPI.getPlot(worldUUID, x, z);
    }
}
