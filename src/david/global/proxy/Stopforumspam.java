package david.global.proxy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import david.forspigot.XFirewall;
import david.utils.codejava.HttpDownloadUtility;
import david.utils.codejava.UnzipUtility;
import david.utils.zombiestriker.WebsiteUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class Stopforumspam {

    private String url = "http://api.stopforumspam.org/api?ip=";

    private Set<String> asnList = new HashSet<>();
    private Set<String> countryList = new HashSet<>();
    private Set<String> downloadedProxies = new HashSet<>();

    private XFirewall xFirewall;

    public Stopforumspam(XFirewall xFirewall) {
        this.xFirewall = xFirewall;
    }

    public void addToAsnList(String asn) {
        asnList.add(asn);
    }

    public boolean isAsnBlacklisted(JsonElement jsonElement) {
        return asnList.stream().anyMatch(e -> jsonElement.toString().contains(e));
    }

    public void addToCountryList(String country) {
        countryList.add(country);
    }

    public boolean isCountryBlacklisted(JsonElement jsonElement) {
        return countryList.stream().anyMatch(e -> jsonElement.toString().contains(e));
    }

    public boolean isInDownloadedProxies(String ip) {
        return downloadedProxies.contains(ip);
    }

    public JsonObject getIpDataAsJson(String IP) {
        String jsonStr = WebsiteUtils.getText(url + IP + "&json");

        JsonParser parser = new JsonParser();

        JsonElement jsonTree = parser.parse(Objects.requireNonNull(jsonStr));
        JsonElement ipElement = null;

        JsonObject mainJsonObject;

        if (jsonTree.isJsonObject()) {
            mainJsonObject = jsonTree.getAsJsonObject();
            ipElement = mainJsonObject.get("ip");

            if (ipElement.isJsonObject()) {
                return ipElement.getAsJsonObject();
            }
        }
        return ipElement.getAsJsonObject();
    }

    public void downloadStopForumSpamDatabase() {
        File zipFile = new File(xFirewall.getDataFolder() + File.separator + "bannedips.zip");
        File unzipFile = new File(xFirewall.getDataFolder() + File.separator + "bannedips.csv");

        boolean zipFileExists = zipFile.exists();
        boolean unzipFileExists = unzipFile.exists();

        if (unzipFileExists) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy");

            if (!simpleDateFormat.format(unzipFile.lastModified()).equals(simpleDateFormat.format(new Date()))) {
                System.out.println("Test 1");
                try {
                    HttpDownloadUtility.downloadFile("http://stopforumspam.com/downloads/bannedips.zip", xFirewall.getDataFolder().toString());
                    System.out.println("Test 2");

                    UnzipUtility.unzip(zipFile.toString(), xFirewall.getDataFolder().toString());
                    System.out.println("Test 3");

                    Stream<String> stream = Files.lines(Paths.get(unzipFile.toString()));
                    stream.forEach(downloadedProxies::add);
                    System.out.println("Test 4");

                    if (zipFileExists) {
                        zipFile.delete();
                        System.out.println("Test 5");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Test 1 (?)");
                Stream<String> stream = null;
                try {
                    stream = Files.lines(Paths.get(unzipFile.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Objects.requireNonNull(stream).forEach(downloadedProxies::add);
            }
        } else {
            try {
                HttpDownloadUtility.downloadFile("http://stopforumspam.com/downloads/bannedips.zip", xFirewall.getDataFolder().toString());
                System.out.println("Test 1 (!)");

                UnzipUtility.unzip(zipFile.toString(), xFirewall.getDataFolder().toString());
                System.out.println("Test 2 (!)");

                Stream<String> stream = Files.lines(Paths.get(unzipFile.toString()));
                stream.forEach(downloadedProxies::add);
                System.out.println("Test 3 (!)");

                if (zipFileExists) {
                    zipFile.delete();
                    System.out.println("Test 4 (!)");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
