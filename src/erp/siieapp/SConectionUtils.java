/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import sa.lib.gui.SGuiClient;

/**
 * Utilidad para establecer conexiones HTTP con servicios externos.
 *
 * Proporciona funcionalidad para realizar peticiones HTTP (GET, POST, etc.)
 * hacia servicios web externos, incluyendo autenticación mediante tokens
 * Bearer. Encapsula la lógica de conexión, envío de datos y lectura de
 * respuestas desde APIs REST.
 *
 * @author Adrián Avilés
 * @version 1.0
 */
public class SConectionUtils {

    /**
     * Cliente que contiene información de sesión
     */
    private SGuiClient miClient;
    /**
     * Código HTTP de respuesta del servidor
     */
    public int responseCode;
    /**
     * Contenido de la respuesta en formato texto
     */
    public StringBuffer response;

    /**
     * Constructor que inicializa la utilidad con un cliente de sesión.
     *
     * @param client cliente de sesión GUI
     */
    public SConectionUtils(SGuiClient client) {
        miClient = client;
    }

    /**
     * Realiza una conexión HTTP con un servicio externo.
     *
     * Envía una petición HTTP al servidor especificado, incluyendo datos en el
     * cuerpo para peticiones POST y token de autorización si se proporciona.
     * Lee la respuesta del servidor y la almacena en los atributos responseCode
     * y response.
     *
     * Nota: Los valores de responseCode y response se guardan como atributos
     * públicos para ser consultados después de la llamada. En caso de error,
     * los atributos pueden no estar correctamente inicializados.
     *
     * @param url URL del servidor a conectar
     * @param method método HTTP (GET, POST, etc.)
     * @param data datos a enviar en el cuerpo de la solicitud (para POST)
     * @param authorization encabezado de autorización opcional (ej: "Bearer
     * token")
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
