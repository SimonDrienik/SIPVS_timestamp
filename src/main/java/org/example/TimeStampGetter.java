package org.example;


import org.apache.cxf.helpers.IOUtils;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;


public class TimeStampGetter {

    public TimeStampGetter(){

    }
    public String getTS(String input) {

        try {
            String rawOutput = IOUtils.readStringFromStream(getWholeTimeStamp(input.getBytes()));
            System.out.println("Output: " + rawOutput);
            byte[] responseByteData = Base64.getDecoder().decode(rawOutput.getBytes());
            TimeStampResponse response = new TimeStampResponse(responseByteData);
            TimeStampToken timeStampToken = response.getTimeStampToken();
            System.out.println("Token:  " + new String(Base64.getEncoder().encode(timeStampToken.getEncoded())));
            return new String(Base64.getEncoder().encode(timeStampToken.getEncoded()));
        } catch (IOException | TSPException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private static InputStream getWholeTimeStamp(byte[] base64data) {
        InputStream in = null;
        try {
            OutputStream out = null;
            URL myUrl = new URL("http://test.ditec.sk/timestampws/TS.aspx");
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/timestamp-query");
            connection.setRequestProperty("Content-length", String.valueOf(base64data.length));

            out = connection.getOutputStream();
            out.write(base64data);
            out.flush();

            in = connection.getInputStream();
        } catch (Exception e) {
            System.out.println(e);
        }
        return in;
    }

}
