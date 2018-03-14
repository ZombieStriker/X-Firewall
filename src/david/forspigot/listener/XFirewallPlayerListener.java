package david.forspigot.listener;

import com.google.gson.JsonObject;
import david.forspigot.XFirewall;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.bukkit.event.player.PlayerLoginEvent.Result.KICK_OTHER;

public class XFirewallPlayerListener implements Listener {

    private final XFirewall xFirewall;

    private Set<String> cache = new HashSet<>();

    private int botscoutMaxMsgOnError = 0;

    public XFirewallPlayerListener(XFirewall xFirewall) {
        this.xFirewall = xFirewall;
    }

    @EventHandler
    public void stopForumSpamCheck(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        //---------------------------------------------//
        if (Bukkit.getIPBans().contains(playerIp) || !(event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;
        //---------------------------------------------//
        if (cache.contains(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("player cache kick")
                    .replace("%player_ip%", playerIp)
                    .replace("%player%", playerName));
        }
        //---------------------------------------------//
        JsonObject stopForumSpamJsonObject = xFirewall.getStopForumSpam().getIpDataAsJson(playerIp);
        //---------------------------------------------//
        if (xFirewall.getSettings().isStopForumSpamProxyCheckEnabled()) {
            if (Integer.valueOf(String.valueOf(stopForumSpamJsonObject.get("appears"))) >= 1) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("internet protocol kick")
                        .replace("%player_ip%", playerIp)
                        .replace("%player%", playerName));
                if (xFirewall.getSettings().isStopForumSpamProxyAdvancedIfFoundBan()) {
                    BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                    if (ipBanList != null && ipBanList.isBanned(playerIp)) {
                        ipBanList.addBan(playerIp,
                                xFirewall.getMessages().translate("internet protocol ban"),
                                new Date(),
                                "X-Firewall");
                        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("internet protocol ban"));
                        }
                    }
                }
            }
        }

        if (xFirewall.getSettings().isStopForumSpamProxyCheckDatabaseEnabled()) {
            if (xFirewall.getStopForumSpam().isInDownloadedProxies(playerIp)) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("internet protocol kick")
                        .replace("%player_ip%", playerIp)
                        .replace("%player%", playerName));
            }
        }
        //---------------------------------------------//
        if (xFirewall.getSettings().isStopForumSpamAsnCheckEnabled()) {
            if (xFirewall.getStopForumSpam().isAsnBlacklisted(stopForumSpamJsonObject.get("asn"))) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("autonomous system number kick")
                        .replace("%player_ip%", playerIp)
                        .replace("%player%", playerName));
                if (xFirewall.getSettings().isStopForumSpamAsnAdvancedIfFoundBan()) {
                    BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                    if (ipBanList != null && ipBanList.isBanned(playerIp)) {
                        ipBanList.addBan(playerIp,
                                xFirewall.getMessages().translate("autonomous system number ban"),
                                new Date(),
                                "X-Firewall");
                        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("autonomous system number ban"));
                        }
                    }
                }
            }
        }
        //---------------------------------------------//
        if (xFirewall.getSettings().isStopForumSpamCountryCheckEnabled()) {
            if (xFirewall.getStopForumSpam().isCountryBlacklisted(stopForumSpamJsonObject.get("country"))) {
                if (xFirewall.getStopForumSpam().isCountryBlacklisted(stopForumSpamJsonObject.get("country"))) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("country kick")
                            .replace("%player_ip%", playerIp)
                            .replace("%player%", playerName));
                    if (xFirewall.getSettings().isStopForumSpamCountryAdvancedIfFoundBan()) {
                        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                        if (ipBanList != null && ipBanList.isBanned(playerIp)) {
                            ipBanList.addBan(playerIp,
                                    xFirewall.getMessages().translate("country ban"),
                                    new Date(),
                                    "X-Firewall");
                            if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("country ban"));
                            }
                        }
                    }
                }
            }
        }
        //---------------------------------------------//
    }

    @EventHandler
    public void botScoutCheck(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        //---------------------------------------------//
        if (Bukkit.getIPBans().contains(playerIp) || !(event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;
        //---------------------------------------------//
        if (cache.contains(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("player cache kick")
                    .replace("%player_ip%", playerIp)
                    .replace("%player%", playerName));
        }
        //---------------------------------------------//
        if (xFirewall.getSettings().isBotscoutProxyCheckEnabled()) {
            String data = xFirewall.getBotScout().getUserDataAsString(playerIp).toLowerCase();
            if (data.isEmpty()) {
                return;
            }
            if (data.contains("y")) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("internet protocol kick")
                        .replace("%player_ip%", playerIp)
                        .replace("%player%", playerName));
                BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                if (ipBanList != null && ipBanList.isBanned(playerIp)) {
                    ipBanList.addBan(playerIp,
                            xFirewall.getMessages().translate("internet protocol ban"),
                            new Date(),
                            "X-Firewall");
                    if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("internet protocol ban"));
                    }
                }
            } else if (!data.contains("y") && !data.contains("n")) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("internet protocol kick")
                        .replace("%player_ip%", playerIp)
                        .replace("%player%", playerName));
                BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                if (ipBanList != null && ipBanList.isBanned(playerIp)) {
                    ipBanList.addBan(playerIp,
                            xFirewall.getMessages().translate("internet protocol ban"),
                            new Date(),
                            "X-Firewall");
                    if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("internet protocol ban"));
                    }
                }
            } else if (data.toLowerCase().contains("reached") || data.toLowerCase().contains("invalid")
                    || data.toLowerCase().contains("missing") || data.toLowerCase().contains("malformed")) {
                botscoutMaxMsgOnError++;
                if (botscoutMaxMsgOnError == 3) {
                    System.out.println("Botscout error: " + data);
                    botscoutMaxMsgOnError = 0;
                }
            }
        }
        //---------------------------------------------//
    }

    @EventHandler
    public void dnsblCheck(AsyncPlayerPreLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getName();
        //---------------------------------------------//
        if (Bukkit.getIPBans().contains(playerIp) || !(event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;
        //---------------------------------------------//
        if (cache.contains(playerIp)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("player cache kick")
                    .replace("%player_ip%", playerIp)
                    .replace("%player%", playerName));
        }
        //---------------------------------------------//
        if (xFirewall.getSettings().isDnsblCheckEnabled()) {
            if (xFirewall.getDnsbl().isBlocked(playerIp)) {
                if (xFirewall.getSettings().isDnsblAdvancedIfFoundBan()) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("domain name system based list kick")
                            .replace("%player_ip%", playerIp)
                            .replace("%player%", playerName));
                    BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                    if (ipBanList != null && ipBanList.isBanned(playerIp)) {
                        ipBanList.addBan(playerIp,
                                xFirewall.getMessages().translate("domain name system based list ban"),
                                new Date(),
                                "X-Firewall");
                        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, xFirewall.getMessages().translate("domain name system based list ban"));
                        }
                    }
                }
            }
        }
        //---------------------------------------------//
    }

    @EventHandler
    public void protectionCheck(PlayerLoginEvent event) {
        String playerIp = event.getAddress().getHostAddress();
        String playerName = event.getPlayer().getName();
        //---------------------------------------------//
        if (Bukkit.getIPBans().contains(playerIp) || !(event.getResult() == PlayerLoginEvent.Result.ALLOWED))
            return;
        //---------------------------------------------//
        if (xFirewall.getSettings().isProtectionEnabled()) {
            if (xFirewall.getProtection().check()) {
                long playTimeConverted = event.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK) / 20;
                if (!xFirewall.getDataSource().isNickWhitelisted(playerName)) {
                    if (playTimeConverted >= xFirewall.getSettings().getProtectionWhitelistPlayTimeRequired()
                            || event.getPlayer().hasPermission("x-firewall.whitelist")) {
                        Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission("x-firewall.receive-messages"))
                                .forEach(players -> players.sendMessage(xFirewall.getMessages().translate("player has been whitelisted")
                                        .replace("%player_ip%", playerIp)
                                        .replace("%player_name%", playerName)));
                        return;
                    }
                    event.disallow(KICK_OTHER, xFirewall.getMessages().translate("protection kick"
                            .replace("%player_ip%", playerIp)
                            .replace("%player%", playerName)));
                }
            }
        }
        //---------------------------------------------//
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();

        if (xFirewall.getSettings().nullifyJoinMsg()) {
            if (cache.contains(ip) || xFirewall.getStopForumSpam().isInDownloadedProxies(ip)) {
                event.setJoinMessage(null);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();

        if (xFirewall.getSettings().nullifyLeaveMsg()) {
            if (cache.contains(ip) || xFirewall.getStopForumSpam().isInDownloadedProxies(ip)) {
                event.setQuitMessage(null);
            }
        }
    }

}
