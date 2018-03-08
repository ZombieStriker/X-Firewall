package david.forspigot.settings;

import david.forspigot.datasource.DataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    public static final String PLUGIN_FOLDER = "./plugins/X-Firewall";
    public static final String MESSAGE_FILE = Settings.PLUGIN_FOLDER + "/messages.yml";
    public static final String SETTINGS_FILE = Settings.PLUGIN_FOLDER + "/config.yml";
    public static final String DATABASE_FILE = Settings.PLUGIN_FOLDER + "/database.yml";

    private FileConfiguration customConfig = null;
    private File customConfigFile;

    public Settings() {
        customConfigFile = new File(Settings.SETTINGS_FILE);
        reload();
    }

    public void reload() {
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        write();
        try {
            customConfig.save(customConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void write() {
        isProtectionEnabled();
        isProtectionWhitelistEnabled();
        getProtectionWhitelistPlayTimeRequired();
        getProtectionDuration();
        getProtectionInteral();
        getProtectionMaxConnections();
        isStopForumSpamProxyCheckEnabled();
        isStopForumSpamProxyCheckDatabaseEnabled();
        isStopForumSpamGetDataOnlyOnce();
        isStopForumSpamProxyAdvancedIfFoundBan();
        isStopForumSpamProxyAdvancedLog();
        isStopForumSpamAsnCheckEnabled();
        getStopForumSpamAsnList();
        isStopForumSpamAsnAdvancedIfFoundBan();
        isStopForumSpamAsnAdvancedLog();
        isBotscoutProxyCheckEnabled();
        isBotscoutProxyAdvancedIfFoundBan();
        isBotscoutProxyAdvancedLog();
        isDnsblCheckEnabled();
        getDnsblList();
        isDnsblAdvancedIfFoundBan();
        isDnsblAdvancedLog();
        nullifyJoinMsg();
        nullifyLeaveMsg();
        isStopForumSpamCountryCheckEnabled();
        getStopForumSpamCountryList();
        isStopForumSpamCountryAdvancedIfFoundBan();
        isStopForumSpamCountryAdvancedLog();
        getMySQLDatabaseName();
        getMySQLHost();
        getMySQLPort();
        getMySQLUsername();
        getMySQLPassword();
    }

    public DataSource.DataSourceType getDatabase() {
        String key = "database";
        if (customConfig.get(key) == null) {
            customConfig.set(key, "yaml");
        }

        try {
            return DataSource.DataSourceType.valueOf(customConfig.getString(key).toUpperCase());
        } catch (IllegalArgumentException ex) {
            System.out.println("Unknown database type! Defaulting to YAML...");
            return DataSource.DataSourceType.YAML;
        }
    }

    public boolean isProtectionEnabled() {
        String key = "protection.enabled";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isProtectionWhitelistEnabled() {
        String key = "protection.whitelist.enabled";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public int getProtectionWhitelistPlayTimeRequired() {
        String key = "protection.whitelist.required-play-time";
        if (customConfig.get(key) == null) {
            customConfig.set(key, 60);
        }
        return customConfig.getInt(key);
    }

    public int getProtectionDuration() {
        String key = "protection.duration";
        if (customConfig.get(key) == null) {
            customConfig.set(key, 60);
        }
        return customConfig.getInt(key);
    }

    public int getProtectionInteral() {
        String key = "protection.interval";
        if (customConfig.get(key) == null) {
            customConfig.set(key, 400);
        }
        return customConfig.getInt(key);
    }

    public int getProtectionMaxConnections() {
        String key = "protection.max-conns";
        if (customConfig.get(key) == null) {
            customConfig.set(key, 5);
        }
        return customConfig.getInt(key);
    }

    public boolean isStopForumSpamProxyCheckEnabled() {
        String key = "stopforumspam.proxy.check";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamProxyCheckDatabaseEnabled() {
        String key = "stopforumspam.proxy.check-database";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamGetDataOnlyOnce() {
        String key = "stopforumspam.advanced.get-data-only-once";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamProxyAdvancedIfFoundBan() {
        String key = "stopforumspam.proxy.advanced.if-found-ban";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamProxyAdvancedLog() {
        String key = "stopforumspam.proxy.advanced.log";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamAsnCheckEnabled() {
        String key = "stopforumspam.asn.check";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public List<String> getStopForumSpamAsnList() {
        String key = "stopforumspam.asn.list";
        ArrayList<String> def = new ArrayList<>();
        def.add("13335");
        def.add("16276");
        def.add("44066");
        def.add("20473");

        if (customConfig.getList(key) == null) {
            customConfig.set(key, def);
        }
        return customConfig.getStringList(key);
    }

    public boolean isStopForumSpamAsnAdvancedIfFoundBan() {
        String key = "stopforumspam.asn.advanced.if-found-ban";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamAsnAdvancedLog() {
        String key = "stopforumspam.asn.advanced.log";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isBotscoutProxyCheckEnabled() {
        String key = "botscout.proxy.check";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isBotscoutProxyAdvancedIfFoundBan() {
        String key = "botscout.proxy.advanced.if-found-ban";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isBotscoutProxyAdvancedLog() {
        String key = "botscout.proxy.advanced.log";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isDnsblCheckEnabled() {
        String key = "dnsbl.check";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public List<String> getDnsblList() {
        String key = "dnsbl.list";
        ArrayList<String> def = new ArrayList<>();

        def.add("b.barracudacentral.org");
        def.add("spam.dnsbl.sorbs.net");
        def.add("dnsbl.dronebl.org");
        def.add("spambot.bls.digibase.ca");
        def.add("all.s5h.net");

        if (customConfig.getList(key) == null) {
            customConfig.set(key, def);
        }
        return customConfig.getStringList(key);
    }

    public boolean isDnsblAdvancedIfFoundBan() {
        String key = "dnsbl.advanced.if-found-ban";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isDnsblAdvancedLog() {
        String key = "dnsbl.advanced.log";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean nullifyJoinMsg() {
        String key = "nullify.join";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean nullifyLeaveMsg() {
        String key = "nullify.leave";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamCountryCheckEnabled() {
        String key = "country.check";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }


    public List<String> getStopForumSpamCountryList() {
        String key = "country.list";
        ArrayList<String> def = new ArrayList<>();

        def.add("cn");
        def.add("bg");

        if (customConfig.getList(key) == null) {
            customConfig.set(key, def);
        }
        return customConfig.getStringList(key);
    }

    public boolean isStopForumSpamCountryAdvancedIfFoundBan() {
        String key = "country.advanced.if-found-ban";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public boolean isStopForumSpamCountryAdvancedLog() {
        String key = "country.advanced.log";
        if (customConfig.get(key) == null) {
            customConfig.set(key, true);
        }
        return customConfig.getBoolean(key);
    }

    public String getMySQLDatabaseName() {
        String key = "mysql.name";
        if (customConfig.get(key) == null) {
            customConfig.set(key, "x-firewall");
        }
        return customConfig.getString(key);
    }

    public String getMySQLHost() {
        String key = "mysql.host";
        if (customConfig.get(key) == null) {
            customConfig.set(key, "127.0.0.1");
        }
        return customConfig.getString(key);
    }

    public String getMySQLPort() {
        String key = "mysql.port";
        if (customConfig.get(key) == null) {
            customConfig.set(key, "3306");
        }
        return customConfig.getString(key);
    }

    public String getMySQLUsername() {
        String key = "mysql.username";
        if (customConfig.get(key) == null) {
            customConfig.set(key, "x-firewall");
        }
        return customConfig.getString(key);
    }

    public String getMySQLPassword() {
        String key = "mysql.password";
        if (customConfig.get(key) == null) {
            customConfig.set(key, "x-firewall");
        }
        return customConfig.getString(key);
    }

}
