package org.example;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import sk.fiit.sipvs.sv.utils.Converter;
//import sk.fiit.sipvs.sv.verify.DocumentVerificationException;


public class TimeStampInserter {

    public TimeStampInserter() throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException, TransformerException {
    }

    public static void doTimeStamp() throws ParserConfigurationException, IOException, SAXException {

        File xmlFile = new File("students.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);


        String timeStampValue = getTimeStamp(document.getElementsByTagName("ds:SignatureValue").item(0).getTextContent());

        Element unsignedProperties = document.createElement("xades:UnsignedProperties");

        Element unsignedSignatureProperties = document.createElement("xades:UnsignedSignatureProperties");

        Element signatureTimestamp = document.createElement("xades:SignatureTimeStamp");
        signatureTimestamp.setAttribute("Id", "TSID" + 1 );

        Element encapsulatedTimeStamp = document.createElement("xades:EncapsulatedTimeStamp");

        unsignedProperties.appendChild(unsignedSignatureProperties);
        unsignedSignatureProperties.appendChild(signatureTimestamp);
        signatureTimestamp.appendChild(encapsulatedTimeStamp);
        encapsulatedTimeStamp.appendChild(document.createTextNode(timeStampValue));
        document.getElementsByTagName("xades:QualifyingProperties").item(0).appendChild(unsignedProperties);

        try {
            //Generate XML file
            Source xmlSource = new DOMSource(document);
            Result result = new StreamResult(new FileOutputStream("signedWithStamp" + 1 + ".xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.transform(xmlSource, result);
        } catch (
                TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }



    static public String getTimeStamp(String xmlData) {
        TimeStamp TimeClient = new TimeStamp();
        String timeStampString = TimeClient.getTS(xmlData);
        return timeStampString;
    }
}
