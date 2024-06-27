package de.glowman554.framework.client.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class WebClient {
    public static String get(String _url, Map<String, String> headers) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        // con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        // con.setRequestProperty("Accept", "application/json");

        for (String key : headers.keySet()) {
            con.setRequestProperty(key, headers.get(key));
        }

        StringBuilder response = new StringBuilder();

        for (byte b : con.getInputStream().readAllBytes()) {
            response.append((char) b);
        }

        con.getInputStream().close();
        con.disconnect();

        return response.toString();
    }

    public static String post(String _url, String body, Map<String, String> headers) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setRequestMethod("POST");
        // con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        // con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(body.length()));

        for (String key : headers.keySet()) {
            con.setRequestProperty(key, headers.get(key));
        }

        con.getOutputStream().write(body.getBytes());

        StringBuilder response = new StringBuilder();

        for (byte b : con.getInputStream().readAllBytes()) {
            response.append((char) b);
        }

        con.getInputStream().close();
        con.disconnect();

        return response.toString();
    }


    public static void download(File destination, String _url) throws IOException {
        URL url = new URL(_url);
        BufferedInputStream in = new BufferedInputStream(url.openStream());

        FileOutputStream fileOutputStream = new FileOutputStream(destination);

        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        in.close();
        fileOutputStream.close();
    }
}

