package david.global;

import david.forspigot.XFirewall;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Protection {

    private static XFirewall xFirewall;

    private States states;

    private int flaggedConnections;

    private Instant lastFlagged;

    private BukkitTask protectionTask;

    public Protection(XFirewall xFirewall) {
        Protection.xFirewall = xFirewall;
        this.stop();

        if (xFirewall.getSettings().isProtectionEnabled()) {
            states = States.WAITING;
        } else {
            states = States.DISABLED;
        }

        flaggedConnections = 0;
    }

    private void start() {
        System.out.println(ChatColor.RED + "ALERT ALERT PROTECTION STARTED!");
        if (states == States.DISABLED) {
            return;
        }

        stop();

        states = States.ACTIVE;

        Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission(""))
                .forEach(players -> players.sendMessage(xFirewall.getMessages().translate("protection activated")
                        .replace("%player%", players.getName())));

        protectionTask = Bukkit.getScheduler().runTaskLater(xFirewall, this::stop, 20 * xFirewall.getSettings().getProtectionDuration());
    }

    private void stop() {
        System.out.println(ChatColor.RED + "ALERT ALERT PROTECTION STOPPED!");

        if (states != States.ACTIVE) {
        }

        states = States.WAITING;

        flaggedConnections = 1;

        if (protectionTask != null) {
            protectionTask.cancel();
            protectionTask = null;
        }

        Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission(""))
                .forEach(players -> players.sendMessage(xFirewall.getMessages().translate("protection deactivated")
                        .replace("%player%", players.getName())));
    }


    public boolean check() {
        System.out.println(ChatColor.RED + "ALERT ALERT PROTECTION CHECK!");
        if (states == States.DISABLED) {
            return false;
        }
        if (states == States.ACTIVE) {
            return true;
        }
        if (lastFlagged == null) {
            lastFlagged = Instant.now();
        }
        if (ChronoUnit.SECONDS.between(lastFlagged, Instant.now()) <= xFirewall.getSettings().getProtectionInterval()) {
            flaggedConnections++;
        } else {
            flaggedConnections = 1;
            lastFlagged = null;
        }
        if (flaggedConnections > xFirewall.getSettings().getProtectionMaxConnections()) {
            start();
            return true;
        }
        return false;
    }

    private enum States {
        ACTIVE, WAITING, DISABLED
    }
}
