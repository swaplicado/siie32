/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.utils;

import erp.mod.trn.db.SDbStockValuationMvt;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationLogUtils {
    private static final String LOG_DIRECTORY = "logs"; // Directorio donde se guardarán los logs
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Escribe un mensaje en un archivo de log asociado a un ID específico.
     * Si el archivo ya existe, concatena el mensaje. Si no existe, lo crea.
     * 
     * @param sufixFileName Identificador único para el archivo de log
     * @param message Mensaje a escribir en el log
     * @throws IOException Si ocurre un error de E/S al escribir el archivo
     */
    public static void writeToLogFile(String sufixFileName, String message) throws IOException {
        // Crear el directorio de logs si no existe
        Path logDirectory = Paths.get(LOG_DIRECTORY);
        if (!Files.exists(logDirectory)) {
            Files.createDirectories(logDirectory);
        }
        
        // Crear el nombre del archivo basado en el ID
        String fileName = String.format("valuation_log_%s.txt", sufixFileName);
        Path logFile = logDirectory.resolve(fileName);
        
        // Formatear el mensaje con timestamp
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String formattedMessage = String.format("[%s] %s%n", timestamp, message);

        Logger.getLogger(SStockValuationLogUtils.class.getName()).info(formattedMessage);
        
        // Escribir en el archivo (crear si no existe, añadir si existe)
        Files.write(logFile, formattedMessage.getBytes(), 
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND);
    }
    
    /**
     * Versión sobrecargada que no lanza excepciones
     * @param sufixFileName Identificador único para el archivo de log
     * @param message Mensaje a escribir en el log
     * @return true si la operación fue exitosa, false si hubo error
     */
    public static boolean safeWriteToLogFile(String sufixFileName, String message) {
        try {
            writeToLogFile(sufixFileName, message);
            return true;
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
            return false;
        }
    }

    public static boolean logConsume(Date startDate, Date endDate, SDbStockValuationMvt oMvtConsumption, String customMessage) {
        // En base a los atributos de la clase SDbStockValuationMvt crear una cadena para el log
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Mensaje: ").append(customMessage).append("\n");
        logMessage.append("ID Val. Mvt.: ").append(oMvtConsumption.getPkStockValuationMvtId()).append("\n");
        logMessage.append("ítem: ").append("(ID: ").append(oMvtConsumption.getFkItemId()).append(") ").append(oMvtConsumption.getAuxItemDescription()).append("\n");
        logMessage.append("Cantidad: ").append(oMvtConsumption.getQuantityMovement()).append("\n");

        // logSufix formado por fecha inicial y final
        String logSufix = String.format("%s_%s", 
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate), 
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(endDate));

        // Escribir el mensaje en el archivo de log
        return safeWriteToLogFile(logSufix, logMessage.toString());
    }

    private static String getItemDescription(SGuiSession oSession, final int idItem) {
        String sql  = "SELECT item_key, name FROM erp.itmu_item WHERE id_item = " + idItem + ";";
        try (Statement statement = oSession.getStatement().getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return ("(ID: " + idItem + ") ") + resultSet.getString("item_key") + " - " + resultSet.getString("name");
            }
        } catch (SQLException e) {
            Logger.getLogger(SStockValuationLogUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return "";
    }
}
