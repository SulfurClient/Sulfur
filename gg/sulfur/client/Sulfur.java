package gg.sulfur.client;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import gg.sulfur.client.api.config.ConfigManager;
import gg.sulfur.client.api.file.FileManager;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.versioning.BuildType;
import gg.sulfur.client.impl.events.KeyboardEvent;
import gg.sulfur.client.impl.gui.alt.boom.AltManager;
import gg.sulfur.client.impl.managers.*;
import gg.sulfur.client.impl.sense.FunnyUtil;
import gg.sulfur.client.impl.sense.Start;
import gg.sulfur.client.impl.utils.java.HWIDUtil;
import gg.sulfur.client.impl.utils.networking.NetworkUtil;
import gg.sulfur.client.soundfx.VolSlider;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

import java.io.*;
import java.net.Proxy;
import java.time.OffsetDateTime;

/**
 * @author Dort
 */
public class Sulfur {

    private static Sulfur instance = new Sulfur();

    public static Sulfur getInstance() {
        return instance;
    }
    public static AltManager altManager;

    private String user = "";
    private BuildType buildType = BuildType.NORMAL;
    private String uid = "";
    private String gpu = "";
    private String cpu = "";

    private String clientName = "";

    private String currentConfig = "";

    private ValueManager valueManager;
    private ModuleManager moduleManager;
    private EventBus eventBus;
    private FileManager fileManager;
    private CommandManager commandManager;
    private Proxy proxy = Proxy.NO_PROXY;
    private ConfigManager configManager;
    private FriendManager friendManager;
    private FontManager fontManager;
    private NotificationManager notificationManager;
    private static String alt;
    private VolSlider volSlider;
    private boolean isViaVersionEnabled = false;

    public boolean isViaVersionEnabled() {
        return isViaVersionEnabled;
    }

    public void setViaVersion(boolean value) {
        isViaVersionEnabled = value;
    }

    /**
     * Initializes this {@code Client}
     */
    public final void initialize() {
        LogManager.getLogger().debug("Starting client startup");
        fontManager = new FontManager();
        try {
            user = NetworkUtil.getUsername();
        } catch (Exception ignored) {
            user = System.getProperty("user.name");
        }

        if (user.equalsIgnoreCase("ben")) {
            user = "Luison";
        }

        if (user.equalsIgnoreCase("Kansio") || user.equalsIgnoreCase("Divine")) {
            buildType = BuildType.DEV;
        }

        if (user.equalsIgnoreCase("retard")) user = "Kansio";

        instance = this;

        altManager = new AltManager();
        eventBus = new EventBus(clientName);
        valueManager = new ValueManager();
        moduleManager = new ModuleManager();
        fileManager = new FileManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        notificationManager = new NotificationManager();
        configManager = new ConfigManager();
        volSlider = new VolSlider();
        try {
            ViaMCP.getInstance().start();
        } catch (Exception gay) {
            gay.printStackTrace();
        }

        try {
            new Start();
            Start.launch();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String filePath = "./specs.txt";
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dxdiag", "/t", filePath);
            Process p = pb.start();
            p.waitFor();

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("Card name: ")) {
                    gpu = line.trim().replaceAll("Card name: ", "");
                }
                if (line.trim().startsWith("Processor: ")) {
                    cpu = line.trim().replaceAll("Processor: ", "");
                }
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error getting specs");
        }

        try {
            readName();
        } catch (Exception e) {
            clientName = "Sulfur";
            System.out.println("There was an error whilst reading the client name:");
            e.printStackTrace();
        }

        try {

        } catch (Exception e) {
            System.out.println("something went wrong whilst loading the client name from file! Here's the error:");
            e.printStackTrace();
        }

        doRPC(880353171795505182l);

        eventBus.register(this);
        Display.setTitle("Minecraft 1.8.8");

        if (!getDownloadDir().exists()) {
            getDownloadDir().mkdirs();
        }

    }

    public VolSlider getVolSlider() {
        return volSlider;
    }

    private void doRPC(long application) {
        IPCClient client = new IPCClient(application);
        System.out.println("Setting RPC");
        client.setListener(new IPCListener() {

            @Override
            public void onReady(IPCClient client) {
                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setState("Client User: " + user)
                        .setDetails(buildType.getName() + " Build")
                        .setStartTimestamp(OffsetDateTime.now())
                        .setLargeImage("logo", "Build: " + getClientVersion())
                        .setSmallImage("logo", "Destroying shitty servers")
                        //.setParty("party1234", 1, 6)
                        .setMatchSecret("xyzzy")
                        //.setJoinSecret("join")
                        .setSpectateSecret("look");

                client.sendRichPresence(builder.build());
            }
        });
        try {
            client.connect();
        } catch (Exception e) {

        }
    }

    @Subscribe
    public void onKeyboardClick(KeyboardEvent event) {
        for (Module module : moduleManager.getObjects()) {
            if (module.getKeyBind() != 0 && event.getKey() == module.getKeyBind()) module.toggle();
        }
    }

    public File getDownloadDir() {
        File dir = new File(Minecraft.getMinecraft().mcDataDir, "Sulfur/Downloads");
        return dir;
    }

    public String getDownloadDirStr() {
        File dir = new File(Minecraft.getMinecraft().mcDataDir, "Sulfur/Downloads");
        try {
            return dir.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath().replace("\\.", "") + "/Sulfur/Downloads";
    }

    //08/29/2021 -> 082921 (use this for versions)
    public String getClientVersion() {
        return "110221";
    }

    public String getClientEdition() {
        return "BlocksMC troll edition";
    }

    public ValueManager getValueManager() {
        return valueManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public String getCpu() {
        return cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static String[] getSpectatorAlt() {
        return alt == null ? null : alt.split(":");
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public static void setAlt(String alt) {
        Sulfur.alt = alt;
    }

    public BuildType getBuildType() {
        return buildType;
    }

    public void readName() throws IOException {
        File file = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Sulfur" + System.getProperty("file.separator") + "settings.cum");

        if (!file.exists()) {
            File cum = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Sulfur" + System.getProperty("file.separator") + "setting.cum");
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write("Sulfur");
            w.close();
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            clientName = st;
        }
    }

    public void setClientName(String str) {
        clientName = str;
    }

    public String getClientName() {
        return clientName;
    }

    public void setCurrentConfig(String currentConfig) {
        this.currentConfig = currentConfig;
    }

    public String getCurrentConfig() {
        return currentConfig;
    }
}
