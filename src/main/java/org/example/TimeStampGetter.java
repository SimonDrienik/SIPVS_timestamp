package org.example;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;

import java.io.IOException;
import java.util.Base64;


public class TimeStampGetter {

    public TimeStampGetter(){

    }
    public String getTS(String input) {

        //TimeStampRequestGenerator requestGenerator = new TimeStampRequestGenerator();
        //requestGenerator.setCertReq(false);
        //TimeStampRequest TSrequest = requestGenerator.generate(TSPAlgorithms.SHA1, input.getBytes());

        try {
            //byte[] encodedRequest = TSrequest.getEncoded();
            String rawOutput = IOUtils.readStringFromStream(getWholeTimeStamp(input.getBytes()));
            System.out.println("Output: " + rawOutput);
            byte[] responseByteData = Base64.getDecoder().decode(rawOutput.getBytes());
            //String rawOutputParsed = parseXmlResponse(IOUtils.readStringFromStream(getWholeTimeStampXML(Base64.getEncoder().encodeToString(encodedRequest))));
            //System.out.println("Output: " + rawOutputParsed);
            //byte[] responseByteData = Base64.getDecoder().decode(rawOutputParsed.getBytes());
            TimeStampResponse response = new TimeStampResponse(responseByteData);
            TimeStampToken timeStampToken = response.getTimeStampToken();
            System.out.println("Token:  " + new String(Base64.getEncoder().encode(timeStampToken.getEncoded())));
            return new String(Base64.getEncoder().encode(timeStampToken.getEncoded()));
        } catch (IOException | TSPException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
