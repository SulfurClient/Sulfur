package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import com.sun.net.httpserver.HttpServer;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ProductType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.hc.core5.http.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Spotify extends Module {

    public static Spotify INSTANCE;
    private Sulfur main;
    private final Logger logger;
    private final Logger spotifyLogger;
    private final String PORT = "2020";
    private final String CLIENT_ID = "a44551f3e5e74f6b8c8045817683ded8";
    private final URI REDIRECT_URI;
    private final String CHALLENGE = "w6iZIj99vHGtEx_NVl9u3sthTN646vvkiP8OMCGfPmo";
    private final String CODE_VERIFIER = "NlJx4kD4opk4HY7zBM6WfUHxX7HoF8A2TUhOIPGA74w";
    private final SpotifyApi api;
    private final AuthorizationCodeUriRequest authorizationCodeUriRequest;
    private AuthorizationCodeCredentials credentials;
    protected Integer millisThrough;
    protected Integer millisAll;
    protected String previousSong;
    protected Integer percentage;
    protected Track currentlyPlaying;
    protected ResourceLocation coverImage;
    protected BufferedImage coverImageBuffer;
    protected Boolean playing;
    private boolean ready;
    private boolean hasPremium;
    public Color darkGrey;
    public Color lightGrey;
    private final Color barAndTextColor;
    private final Color barColorDark;
    public int width;
    public int height;
    private Stopwatch stopwatch = new Stopwatch();

    private GuiScreen lastGui;
    private GuiScreen currentGui = mc.currentScreen;

    private boolean authenticated = false;

    String accessToken = "";
    String refreshToken = "";

    public Spotify(ModuleData moduleData) {
        super(moduleData);

        this.logger = LogManager.getLogger("Spotify Viewer (Renderer)");
        this.spotifyLogger = LogManager.getLogger("Spotify Viewer (Spotify)");
        this.REDIRECT_URI = this.makeUri("http://localhost:2020");
        this.api = SpotifyApi.builder().setClientId("a44551f3e5e74f6b8c8045817683ded8").setRedirectUri(this.REDIRECT_URI).build();
        this.authorizationCodeUriRequest = this.api.authorizationCodePKCEUri("w6iZIj99vHGtEx_NVl9u3sthTN646vvkiP8OMCGfPmo").scope("user-read-playback-state user-read-currently-playing user-modify-playback-state streaming user-read-private").build();
        this.ready = false;
        this.hasPremium = false;
        this.darkGrey = new Color(169, 169, 169);
        this.lightGrey = new Color(145, 145, 145);
        this.barAndTextColor = new Color(145, 145, 145);
        this.barColorDark = new Color(169, 169, 169);
        this.width = 290;
        this.height = 64;
    }


    public void setDarkGrey(final Color darkGrey) {
        this.darkGrey = darkGrey;
    }

    public void setLightGrey(final Color lightGrey) {
        this.lightGrey = lightGrey;
    }

    public void initSpotify() throws IOException {
        try {
            this.api.setAccessToken(accessToken);
            this.api.setRefreshToken(refreshToken);
            this.setupApi();
            this.getCode();
        } catch (Exception e) {
            if (!(e instanceof SpotifyWebApiException)) {
                return;
            }
            e.printStackTrace();
            this.getCode();
        }
    }

    private void getCode() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt("2020")), 0);
        final String[] code = new String[1];
        final HttpException[] ex = new HttpException[1];
        HttpException e;
        server.createContext("/", httpExchange -> {
            code[0] = this.queryToMap(httpExchange.getRequestURI().getQuery()).get("code");
            if (code[0] != null) {
                server.stop(0);
                try {
                    this.credentials = this.api.authorizationCodePKCE(code[0], "NlJx4kD4opk4HY7zBM6WfUHxX7HoF8A2TUhOIPGA74w").build().execute();
                    this.setupApi();
                } catch (Exception exxx) {
                    exxx.printStackTrace();
                }
            }
        });
        server.start();
        this.spotifyLogger.info("Opening the Spotify Authenticator in the users default browser!");
        Desktop.getDesktop().browse(this.authorizationCodeUriRequest.execute());
    }

    @Subscribe
    public void onRender(final RenderHUDEvent event) {
        int startY = Sulfur.getInstance().getModuleManager().get(SessionInformation.class).isToggled() ? 150 : 64;

        this.render(5, startY, true);
    }

    public void render(final int x, final int y, final boolean toggle) {
        if (!toggle) {
            return;
        }

        Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);

        if (currentlyPlaying == null) {
            return;
        }

        int width;

        String listauthors;
        if (this.currentlyPlaying.getArtists().length == 1) {
            listauthors = this.currentlyPlaying.getArtists()[0].getName();
        } else if (this.currentlyPlaying.getArtists().length == 2) {
            listauthors = this.currentlyPlaying.getArtists()[0].getName() + " and " + this.currentlyPlaying.getArtists()[1].getName();
        } else {
            final StringBuilder authors = new StringBuilder();
            int index = 0;
            for (final ArtistSimplified author : this.currentlyPlaying.getArtists()) {
                if (index == this.currentlyPlaying.getArtists().length - 1) {
                    authors.append(" and ").append(author.getName());
                } else if (index == this.currentlyPlaying.getArtists().length - 2) {
                    authors.append(author.getName());
                } else {
                    authors.append(author.getName()).append(", ");
                }
                ++index;
            }
            listauthors = authors.toString();
        }

        if (mc.fontRendererObj.getStringWidth(this.currentlyPlaying.getName()) > mc.fontRendererObj.getStringWidth(listauthors)) {
            width = mc.fontRendererObj.getStringWidth(this.currentlyPlaying.getName());
        } else {
            width = mc.fontRendererObj.getStringWidth(listauthors);
        }

        //int startY = Sulfur.getInstance().getModuleManager().get(SessionInformation.class).isToggled() ? 150 : 64;
        int startY = y;

        Gui.drawRect(x, startY + 5.0, x + width + 24, startY + 54.0, new Color(0, 0, 0, 105).getRGB());
        Gui.drawRect(x, startY + 5.0, x + width + 24, startY + 6.0, hud.color.getValue().getRGB());

        if (this.percentage != null) {
            final int max = width;
            final int wayThroughPixels = (int) (max * (this.percentage / 100.0));
            RenderUtils.drawRoundedRect(x + 8, y + 40, max, 4, 2, new Color(8, 8, 8).getRGB());
            RenderUtils.drawRoundedRect(x + 8, y + 40, wayThroughPixels, 4, 2, hud.color.getValue().getRGB());
        }
        if (this.millisThrough != null) {
            //Minecraft.getMinecraft().fontRendererObj.drawString(this.formatMillis(this.millisThrough), x, y + 56, this.barAndTextColor.getRGB());
        }
        if (this.millisAll != null) {
            //Minecraft.getMinecraft().fontRendererObj.drawString(this.formatMillis(this.millisAll), x + 265 - 82, y + 56, this.barAndTextColor.getRGB());
            if (this.millisThrough > this.millisAll) {
                this.update();
            }
        }
        Minecraft.getMinecraft().fontRendererObj.drawString(this.currentlyPlaying.getName(), x + 8, y + 14, -1);
        /*/if (this.currentlyPlaying != null) {
            Minecraft.getMinecraft().fontRendererObj.drawString(this.currentlyPlaying.getName(), x + 8, y + 10, -1);
            String authorList;
            if (this.currentlyPlaying.getArtists().length == 1) {
                authorList = this.currentlyPlaying.getArtists()[0].getName();
            }
            else if (this.currentlyPlaying.getArtists().length == 2) {
                authorList = this.currentlyPlaying.getArtists()[0].getName() + " and " + this.currentlyPlaying.getArtists()[1].getName();
            }
            else {
                final StringBuilder authors = new StringBuilder();
                int index = 0;
                for (final ArtistSimplified author : this.currentlyPlaying.getArtists()) {
                    if (index == this.currentlyPlaying.getArtists().length - 1) {
                        authors.append(" and ").append(author.getName());
                    }
                    else if (index == this.currentlyPlaying.getArtists().length - 2) {
                        authors.append(author.getName());
                    }
                    else {
                        authors.append(author.getName()).append(", ");
                    }
                    ++index;
                }
                authorList = authors.toString();
            }/*/
        if (this.currentlyPlaying.getArtists().length == 1) {
            Minecraft.getMinecraft().fontRendererObj.drawString(listauthors, x + 8, y + 24, -1);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawString(listauthors, x + 8, y + 24, -1);
        }
    }
    //Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("sv", "textures/logo.png"));
    //Gui.drawScaledCustomSizeModalRect(x + 258, y, 0.0f, 0.0f, 32, 32, 32, 32, 32.0f, 32.0f);
    //Minecraft.getMinecraft().renderEngine.bindTexture(Gui.icons);

    public void setupApi() {
        try {
            this.api.setAccessToken(this.credentials.getAccessToken());
            this.api.setRefreshToken(this.credentials.getRefreshToken());
            accessToken = this.credentials.getAccessToken();
            refreshToken = this.credentials.getRefreshToken();
            int time;
            if (this.credentials != null) {
                time = this.credentials.getExpiresIn() - 30;
            } else {
                time = 3600000;
            }
            new Thread("Spotify Token Renewer") {
                @Override
                public void run() {
                    while (true) {
                        try {
                            while (true) {
                                TimeUnit.SECONDS.sleep(time);
                                final AuthorizationCodeCredentials credentials1 = Spotify.this.api.authorizationCodePKCERefresh().build().execute();
                                Spotify.this.api.setAccessToken(credentials1.getAccessToken());
                                Spotify.this.api.setRefreshToken(credentials1.getRefreshToken());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
            }.start();
            new Thread("Spotify Timer Updater") {
                @Override
                public void run() {
                    while (true) {
                        try {
                            while (true) {
                                TimeUnit.SECONDS.sleep(1L);
                                if (Spotify.this.millisThrough != null) {
                                    Spotify.this.millisThrough += 1000;
                                }
                                Spotify.this.updatePercentage();

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
            }.start();
            this.ready = true;
            this.spotifyLogger.info("Logged In As: " + this.api.getCurrentUsersProfile().build().execute().getDisplayName());
            this.hasPremium = this.api.getCurrentUsersProfile().build().execute().getProduct().equals(ProductType.PREMIUM);
            this.spotifyLogger.warn("Premium: " + this.hasPremium);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatMillis(final int millis) {
        if (millis >= 3600000) {
            return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    @Subscribe
    public void onGuiSwitch(final UpdateEvent event) {
        try {

            if (this.ready && stopwatch.timeElapsed(5000)) {
                this.update();
                System.out.println("Updated.");
                stopwatch.resetTime();
                authenticated = true;
            }

        } catch (Exception ex) {
        }
    }

    private void update() {
        final Thread updateThread = new Thread("Spotify Updater Thread") {
            @Override
            public void run() {
                try {
                    final CurrentlyPlayingContext context = Spotify.this.api.getInformationAboutUsersCurrentPlayback().build().execute();
                    if (context != null) {
                        Spotify.this.currentlyPlaying = (Track) context.getItem();
                    }
                    if (context != null && Spotify.this.currentlyPlaying != null && Spotify.this.currentlyPlaying != context.getItem()) {
                        Spotify.this.currentlyPlaying = (Track) context.getItem();
                    }
                    if (context != null && Spotify.this.currentlyPlaying != null) {
                        Spotify.this.millisThrough = context.getProgress_ms();
                        Spotify.this.millisAll = Spotify.this.currentlyPlaying.getDurationMs();
                        Spotify.this.updatePercentage();
                        if (Spotify.this.previousSong == null) {
                            Spotify.this.previousSong = "";
                        }
                        if (!Spotify.this.previousSong.equals(Spotify.this.currentlyPlaying.getId())) {
                            Spotify.this.coverImageBuffer = ImageIO.read(new URL(Spotify.this.currentlyPlaying.getAlbum().getImages()[0].getUrl()));
                            final DynamicTexture dynamicTexture;
                            /*/Minecraft.func_71410_x().func_152344_a(() -> {
                                dynamicTexture = new DynamicTexture(Spotify.this.coverImageBuffer);
                                Spotify.this.coverImage = Minecraft.func_71410_x().func_110434_K().func_110578_a("cover.jpg", dynamicTexture);
                                return;
                            });/*/
                            Spotify.this.previousSong = Spotify.this.currentlyPlaying.getId();
                        }
                        Spotify.this.playing = context.getIs_playing();
                    } else {
                        Spotify.this.percentage = null;
                        Spotify.this.coverImageBuffer = null;
                        Spotify.this.coverImage = null;
                        Spotify.this.previousSong = "";
                        Spotify.this.playing = null;
                        Spotify.this.millisAll = null;
                        Spotify.this.millisThrough = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (updateThread.isAlive()) {
            return;
        }
        updateThread.start();
    }

    private void updatePercentage() {
        if (this.millisThrough != null && this.millisAll != null) {
            final double divided = this.millisThrough / (double) this.millisAll;
            final double p = divided * 100.0;
            this.percentage = (int) p;
        }
    }

    private Map<String, String> queryToMap(final String query) {
        final Map<String, String> result = new HashMap<String, String>();
        for (final String param : query.split("&")) {
            final String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public URI makeUri(final String uriString) {
        try {
            this.spotifyLogger.warn("Changing '" + uriString + "' into " + new URI(uriString));

            return new URI(uriString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onEnable() {
        try {
            if (!authenticated)
                initSpotify();
        } catch (Exception e) {

        }
    }
}