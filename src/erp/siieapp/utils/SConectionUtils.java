/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import sa.lib.gui.SGuiClient;

/**
 * Utilidad para realizar peticiones HTTP hacia la aplicación SIIE App.
 * <p>
 * Encapsula la apertura de una conexión {@link HttpURLConnection}, el envío del
 * cuerpo de la petición (en peticiones POST) y la lectura de la respuesta. El
 * código de respuesta HTTP y el cuerpo de la respuesta quedan disponibles en
 * los campos públicos {@link #responseCode} y {@link #response} tras llamar a
 * {@link #conectWithSiieApp}.
 * </p>
 *
 * @author Adrián Avilés
 */
public class SConectionUtils {

    /**
     * Cliente de sesión activo.
     */
    private SGuiClient miClient;
    /**
     * Código de respuesta HTTP obtenido en la última petición (ej. 200, 401,
     * 500).
     */
    public int responseCode;
    /**
     * Cuerpo de la respuesta HTTP obtenido en la última petición.
     */
    public StringBuffer response;

    /**
     * Crea una instancia de la utilidad con el cliente de sesión indicado.
     *
     * @param client cliente de sesión activo
     */
    public SConectionUtils(SGuiClient client) {
        miClient = client;
    }

    /**
     * Realiza una petición HTTP a SIIE App y almacena el resultado en
     * {@link #responseCode} y {@link #response}.
     * <p>
     * Siempre envía las cabeceras {@code Content-Type: application/json} y
     * {@code Accept: application/json}. El cuerpo ({@code data}) solo se
     * escribe en el stream de salida cuando {@code method} es {@code "POST"}.
     * Si ocurre cualquier excepción durante la conexión, se ignora
     * silenciosamente.
     * </p>
     *
     * @param url URL completa del endpoint a invocar
     * @param method método HTTP a usar ({@code "GET"} o {@code "POST"})
     * @param data cuerpo de la petición en formato JSON; solo se usa cuando
     * {@code method} es {@code "POST"}
     * @param authorization valor del encabezado {@code Authorization} (ej.
     * {@code "Bearer <token>"}), o {@code null} para omitirlo
     */
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
        }
        catch (Exception e) {

        }
    }
}
