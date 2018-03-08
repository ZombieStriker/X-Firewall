package david.forspigot.settings;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Messages {

    private final HashMap<String, String> map = new HashMap<>();

    private FileConfiguration customConfig = null;
    private File customConfigFile;

    public Messages() {
        customConfigFile = new File(Settings.MESSAGE_FILE);
        loadDefaults();
        getMessages();
    }

    /**
     * %player%
     * %player_ip%
     * %dnsbl_response%
     * %sfp_ip_response%
     * %btt_ip_response%
     * %sfp_asn_response%
     * %sfp_country_response%
     */

    private void loadDefaults() {
        map.put("autonomous system number kick",
                "You have been kicked!\\nReason:\\nYour autonomous system number (ASN: %asn_response%) is blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("autonomous system number ban",
                "You are banned!\\nReason:\\nYour autonomous system number is blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("internet protocol kick",
                "You have been kicked!\\nReason:\\nYour internet protocol (IP: %player_ip%) is blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("internet protocol ban",
                "You are banned!\\nReason:\\nYour internet protocol is blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("domain name system based list kick",
                "You have been kicked!\\nReason:\\nYou are domain name system based list (DNSBL: %dnsbl_response%) is blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("domain name system based list ban",
                "You are banned!\\nReason:\\nYou are domain name system based list blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("country blacklist kick",
                "You have been kicked!\\nReason:\\nYour country (COUNTRY: %country_response%) is blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("country blacklist ban",
                "You are banned!\\nReason:\\nYour country is blacklisted!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("player cache kick",
                "You have been kicked!\\nReason:\\nYou are listed on the cache!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("protection kick",
                "You have been kicked!\\nReason:\\nProtection is active and you are not on the whitelist!\\nIf you think this is a issue, please contact us at www.example.com");
        map.put("protection activated",
                "%player%, due to tremendous connections, protection has activated. Your game play will not be affected!");
        map.put("protection still active",
                "%player%, do not panic! There is still tremendous connections on-going! Restarting protection...");
        map.put("protection deactivated",
                "%player%, protection deactivated!");
        map.put("a country has been banned: ", "&cA country has been banned: %country%");
        map.put("a country has been unbanned: ", "&cA country has been unbanned: %country%");
        map.put("Banned countries: ", "&cBanned countries: %countries%");
        map.put("player has been whitelisted", "%player% &chas been whitelisted");
        map.put("player has been removed from the whitelist",
                "%player% &chas been removed from the whitelist");
        map.put("Whitelist: ", "&cWhitelist: ");
        map.put("Settings and database reloaded", "Settings and database reloaded");
    }

    public String translate(String message) {
        String ret = map.get(message);
        if (ret != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

    private void getMessages() {
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        for (String key : map.keySet()) {
            if (this.customConfig.getString(key) == null) {
                this.customConfig.set(key, map.get(key));
            } else {
                map.put(key, this.customConfig.getString(key));
            }
        }

        try {
            customConfig.save(customConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
