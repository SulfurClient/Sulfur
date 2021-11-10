package gg.sulfur.client.impl.utils.networking;

import gg.sulfur.client.impl.utils.java.HWIDUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class NetworkUtil {

    public static String getUsername() throws IOException, NoSuchAlgorithmException {
        URL url = new URL("http://shotbowxdisastinkystinkyloserandreallysuckslol.dotexe.cf:42069/hwids/" + HWIDUtil.getHWID());
        ArrayList<String> input = new ArrayList<>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("b: " + inputLine);
            input.add(inputLine);
        }
        in.close();

        return input.get(0);
    }

    public static String getRank() throws IOException, NoSuchAlgorithmException {
        URL url = new URL("http://shotbowxdisastinkystinkyloserandreallysuckslol.dotexe.cf:42069/hwids/" + HWIDUtil.getHWID());
        ArrayList<String> input = new ArrayList<>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("a: " + inputLine);
            input.add(inputLine);
        }
        in.close();

        return input.get(1);
    }

}
