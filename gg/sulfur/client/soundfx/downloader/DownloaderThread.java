package gg.sulfur.client.soundfx.downloader;

import net.minecraft.client.Minecraft;
import gg.sulfur.client.impl.utils.java.HWIDUtil;

import java.io.File;
import java.io.IOException;

public class DownloaderThread extends java.lang.Thread {

    @Override
    public void run() {

        File file = References.Files.DOWNLOADDIR.getFile();
        if(!file.exists()){
            System.out.println("Creating file in " + References.Files.DOWNLOADDIR.getFileLoc());
            file.mkdirs();
        }

        try {
            HWIDUtil.saveFile(References.MAINMENU.getUrl(), References.Files.MAINMENUFILE.getFile().getCanonicalPath());
            HWIDUtil.saveFile(References.MEME.getUrl(), References.Files.MEMEFILE.getFile().getCanonicalPath());
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Downloaded audio files");

        try {
            System.out.println(Minecraft.getMinecraft().mcDataDir.getCanonicalPath());

            System.out.println(References.Files.DOWNLOADDIR.getFile().getCanonicalPath());
            System.out.println(References.MAINMENU.getUrl());
            System.out.println(References.MEME.getUrl());
            System.out.println(References.Files.MEMEFILE.getFile().getCanonicalPath());
            System.out.println(References.Files.MAINMENUFILE.getFile().getCanonicalPath());
        }
        catch(Exception e) {

        }

    }

}
