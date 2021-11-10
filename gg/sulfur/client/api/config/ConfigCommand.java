package gg.sulfur.client.api.config;

import com.google.gson.*;
import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.impl.utils.networking.NetworkManager;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import gg.sulfur.client.Sulfur;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;


public class ConfigCommand extends Command {

    public ConfigCommand() {
        super(new CommandData("config"));
    }

    @Override
    public void run(String command, String... args) {
        try {
            switch (args[1].toLowerCase()) {
                case "load": {
                    try {
                        Config config = Sulfur.getInstance().getConfigManager().find(args[2].toLowerCase());
                        config.load(Sulfur.getInstance().getFileManager());
                        ChatUtil.displayChatMessage("Loaded config " + args[2].toLowerCase() + ".");
                        Sulfur.getInstance().setCurrentConfig(config.getName());
                    } catch (Exception exc) {
                        exc.printStackTrace();
                        ChatUtil.displayChatMessage("That config doesn't exist!");
                    }
                    break;
                }

                case "save": {
                    if (!Sulfur.getInstance().getConfigManager().saveIfAdded(args[2].toLowerCase())) {
                        Config nigger = new Config(args[2].toLowerCase());
                        nigger.save();
                        Sulfur.getInstance().getConfigManager().add(nigger);
                    }
                    ChatUtil.displayChatMessage("Saved config " + args[2].toLowerCase() + ".");
                    break;
                }

                case "download": {
                    if (!downloadConfig(args[2].toLowerCase())) {
                        ChatUtil.displayChatMessage("Failed to download the config");
                    } else {
                        ChatUtil.displayChatMessage("Downloaded config file \"" + args[2] + "\" ");
                    }
                    break;
                }

                case "listonlineconfigs":
                case "onlineconfigs":
                case "verifiedconfigslist": {
                    if (Arrays.asList(listConfigs()).isEmpty()) {
                        ChatUtil.displayChatMessage("No online configs");
                    } else {
                        for (String config : listConfigs()) {
                            ChatUtil.displayChatMessage("- " + config);
                        }
                    }
                    break;
                }

                case "reload": {
                    Sulfur.getInstance().getConfigManager().onCreated();
                    ChatUtil.displayChatMessage("Reloaded.");
                    break;
                }

                case "loadonlineconfig":
                case "loadonline": {
                    if (!downloadConfig(args[2].toLowerCase())) {
                        ChatUtil.displayChatMessage("Failed to load the config");
                    } else {
                        //ChatUtil.displayChatMessage("Downloaded config file \"" + args[2] + "\" ");
                        Sulfur.getInstance().getConfigManager().onCreated();

                        try {
                            Config config = Sulfur.getInstance().getConfigManager().find(args[2].toLowerCase());
                            config.load(Sulfur.getInstance().getFileManager());
                            ChatUtil.displayChatMessage("Loaded config " + args[2].toLowerCase() + ".");
                        } catch (Exception exc) {
                            exc.printStackTrace();
                            ChatUtil.displayChatMessage("That config doesn't exist!");
                        }
                    }
                    break;
                }
                case "list": {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Configs: \n");
                    int i = 0;
                    for (Config config : Sulfur.getInstance().getConfigManager().getObjects()) {
                        i++;
                        stringBuilder.append("Config ").append(i).append(": ").append(config.getName());
                        stringBuilder.append("\n");
                    }
                    ChatUtil.displayChatMessage(stringBuilder.toString());
                    break;
                }

                default: {
                    showHelp();
                    break;
                }
            }
        } catch (Exception exc) {
            showHelp();
        }
    }

    private String[] listConfigs() {
        try {
            ArrayList<String> stringArrayList = new ArrayList<>();
            String apiGet = NetworkManager.getNetworkManager().sendGet(new HttpGet(new URI("http://api.zerotwoclient.xyz/listconfigs")));

            JsonElement node = new JsonParser().parse(apiGet);
            JsonArray arr = node.getAsJsonArray();
            if (!arr.isJsonArray()) {
                return new String[]{};
            }
            arr.forEach(element -> {
                JsonObject subobj = element.getAsJsonObject();
                String confName = subobj.get("name").getAsString();
                stringArrayList.add(confName);
            });
            String[] forReturnStatement = new String[0];
            stringArrayList.toArray(forReturnStatement);
            return forReturnStatement;
        }
        catch (Exception e) {
            ChatUtil.displayChatMessage("Failed to get configs");
            return new String[]{};
        }
    }

    private boolean downloadConfig(String configName) {
        File conf = new File("Sulfur/configs", configName);
        try {
            String url = ("http://www.zerotwoclient.xyz/test/configs/" + configName);

            URL urlObject = new URL(url);
            URLConnection urlConnection = urlObject.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            String configFileThing;

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8")))
            {
                String inputLine;
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(inputLine);
                }

                configFileThing = stringBuilder.toString();
            }

            if (!conf.exists()) {
                try {
                    conf.createNewFile();
                } catch (IOException e) {
                }
            }

            try (Writer writer = new FileWriter(conf)) {
                writer.write(configFileThing);
            } catch (IOException e) {
                conf.delete();
            }


            //return HWIDUtil.saveFile(url, "Sulfur/configs");
        } catch (HttpResponseException hre) {
            if (hre.getStatusCode() == 404) {
                ChatUtil.displayChatMessage("Config not found");
            } else {
                ChatUtil.displayChatMessage("An unexpected error occurred");
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            ChatUtil.displayChatMessage("An unexpected error occurred");
            return false;
        }
        return conf.exists();
    }

    private void showAllVerifiedConfigs() {

    }

    private void showHelp() {
        String prefix = ".";
        ChatUtil.displayChatMessage("Invalid syntax! Showing help:");
        ChatUtil.displayChatMessage(prefix + "config load (config) - Loads a config locally.");
        ChatUtil.displayChatMessage(prefix + "config save (config) - Saves a config locally.");
        ChatUtil.displayChatMessage(prefix + "config reload - Reloads the config list.");
        ChatUtil.displayChatMessage(prefix + "config list - Shows all of your saved configs.");
        ChatUtil.displayChatMessage(prefix + "config download (config) - Download a verified config into your configs folder.");
        //ChatUtil.displayChatMessage(prefix + "config onlineconfigs - Lists online verified configs.");
    }
}
