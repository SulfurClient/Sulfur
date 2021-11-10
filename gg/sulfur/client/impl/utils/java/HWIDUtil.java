package gg.sulfur.client.impl.utils.java;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDUtil {

    public static String getHWID() throws NoSuchAlgorithmException {
        String s = "";
        final String hhfdssdjfdsd = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = hhfdssdjfdsd.getBytes(StandardCharsets.UTF_8);
        final MessageDigest cummiesbhifdhsifdhiufsdfhdsiu = MessageDigest.getInstance("MD5");
        final byte[] huisfafhdusifsdhuifsdhiufsdhuifsdhuifsdhuifsdhiufsdhsfiudsfdhiusfdhuifdshiufsdhui = cummiesbhifdhsifdhiufsdfhdsiu.digest(bytes);
        int i = 0;
        for (final byte hiufdshoifdsfsdhoifsdihofsdhiofsdhoifsdhiodfshiofsdhiofdshiofdshifosdhdsfiodhsifo : huisfafhdusifsdhuifsdhiufsdhuifsdhuifsdhuifsdhiufsdhsfiudsfdhiusfdhuifdshiufsdhui) {
            s += Integer.toHexString((hiufdshoifdsfsdhoifsdihofsdhiofsdhoifsdhiodfshiofsdhiofdshiofdshifosdhdsfiodhsifo & 0xFF) | 0x300).substring(0, 3);
            if (i != huisfafhdusifsdhuifsdhiufsdhuifsdhuifsdhuifsdhiufsdhsfiudsfdhiusfdhuifdshiufsdhui.length - 1) {
                s += "-";
            }
            i++;
        }
        return s;
    }

    public static boolean get() {
        int lines = 0;
        try {
            URL url = new URL("http://shotbowxdisastinkystinkyloserandreallysuckslol.dotexe.cf:42069/hwids/" + getHWID());
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines += 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                NotificationUtil.sendError("HWID", "You aren't whitelisted, please dm the bot with ur license!");
                try {
                    System.exit(-1);
                }catch (Exception e) {

                }
            } catch (AWTException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     *  Download any file from a url
     *
     * @param url The url it download from
     * @param file The location for the file to be saved to
     * @throws IOException
     */
    public static boolean saveFile(String url, String file) throws IOException {
        return false;
    }
}
