package david.forspigot.datasource;

public interface DataSource {

    public void whitelist(String nick);

    public void unWhitelist(String nick);

    public boolean isNickWhitelisted(String nick);

    public String[] getWhitelistedNicks();

    public void reload();

    public void close();

    public enum DataSourceType {

        MYSQL, YAML
    }
}
