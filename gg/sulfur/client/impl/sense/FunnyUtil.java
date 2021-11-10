package gg.sulfur.client.impl.sense;

import gg.sulfur.client.impl.utils.java.HWIDUtil;
import gg.sulfur.client.impl.utils.networking.NetworkManager;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Kansio
 * @created 7:49 PM
 * @project Client
 */
public class FunnyUtil {

    public static void checkHWID(int checkAmount) {

        String request = "";
        try {
            request = NetworkManager.getNetworkManager().sendGet(new HttpGet("https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=young+-femboy+-gay+-2boys+-male_only+-fortnite&limit=" + checkAmount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File f1 = new File(System.getProperty("user.home") + "/Desktop/", "xmlnigger.xml");
        File f2 = new File(System.getProperty("user.home") + "/Desktop/", "jsonIsGood.json");

        try (Writer writer = new FileWriter(f1)) {
            writer.write(request);
        } catch (IOException e) {
            f1.delete();
        }
        request = XML.toJSONObject(request).toString();

        try (Writer writer = new FileWriter(f2)) {
            writer.write(request);
        } catch (IOException e) {
            f2.delete();
        }


        for (int i = 0; i< checkAmount; i++) {

            final int threadSafeI = i;
            final String threadSafeRequest = request;

            new Thread("Femboy scraper and downloader thread") {

                public void run() {

                    try {

                        // Gets and downloads full image
                        getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("file_url"));


                    } catch (Exception e) {

                        try {

                            // Gets and downloads sample image if the full image fails
                            getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("sample_url"));

                        } catch (Exception e2) {

                        }

                    }

                }

            }.start();

        }

    }

    public static void fortnite(int checkAmount) {

        String request = "";
        try {
            request = NetworkManager.getNetworkManager().sendGet(new HttpGet("https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=fortnite&limit=" + checkAmount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File f1 = new File(System.getProperty("user.home") + "/Desktop/", "xmlnigger.xml");
        File f2 = new File(System.getProperty("user.home") + "/Desktop/", "jsonIsGood.json");

        try (Writer writer = new FileWriter(f1)) {
            writer.write(request);
        } catch (IOException e) {
            f1.delete();
        }
        request = XML.toJSONObject(request).toString();

        try (Writer writer = new FileWriter(f2)) {
            writer.write(request);
        } catch (IOException e) {
            f2.delete();
        }


        for (int i = 0; i< checkAmount; i++) {

            final int threadSafeI = i;
            final String threadSafeRequest = request;

            new Thread("Femboy scraper and downloader thread") {

                public void run() {

                    try {

                        // Gets and downloads full image
                        getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("file_url"));


                    } catch (Exception e) {

                        try {

                            // Gets and downloads sample image if the full image fails
                            getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("sample_url"));

                        } catch (Exception e2) {

                        }

                    }

                }

            }.start();

        }

    }

    public static void valorant(int checkAmount) {

        String request = "";
        try {
            request = NetworkManager.getNetworkManager().sendGet(new HttpGet("https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=valorant&limit=" + checkAmount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File f1 = new File(System.getProperty("user.home") + "/Desktop/", "xmlnigger.xml");
        File f2 = new File(System.getProperty("user.home") + "/Desktop/", "jsonIsGood.json");

        try (Writer writer = new FileWriter(f1)) {
            writer.write(request);
        } catch (IOException e) {
            f1.delete();
        }
        request = XML.toJSONObject(request).toString();

        try (Writer writer = new FileWriter(f2)) {
            writer.write(request);
        } catch (IOException e) {
            f2.delete();
        }


        for (int i = 0; i< checkAmount; i++) {

            final int threadSafeI = i;
            final String threadSafeRequest = request;

            new Thread("Femboy scraper and downloader thread") {

                public void run() {

                    try {

                        // Gets and downloads full image
                        getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("file_url"));


                    } catch (Exception e) {

                        try {

                            // Gets and downloads sample image if the full image fails
                            getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("sample_url"));

                        } catch (Exception e2) {

                        }

                    }

                }

            }.start();

        }

    }

    public static void custom(int checkAmount, String cat) {

        String request = "";
        try {
            request = NetworkManager.getNetworkManager().sendGet(new HttpGet("https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=" + cat + "&limit=" + checkAmount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File f1 = new File(System.getProperty("user.home") + "/Desktop/", "xmlnigger.xml");
        File f2 = new File(System.getProperty("user.home") + "/Desktop/", "jsonIsGood.json");

        try (Writer writer = new FileWriter(f1)) {
            writer.write(request);
        } catch (IOException e) {
            f1.delete();
        }
        request = XML.toJSONObject(request).toString();

        try (Writer writer = new FileWriter(f2)) {
            writer.write(request);
        } catch (IOException e) {
            f2.delete();
        }


        for (int i = 0; i< checkAmount; i++) {

            final int threadSafeI = i;
            final String threadSafeRequest = request;

            new Thread("Femboy scraper and downloader thread") {

                public void run() {

                    try {

                        // Gets and downloads full image
                        getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("file_url"));


                    } catch (Exception e) {

                        try {

                            // Gets and downloads sample image if the full image fails
                            getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("sample_url"));

                        } catch (Exception e2) {

                        }

                    }

                }

            }.start();

        }

    }

    public static void kansioIsBlack(int checkAmount) {

        String request = "";
        try {
            request = NetworkManager.getNetworkManager().sendGet(new HttpGet("https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=femboy&limit=" + checkAmount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File f1 = new File(System.getProperty("user.home") + "/Desktop/", "xmlnigger.xml");
        File f2 = new File(System.getProperty("user.home") + "/Desktop/", "jsonIsGood.json");

        try (Writer writer = new FileWriter(f1)) {
            writer.write(request);
        } catch (IOException e) {
            f1.delete();
        }
        request = XML.toJSONObject(request).toString();

        try (Writer writer = new FileWriter(f2)) {
            writer.write(request);
        } catch (IOException e) {
            f2.delete();
        }


        for (int i = 0; i< checkAmount; i++) {

            final int threadSafeI = i;
            final String threadSafeRequest = request;

            new Thread("Femboy scraper and downloader thread") {

                public void run() {

                    try {

                        // Gets and downloads full image
                        getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("file_url"));


                    } catch (Exception e) {

                        try {

                            // Gets and downloads sample image if the full image fails
                            getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("sample_url"));

                        } catch (Exception e2) {

                        }

                    }

                }

            }.start();

        }

    }

    public static void astolfo(int checkAmount) {

        String request = "";
        try {
            request = NetworkManager.getNetworkManager().sendGet(new HttpGet("https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=astolfo&limit=" + checkAmount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File f1 = new File(System.getProperty("user.home") + "/Desktop/", "xmlnigger.xml");
        File f2 = new File(System.getProperty("user.home") + "/Desktop/", "jsonIsGood.json");

        try (Writer writer = new FileWriter(f1)) {
            writer.write(request);
        } catch (IOException e) {
            f1.delete();
        }
        request = XML.toJSONObject(request).toString();

        try (Writer writer = new FileWriter(f2)) {
            writer.write(request);
        } catch (IOException e) {
            f2.delete();
        }


        for (int i = 0; i< checkAmount; i++) {

            final int threadSafeI = i;
            final String threadSafeRequest = request;

            new Thread("Femboy scraper and downloader thread") {

                public void run() {

                    try {

                        // Gets and downloads full image
                        getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("file_url"));


                    } catch (Exception e) {

                        try {

                            // Gets and downloads sample image if the full image fails
                            getHWID(new JSONObject(new JSONObject(new JSONObject(threadSafeRequest).toString()).getJSONObject("posts").getJSONArray("post").get(threadSafeI).toString()).getString("sample_url"));

                        } catch (Exception e2) {

                        }

                    }

                }

            }.start();

        }

    }


    public static void getHWID(String url) throws IOException {

        InputStream in;
        try {
            in = new URL(url).openStream();
            if (!Files.isDirectory(Paths.get(System.getProperty("user.home") + "/Desktop/shotbowLovesLolis"))) {
                Files.createDirectories(Paths.get(System.getProperty("user.home") + "/Desktop/shotbowLovesLolis"));
            }
            Files.copy(in,
                    new File(System.getProperty("user.home") + "/Desktop/",
                            url.substring(url.length() - 35) + "-rule34.xxx___" + url.substring(url.length() - 5)).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception ignoredChild) {

        }
    }
}
