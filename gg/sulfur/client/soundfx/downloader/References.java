package gg.sulfur.client.soundfx.downloader;

import gg.sulfur.client.Sulfur;
import net.minecraft.client.Minecraft;

import java.io.File;

public enum References {

    MEME("http://www.zerotwoclient.xyz/wavs/meme.wav"),
    MAINMENU("http://www.zerotwoclient.xyz/wavs/main.wav");

    private String url;

    References(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public static enum Files {
        DOWNLOADDIR(Sulfur.getInstance().getDownloadDir(), Minecraft.getMinecraft().mcDataDir.getAbsolutePath().replace("\\.", "") + "/Sulfur/Downloads"),
        MEMEFILE(new File(DOWNLOADDIR.getFile(), "meme.wav"), Minecraft.getMinecraft().mcDataDir.getAbsolutePath().replace("\\.", "") + "/Sulfur/Downloads/meme.wav"),
        MAINMENUFILE(new File(DOWNLOADDIR.getFile(), "main.wav"), Minecraft.getMinecraft().mcDataDir.getAbsolutePath().replace("\\.", "") + "/Sulfur/Downloads/main.wav");

        private File file;
        private String fileLocThing;

        public File getFile() {
            return file;
        }

        public String getFileLoc() {
            return fileLocThing;
        }

        Files (File file, String fileLocation) {
            this.file = file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }
}
