package david.forspigot.datasource;

import java.sql.*;
import java.util.ArrayList;

public abstract class SQLDataSource implements DataSource {

    protected Connection con;

    protected abstract void connect() throws ClassNotFoundException,
            SQLException;

    protected abstract void setup() throws SQLException;

    @Override
    public abstract void close();

    @Override
    public synchronized void whitelist(String nick) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("INSERT INTO whitelist (player_id) VALUES("
                    + "SELECT player_id FROM player WHERE player=?"
                    + ");");
            pst.setString(1, nick);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized void unWhitelist(String nick) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(
                    "DELETE FROM whitelist "
                            + "WHERE player_id=(SELECT player_id FROM player WHERE player=?);");
            pst.setString(1, nick);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public synchronized boolean isNickWhitelisted(String nick) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement(
                    "SELECT player_id FROM whitelist "
                            + "WHERE player_id=(SELECT player_id FROM player WHERE player=?);");
            pst.setString(1, nick);
            return pst.executeQuery().next();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }


    @Override
    public synchronized String[] getWhitelistedNicks() {
        ArrayList<String> list = new ArrayList<>();
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT player FROM player "
                            + "WHERE player_id IN (SELECT player_id FROM whitelist);");
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignored) {
                }
            }
        }

        return list.toArray(new String[0]);
    }

}
