package david.global.proxy;

import david.forspigot.XFirewall;
import david.utils.zombiestriker.WebsiteUtils;

public class Botscout {

    private final XFirewall xFirewall;

    private String url = "http://botscout.com/test/?ip=";


    public Botscout(XFirewall xFirewall) {
        this.xFirewall = xFirewall;
    }

    public String getUserDataAsString(String IP) {
        return WebsiteUtils.getText(url + IP);
    }

}
