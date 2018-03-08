package david.forspigot;

import david.forspigot.datasource.DataSource;
import david.forspigot.datasource.MySQLDataSource;
import david.forspigot.datasource.YamlDataSource;
import david.forspigot.listener.XFirewallPlayerListener;
import david.forspigot.settings.Messages;
import david.forspigot.settings.Settings;
import david.global.DNSBL;
import david.global.Protection;
import david.global.proxy.Botscout;
import david.global.proxy.Stopforumspam;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.NamingException;

public class XFirewall extends JavaPlugin {

    //TODO: Java OOP

    private Messages messages;
    private Protection protection;
    private Stopforumspam stopforumspam;
    private Botscout botscout;
    private DataSource dataSource;
    private Settings settings;
    private DNSBL dnsbl;

    @Override
    public void onEnable() {
        settings = new Settings();
        messages = new Messages();
        protection = new Protection(this);
        stopforumspam = new Stopforumspam(this);

        switch (settings.getDatabase()) {
            case YAML:
                dataSource = new YamlDataSource();
                break;
            case MYSQL:
                try {
                    dataSource = new MySQLDataSource(settings);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    getServer().getPluginManager().disablePlugin(this);
                }
        }

        try {
            dnsbl = new DNSBL();
            settings.getDnsblList().forEach(s -> dnsbl.addLookupService(s));
        } catch (NamingException e) {
            e.printStackTrace();
        }

        stopforumspam = new Stopforumspam(this);
        stopforumspam.downloadStopForumSpamDatabase();
        settings.getStopForumSpamAsnList().forEach(s -> stopforumspam.addToAsnList(s));
        settings.getStopForumSpamCountryList().forEach(s -> stopforumspam.addToCountryList(s));

        getServer().getPluginManager().registerEvents(new XFirewallPlayerListener(this), this);
    }

    public Messages getMessages() {
        return messages;
    }

    public Settings getSettings() {
        return settings;
    }

    public Stopforumspam getStopForumSpam() {
        return stopforumspam;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Protection getProtection() {
        return protection;
    }

    public DNSBL getDnsbl() {
        return dnsbl;
    }

    public Botscout getBotScout() {
        return botscout;
    }
}
