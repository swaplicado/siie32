/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection; import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.bouncycastle.util.encoders.Base64;

public class SoapGetSatStatus {
    /**
     *
     */
    public static void main(String args[]) {
        try {
            // Creación de la conexión SoapStamp
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
 
            // Enviar mensaje SOAP al servidor SOAP
            //String url = "https://demo-facturacion.finkok.com/servicios/soap/cancel.wsdl";
            String url = "https://facturacion.finkok.com/servicios/soap/cancel.wsdl";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);
 
            // Procesar la respuesta SOAP
            printSOAPResponse(soapResponse);
 
            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error al conectarse al ws");
            e.printStackTrace();
        }
    }
 
    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
 
        String serverURI = "http://facturacion.finkok.com/cancel";
        String otherServer = "apps.services.soap.core.views";
        String serverCancellation = "http://facturacion.finkok.com/cancellation";
 
        // Cifrar el certificado a base64
        /* Original Finkok source code:
        Base64 base64 = new Base64();
        */
 
        //File cer = new File("aad990814bp7_1210261233s.cer.pem");
        File cer = new File("C:\\Temp\\csd.cer.pem");
        byte[] CerArray = new byte[(int) cer.length()];
        InputStream inputStream;
 
        String encodedCer = "";
        try {
            inputStream = new FileInputStream(cer);
            inputStream.read(CerArray);
            /* Original Finkok source code:
            encodedCer = base64.encodeToString(CerArray);
            */
            byte[] bytesSignatureEncoded = Base64.encode(CerArray);     // Sergio Flores, 2018-11-06: my code!
            encodedCer = new String(bytesSignatureEncoded, "UTF-8");    // Sergio Flores, 2018-11-06: my code!
        } catch (Exception e) {
            System.err.println("Error al cifrar el certificado a base64");
        }
 
        // Cifrar la key a base64
        //File key = new File("aad990814bp7_1210261233s.key.pem");
        File key = new File("C:\\Temp\\csd.key.pem.enc");
        byte[] KeyArray = new byte[(int) key.length()];
        InputStream inputStream1;
 
        String encodedKey = "";
        try {
            inputStream1 = new FileInputStream(key);
            inputStream1.read(KeyArray);
            /* Original Finkok source code:
            encodedKey = base64.encodeToString(KeyArray);
            */
            byte[] bytesSignatureEncoded = Base64.encode(KeyArray);     // Sergio Flores, 2018-11-06: my code!
            encodedKey = new String(bytesSignatureEncoded, "UTF-8");    // Sergio Flores, 2018-11-06: my code!
        } catch (Exception e) {
            System.err.println("Error al cifrar la key a base64");
        }
 
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("can", serverURI);
        envelope.addNamespaceDeclaration("apps", otherServer);
        envelope.addNamespaceDeclaration("can1", serverCancellation);
 
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("cancel", "can");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("UUIDS", "can");
        SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("uuids", "apps");
        SOAPElement soapBodyElem3 = soapBodyElem2.addChildElement("string", "can1");
        soapBodyElem3.addTextNode("C719D560-A03D-4677-80AB-69CE6C6B18BB");
        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("username", "can");
        soapBodyElem4.addTextNode("cfd@swaplicado.com.mx");
        SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("password", "can");
        soapBodyElem5.addTextNode("sWaP*2014!q5R7u9I2");
        SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("taxpayer_id", "can");
        soapBodyElem6.addTextNode("AET131112RQ2");
        SOAPElement soapBodyElem7 = soapBodyElem.addChildElement("cer", "can");
        soapBodyElem7.addTextNode(encodedCer);
        SOAPElement soapBodyElem8 = soapBodyElem.addChildElement("key", "can");
        soapBodyElem8.addTextNode(encodedKey);
 
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "can");
 
        soapMessage.saveChanges();
 
        /* Imprimir el Request */
        System.out.print("Request SOAP  : ");
        soapMessage.writeTo(System.out);
        System.out.println();
 
        return soapMessage;
    }
 
    /**
     * Método para imprimir el Response
     */
    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP : ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }
}