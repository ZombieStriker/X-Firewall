package david.forspigot.datasource;

import david.forspigot.settings.Settings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class YamlDataSource implements DataSource {

    private final String whitelistPath = "whitelist";
    private ArrayList<String> whitelist;

    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    @SuppressWarnings("unchecked")
    public YamlDataSource() {
        customConfigFile = new File(Settings.DATABASE_FILE);
        reload();
    }

    @Override
    public synchronized void whitelist(String player) {
        if (!whitelist.contains(player)) {
            whitelist.add(player);
            save();
        }
    }

    @Override
    public synchronized void unWhitelist(String player) {
        whitelist.remove(player);
    }

    @Override
    public synchronized boolean isNickWhitelisted(String player) {
        return whitelist.contains(player);
    }

    @Override
    public synchronized String[] getWhitelistedNicks() {
        return whitelist.toArray(new String[0]);
    }


    @Override
    public final void reload() {
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        if (customConfig.get(whitelistPath) == null) {
            customConfig.set(whitelistPath, new ArrayList<String>());
        }

        whitelist = (ArrayList<String>) customConfig.get(whitelistPath);
        save();
    }

    private void save() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
    }
}