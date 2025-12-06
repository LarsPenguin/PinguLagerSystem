package de.pingu.lager;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PlotSquaredHandler {

    private final PinguLagerSystem plugin;
    private final PlotAPI plotAPI;

    public PlotSquaredHandler(PinguLagerSystem plugin) {
        this.plugin = plugin;
        this.plotAPI = PlotAPI.get(); // PlotAPI 1.55: get() liefert Singleton
    }

    /**
     * Liefert den Plot, auf dem ein Spieler gerade steht.
     */
    public Optional<Plot> getPlotAtPlayer(Player player) {
        PlotUser plotUser = plotAPI.wrapPlayer(player);
        Location loc = new Location(
                player.getWorld().getName(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );
        return plotAPI.getPlot(loc); // 1.55 Core Methode
    }

    /**
     * Prüft, ob ein Spieler Besitzer eines Plots ist.
     */
    public boolean isOwner(Player player, Plot plot) {
        PlotUser plotUser = plotAPI.wrapPlayer(player);
        return plot.getOwners().contains(plotUser.getUUID());
    }

    /**
     * Prüft, ob ein Spieler Mitglied eines Plots ist.
     */
    public boolean isMember(Player player, Plot plot) {
        PlotUser plotUser = plotAPI.wrapPlayer(player);
        return plot.getMembers().contains(plotUser.getUUID());
    }

    /**
     * Beispiel-Methode zum Senden einer Nachricht an alle Spieler auf einem Plot.
     */
    public void broadcastToPlot(Plot plot, String message) {
        for (UUID uuid : plot.getPlayers()) {
            Player bukkitPlayer = Bukkit.getPlayer(uuid);
            if (bukkitPlayer != null) {
                bukkitPlayer.sendMessage(message);
            }
        }
    }
}
