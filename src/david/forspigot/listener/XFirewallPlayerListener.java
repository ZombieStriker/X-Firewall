package david.forspigot.listener;

import com.google.gson.JsonObject;
import david.forspigot.XFirewall;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.bukkit.event.player.PlayerLoginEvent.Result.ALLOWED;
import static org.bukkit.event.player.PlayerLoginEvent.Result.KICK_OTHER;

public class XFirewallPlayerListener implements Listener {

    private final XFirewall xFirewall;

    private Set<String> cache = new HashSet<>();

    private int botscoutMaxMsgOnError = 0;

    public XFirewallPlayerListener(XFirewall xFirewall) {
        this.xFirewall = xFirewall;
    }

    @EventHandler
    public void test(PlayerLoginEvent event) {
        if (event.getPlayer() == null || event.getPlayer().isBanned() || !event.getResult().equals(ALLOWED))
            return;

        String sfpInternetProtocolKickMsg = xFirewall.getMessages().translate("internet protocol kick");
        String sfpInternetProtocolBanMsg = xFirewall.getMessages().translate("internet protocol ban");

        String sfpAutonomousSystemNumberKickMsg = xFirewall.getMessages().translate("autonomous system number kick");
        String sfpAutonomousSystemNumberBanMsg = xFirewall.getMessages().translate("autonomous system number ban");

        String sfpCountryKickMsg = xFirewall.getMessages().translate("country kick");
        String sfpCountryBanMsg = xFirewall.getMessages().translate("country ban");

        String dnsblKickMsg = xFirewall.getMessages().translate("domain name system based list kick");
        String dnsblBanMsg = xFirewall.getMessages().translate("domain name system based list ban");

        String cacheKickMsg = xFirewall.getMessages().translate("player cache kick");

        String protectionKickMsg = xFirewall.getMessages().translate("protection kick");
        String protectionWhitelistedMsg = xFirewall.getMessages().translate("player has been whitelisted");

        String name = event.getPlayer().getName();
        String ip = event.getAddress().getHostAddress();

        if (cache.contains(ip)) {
            event.disallow(KICK_OTHER, cacheKickMsg
                    .replace("%player_ip%", ip)
                    .replace("%player%", name));
        }

        if (xFirewall.getSettings().isStopForumSpamGetDataOnlyOnce()) {
            JsonObject stopForumSpamJsonObject = xFirewall.getStopForumSpam().getIpDataAsJson(ip);

            if (xFirewall.getSettings().isStopForumSpamProxyCheckEnabled()) {
                boolean ban = xFirewall.getSettings().isStopForumSpamProxyAdvancedIfFoundBan();
                boolean appears = Integer.valueOf(String.valueOf(stopForumSpamJsonObject.get("appears"))) >= 1;

                if (appears) {
                    event.disallow(KICK_OTHER, sfpInternetProtocolKickMsg
                            .replace("%player_ip%", ip)
                            .replace("%player%", name)
                            .replace("%sfp_ip_response%", String.valueOf(true)));
                    if (ban) {
                        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                        if (!ipBanList.isBanned(ip)) {
                            ipBanList.addBan(ip,
                                    sfpInternetProtocolBanMsg,
                                    new Date(),
                                    "X-Firewall");
                        }
                    }
                    cache.add(ip);
                }
            } else if (xFirewall.getSettings().isStopForumSpamAsnCheckEnabled()) {
                boolean appears = xFirewall.getStopForumSpam().isAsnBlacklisted(stopForumSpamJsonObject.get("asn"));
                boolean ban = xFirewall.getSettings().isStopForumSpamAsnAdvancedIfFoundBan();

                if (appears) {
                    event.disallow(KICK_OTHER, sfpAutonomousSystemNumberKickMsg
                            .replace("%player_ip%", ip)
                            .replace("%player%", name)
                            .replace("%sfp_asn_response%", String.valueOf(true)));
                    if (ban) {
                        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                        if (!ipBanList.isBanned(ip)) {
                            ipBanList.addBan(ip,
                                    sfpAutonomousSystemNumberBanMsg
                                            .replace("%player%", name)
                                            .replace("%player_ip%", ip),
                                    new Date(),
                                    "X-Firewall");
                        }
                    }
                    cache.add(ip);
                }
            } else if (xFirewall.getSettings().isStopForumSpamCountryCheckEnabled()) {
                boolean appears = xFirewall.getStopForumSpam().isCountryBlacklisted(stopForumSpamJsonObject.get("country"));
                boolean ban = xFirewall.getSettings().isStopForumSpamCountryAdvancedIfFoundBan();

                if (appears) {
                    event.disallow(KICK_OTHER, sfpCountryKickMsg
                            .replace("%player_ip%", ip)
                            .replace("%player%", name)
                            .replace("%sfp_country_response%", String.valueOf(true)));
                    if (ban) {
                        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                        if (!ipBanList.isBanned(ip)) {
                            ipBanList.addBan(ip,
                                    sfpCountryBanMsg
                                            .replace("%player%", name)
                                            .replace("%player_ip%", ip),
                                    new Date(),
                                    "X-Firewall");
                        }
                    }
                    cache.add(ip);
                }
            }
        } else {
            if (xFirewall.getSettings().isStopForumSpamProxyCheckEnabled()) {
                JsonObject stopForumSpamJsonObject = xFirewall.getStopForumSpam().getIpDataAsJson(ip);

                boolean ban = xFirewall.getSettings().isStopForumSpamProxyAdvancedIfFoundBan();
                boolean appears = Integer.valueOf(String.valueOf(stopForumSpamJsonObject.get("appears"))) >= 1;

                if (appears) {
                    event.disallow(KICK_OTHER, sfpInternetProtocolKickMsg
                            .replace("%player_ip%", ip)
                            .replace("%player%", name)
                            .replace("%sfp_ip_response%", String.valueOf(true)));
                    if (ban) {
                        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                        if (!ipBanList.isBanned(ip)) {
                            ipBanList.addBan(ip,
                                    sfpInternetProtocolBanMsg,
                                    new Date(),
                                    "X-Firewall");
                        }
                    }
                    cache.add(ip);
                }
            } else if (xFirewall.getSettings().isStopForumSpamAsnCheckEnabled()) {
                JsonObject stopForumSpamJsonObject = xFirewall.getStopForumSpam().getIpDataAsJson(ip);

                boolean appears = xFirewall.getStopForumSpam().isAsnBlacklisted(stopForumSpamJsonObject.get("asn"));
                boolean ban = xFirewall.getSettings().isStopForumSpamAsnAdvancedIfFoundBan();

                if (appears) {
                    event.disallow(KICK_OTHER, sfpAutonomousSystemNumberKickMsg
                            .replace("%player_ip%", ip)
                            .replace("%player%", name)
                            .replace("%sfp_asn_response%", String.valueOf(true)));
                    if (ban) {
                        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                        if (!ipBanList.isBanned(ip)) {
                            ipBanList.addBan(ip,
                                    sfpAutonomousSystemNumberBanMsg
                                            .replace("%player%", name)
                                            .replace("%player_ip%", ip),
                                    new Date(),
                                    "X-Firewall");
                        }
                    }
                    cache.add(ip);
                }
            } else if (xFirewall.getSettings().isStopForumSpamCountryCheckEnabled()) {
                JsonObject stopForumSpamJsonObject = xFirewall.getStopForumSpam().getIpDataAsJson(ip);

                boolean appears = xFirewall.getStopForumSpam().isCountryBlacklisted(stopForumSpamJsonObject.get("country"));
                boolean ban = xFirewall.getSettings().isStopForumSpamCountryAdvancedIfFoundBan();

                if (appears) {
                    event.disallow(KICK_OTHER, sfpCountryKickMsg
                            .replace("%player_ip%", ip)
                            .replace("%player%", name)
                            .replace("%sfp_country_response%", String.valueOf(true)));
                    if (ban) {
                        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                        if (!ipBanList.isBanned(ip)) {
                            ipBanList.addBan(ip,
                                    sfpCountryBanMsg
                                            .replace("%player%", name)
                                            .replace("%player_ip%", ip),
                                    new Date(),
                                    "X-Firewall");
                        }
                    }
                    cache.add(ip);
                }
            } else if (xFirewall.getSettings().isStopForumSpamProxyCheckDatabaseEnabled()) {
                boolean appears = xFirewall.getStopForumSpam().isInDownloadedProxies(ip);

                if (appears) {
                    event.disallow(KICK_OTHER, sfpInternetProtocolKickMsg
                            .replace("%player_ip%", ip)
                            .replace("%player%", name)
                            .replace("%sfp_ip_response%", String.valueOf(true)));
                }
            }
        }
        if (xFirewall.getSettings().isDnsblCheckEnabled()) {
            boolean appears = xFirewall.getDnsbl().isBlocked(ip);
            boolean ban = xFirewall.getSettings().isDnsblAdvancedIfFoundBan();

            if (appears) {
                event.disallow(KICK_OTHER, dnsblKickMsg
                        .replace("%player_ip%", ip)
                        .replace("%player%", name)
                        .replace("%dnsbl_response%", String.valueOf(true)));
                if (ban) {
                    BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                    if (!ipBanList.isBanned(ip)) {
                        ipBanList.addBan(ip,
                                dnsblBanMsg
                                        .replace("%player%", name)
                                        .replace("%player_ip%", ip),
                                new Date(),
                                "X-Firewall");
                    }
                }
                cache.add(ip);
            }
        }
        if (xFirewall.getSettings().isBotscoutProxyCheckEnabled()) {
            String userData = xFirewall.getBotScout().getUserDataAsString(ip);

            boolean ban = xFirewall.getSettings().isBotscoutProxyAdvancedIfFoundBan();

            if (userData.toLowerCase().contains("Y")) {
                event.disallow(KICK_OTHER, sfpInternetProtocolKickMsg
                        .replace("%player_ip%", ip)
                        .replace("%player%", name)
                        .replace("%sfp_ip_response%", String.valueOf(true)));
                if (ban) {
                    BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                    if (!ipBanList.isBanned(ip)) {
                        ipBanList.addBan(ip,
                                dnsblBanMsg
                                        .replace("%player%", name)
                                        .replace("%player_ip%", ip),
                                new Date(),
                                "X-Firewall");
                    }
                }
            } else if (!userData.toLowerCase().contains("Y") && !userData.toLowerCase().contains("N")) {
                event.disallow(KICK_OTHER, sfpInternetProtocolKickMsg
                        .replace("%player_ip%", ip)
                        .replace("%player%", name)
                        .replace("%sfp_ip_response%", String.valueOf(true)));
                if (ban) {
                    BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
                    if (!ipBanList.isBanned(ip)) {
                        ipBanList.addBan(ip,
                                dnsblBanMsg
                                        .replace("%player%", name)
                                        .replace("%player_ip%", ip),
                                new Date(),
                                "X-Firewall");
                    }
                }
            } else if (userData.toLowerCase().contains("reached") || userData.toLowerCase().contains("invalid")
                    || userData.toLowerCase().contains("missing") || userData.toLowerCase().contains("malformed")) {
                botscoutMaxMsgOnError++;
                if (botscoutMaxMsgOnError != 3) {
                    System.out.println("Botscout error: " + userData);
                }
            }
        }

        if (!xFirewall.getSettings().isStopForumSpamProxyCheckEnabled() && !xFirewall.getSettings().isStopForumSpamProxyCheckDatabaseEnabled()
                && !xFirewall.getSettings().isStopForumSpamAsnCheckEnabled() && !xFirewall.getSettings().isStopForumSpamCountryCheckEnabled()
                && !xFirewall.getSettings().isDnsblCheckEnabled() && !xFirewall.getSettings().isBotscoutProxyCheckEnabled() && !xFirewall.getDataSource().isNickWhitelisted(name)
                && xFirewall.getSettings().isProtectionEnabled()) {
            if (xFirewall.getProtection().check()) {
                event.disallow(KICK_OTHER, protectionKickMsg.replace("%player_ip%", ip)
                        .replace("%player%", name));
            } else {
                if (xFirewall.getSettings().isProtectionWhitelistEnabled()) {
                    long playTimeConverted = event.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK) / 20;

                    if (playTimeConverted >= xFirewall.getSettings().getProtectionWhitelistPlayTimeRequired()
                            || event.getPlayer().hasPermission("x-firewall.whitelist")) {
                        Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission("x-firewall.receive-messages"))
                                .forEach(players -> players.sendMessage(protectionWhitelistedMsg.replace("%player_ip%", ip)
                                        .replace("%player_name%", name)));
                    } else if (!(playTimeConverted <= xFirewall.getSettings().getProtectionWhitelistPlayTimeRequired())
                            && !event.getPlayer().hasPermission("x-firewall.whitelist") && xFirewall.getDataSource().isNickWhitelisted(name)) {
                        event.disallow(KICK_OTHER, protectionKickMsg.replace("%player_ip%", ip)
                                .replace("%player%", name));
                    }
                }
            }
        }

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
