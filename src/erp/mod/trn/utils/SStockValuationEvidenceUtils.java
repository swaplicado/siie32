package erp.mod.trn.utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiSession;

/**
 * Guarda los archivos de evidencia generados por una verificación de
 * valuación de inventarios.
 */
public abstract class SStockValuationEvidenceUtils {

    private static final Logger LOGGER = Logger.getLogger(SStockValuationEvidenceUtils.class.getName());

    public static File[] save(SGuiSession session, SStockValuationVerify.VerificationResult result) throws IOException, SQLException {
        return save(session, result, null, null);
    }

    public static File[] save(SGuiSession session, SStockValuationVerify.VerificationResult result, Date dateStart, Date dateEnd) throws IOException, SQLException {
        File directory = new File(new File(System.getProperty("user.dir"), "logs"), "valuation").getAbsoluteFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("No se pudo crear el directorio de evidencia: " + directory.getAbsolutePath());
        }
        if (!directory.isDirectory()) {
            throw new IOException("La ruta de evidencia no es un directorio: " + directory.getAbsolutePath());
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(result.getStartedAt());
        File reportFile = new File(directory, "Bitacora_valuacion_inventarios_" + timestamp + ".xlsx");

        result.writeTo(reportFile);
        verifyFile(reportFile, "bitácora");
        LOGGER.info("Bitácora de valuación guardada en: " + reportFile.getAbsolutePath());

        if (dateStart == null || dateEnd == null) {
            Date[] period = SStockValuationSourceData.getLatestValuationPeriod(session);
            dateStart = period[0];
            dateEnd = period[1];
        }
        File[] sourceFiles = SStockValuationSourceData.writeTo(session, directory, timestamp, dateStart, dateEnd);
        for (File sourceFile : sourceFiles) {
            verifyFile(sourceFile, "datos fuente");
            LOGGER.info("Datos fuente de valuación guardados en: " + sourceFile.getAbsolutePath());
        }

        File[] files = new File[sourceFiles.length + 1];
        files[0] = reportFile;
        System.arraycopy(sourceFiles, 0, files, 1, sourceFiles.length);
        return files;
    }

    private static void verifyFile(File file, String description) throws IOException {
        if (!file.isFile() || file.length() == 0) {
            IOException ex = new IOException("El archivo de " + description + " no se creó correctamente: " + file.getAbsolutePath());
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        }
    }
}
