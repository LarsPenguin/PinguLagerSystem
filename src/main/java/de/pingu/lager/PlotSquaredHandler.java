package de.pingu.lager;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotPlayer;
import com.plotsquared.core.location.Location;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlotSquaredHandler {

    private final PinguLagerSystem plugin;
    private final PlotAPI plotAPI;

    public PlotSquaredHandler(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.plotAPI = PlotAPI.get();
    }

    /**
     * Liefert den Plot, auf dem ein Spieler gerade steht.
     */
    public Optional<Plot> getPlotAtPlayer(Player player) {
        PlotPlayer plotPlayer = plotAPI.wrapPlayer(player);
        Location loc = new Location(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        return plotAPI.getPlot(loc);
    }

    /**
     * Prüft, ob ein Spieler Besitzer eines Plots ist.
     */
    public boolean isOwner(Player player, Plot plot) {
        PlotPlayer plotPlayer = plotAPI.wrapPlayer(player);
        return plot.getOwners().contains(plotPlayer.getUUID());
    }

    /**
     * Prüft, ob ein Spieler Mitglied eines Plots ist.
     */
    public boolean isMember(Player player, Plot plot) {
        PlotPlayer plotPlayer = plotAPI.wrapPlayer(player);
        return plot.getMembers().contains(plotPlayer.getUUID());
    }

    /**
     * Beispiel-Methode zum Senden einer Nachricht an alle Spieler auf einem Plot.
     */
    public void broadcastToPlot(Plot plot, String message) {
        plot.getPlayers().forEach(p -> {
            Player bukkitPlayer = Bukkit.getPlayer(p.getUUID());
            if (bukkitPlayer != null) {
                bukkitPlayer.sendMessage(message);
            }
        });
    }
}
