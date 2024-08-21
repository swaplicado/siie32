/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.utils;

import erp.siieapp.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author AdrianAviles
 */
public class SConectionUtils {

    private SGuiClient miClient;
    public int responseCode;
    public StringBuffer response;

    public SConectionUtils(SGuiClient client) {
        miClient = client;
    }

    public void conectWithSiieApp(String url, String method, String data, String authorization) {
        try {
            // URL del servidor web
            URL oUrl = new URL(url);

            // Abrir conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) oUrl.openConnection();

            // Establecer método de solicitud
            connection.setRequestMethod(method);

            if (authorization != null) {
                connection.setRequestProperty("Authorization", authorization);
            }

            // Habilitar escritura de datos
            connection.setDoOutput(true);

            // Establecer el tipo de contenido
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            if (method == "POST") {
                // Escribir los datos en el cuerpo de la solicitud
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();
            }

            // Obtener la respuesta del servidor
            this.responseCode = connection.getResponseCode();

            // Leer la respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            this.response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                this.response.append(line);
            }
            reader.close();
        } catch (Exception e) {

        }
    }
}
