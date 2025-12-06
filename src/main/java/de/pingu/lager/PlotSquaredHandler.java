package de.pingu.lager;

import com.plotsquared.core.PlotAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class PlotSquaredHandler {

    private final PlotAPI plotAPI;

    public PlotSquaredHandler(JavaPlugin plugin) {
        // Holt die PlotSquared-Instanz vom Server
        this.plotAPI = PlotAPI.get();
    }

    public PlotAPI getPlotAPI() {
        return plotAPI;
    }

}
