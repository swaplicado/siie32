/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swapms.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SExportLogUtils {
    private static final String LOG_DIRECTORY = "logs/export"; // Directorio donde se guardarán los logs
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Escribe un mensaje en un archivo de log asociado a un ID específico.
     * Si el archivo ya existe, concatena el mensaje. Si no existe, lo crea.
     * 
     * @param sufixFileName Identificador único para el archivo de log
     * @param message Mensaje a escribir en el log
     * @return 
     * @throws IOException Si ocurre un error de E/S al escribir el archivo
     */
    public static String writeToLogFile(String sufixFileName, String message) throws IOException {
        // Crear el directorio de logs si no existe
        Path logDirectory = Paths.get(LOG_DIRECTORY);
        if (!Files.exists(logDirectory)) {
            Files.createDirectories(logDirectory);
        }
        
        // Crear el nombre del archivo basado en el ID
        String fileName = String.format("sync_log_%s.txt", sufixFileName);
        Path logFile = logDirectory.resolve(fileName);
        
        // Formatear el mensaje con timestamp
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String formattedMessage = String.format("[%s] %s%n", timestamp, message);

        Logger.getLogger(SExportLogUtils.class.getName()).info(formattedMessage);
        
        // Escribir en el archivo (crear si no existe, añadir si existe)
        Files.write(logFile, formattedMessage.getBytes(), 
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND);
        
        return formattedMessage;
    }
    
    /**
     * Versión sobrecargada que no lanza excepciones
     * @param sufixFileName Identificador único para el archivo de log
     * @param message Mensaje a escribir en el log
     * @return string si la operación fue exitosa, null si hubo error
     */
    public static String safeWriteToLogFile(String sufixFileName, String message) {
        try {
            return writeToLogFile(sufixFileName, message);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
            return null;
        }
    }
}
