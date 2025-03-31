package erp.mod.hrs.utils;

import erp.mod.hrs.db.SHrsConsts;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.lib.SLibTimeUtils;

/**
 * Clase para generar un reporte de cuotas patronales por centro de costo.
 *
 * @author Claudio Peña
 */
public class SReportEmpRetiremetReport {

    /**
     * Genera un reporte en formato CSV con los totales de cuotas patronales por centro de costo.
     *
     * @param connection Conexión a la base de datos
     * @param dateStart Fecha de inicio del rango (inicio del bimestre)
     * @param dateEnd Fecha de fin del rango (fin del bimestre)
     * @throws SQLException Si hay un error al ejecutar la consulta SQL
     * @throws IOException Si hay un error al generar el archivo CSV
     */
    public static void generateCcTotalsReport(Connection connection, Date dateStart, Date dateEnd) throws SQLException, IOException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PreparedStatement pstmtCurrency = null;
        ResultSet rsCurrency = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
        String formattedDateStart = dateFormat.format(dateStart);
        String formattedDateEnd = dateFormat.format(dateEnd);
        String monthName = monthFormat.format(dateStart).toUpperCase();
        int mnPeriodDays = SLibTimeUtils.countPeriodDays(dateStart, dateEnd);
        String currencyCode = "MXN"; 

        try {
            if (connection == null) {
                throw new IllegalArgumentException("La conexión a la base de datos no puede ser nula.");
            }
            if (dateStart == null || dateEnd == null) {
                throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas.");
            }

            String currencySql = "SELECT cur_key FROM erp.CFGU_CUR WHERE cur_key = 'MXN' LIMIT 1;";
            pstmtCurrency = connection.prepareStatement(currencySql);
            rsCurrency = pstmtCurrency.executeQuery();
            if (rsCurrency.next()) {
                currencyCode = rsCurrency.getString("cur_key");
            }

            int[] aDt = SLibTimeUtils.digestDate(dateStart);

            String msSql = "SELECT subquery.cc_id,subquery.cc_name,subquery.concept_type,SUM(subquery.amount) AS total_amount " +
                    "FROM (SELECT fc.id_cc AS cc_id,fc.cc AS cc_name,((e.sal_ssc*" + SHrsConsts.IMMS_RETIREMET + ")*" + mnPeriodDays + ") AS amount," +
                    "'SAR 2% " + monthName + " " + aDt[0] + "' AS concept_type " +
                    "FROM erp.hrsu_emp AS e " +
                    "INNER JOIN erp.bpsu_bp AS b ON b.id_bp=e.id_emp " +
                    "INNER JOIN erp.hrsu_dep AS d ON d.id_dep=e.fk_dep " +
                    "INNER JOIN HRS_CFG_ACC_DEP_PACK_CC AS c ON c.id_dep=d.id_dep " +
                    "INNER JOIN HRS_PACK_CC_CC AS cc ON cc.id_pack_cc=c.fk_pack_cc " +
                    "INNER JOIN FIN_CC AS fc ON fc.pk_cc=cc.id_cc " +
                    "INNER JOIN HRS_EMP_MEMBER AS mem ON mem.id_emp=e.id_emp " +
                    "WHERE NOT e.b_del AND e.b_act=1 " +
                    "UNION ALL " +
                    "SELECT fc.id_cc AS cc_id,fc.cc AS cc_name," +
                    "ROUND(((e.sal_ssc*(CASE WHEN e.sal_ssc<(SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta>='" + formattedDateStart + "' AND dt_sta<='" + formattedDateEnd + "' LIMIT 1) " +
                    "THEN (SELECT r.empr_rate FROM HRS_EMPR_SSC s INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc=r.id_empr_ssc WHERE s.tbl_year=" + aDt[0] + " AND r.id_row IN (1,2) ORDER BY r.id_row ASC LIMIT 1) " +
                    "ELSE COALESCE((SELECT r.empr_rate FROM HRS_EMPR_SSC s INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc=r.id_empr_ssc " +
                    "WHERE s.tbl_year=" + aDt[0] + " AND (e.sal_ssc/(SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) BETWEEN r.low_lim AND COALESCE(" +
                    "(SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim>r.low_lim),999999) ORDER BY r.low_lim ASC LIMIT 1)," +
                    "(SELECT r.empr_rate FROM HRS_EMPR_SSC s INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc=r.id_empr_ssc WHERE s.tbl_year=" + aDt[0] + " ORDER BY r.low_lim DESC LIMIT 1)) END))" +
                    "*" + mnPeriodDays + ")," + SHrsConsts.IMMS_RETIREMET + ") AS amount," +
                    "'RCV 2% " + monthName + " " + aDt[0] + "' AS concept_type " +
                    "FROM erp.hrsu_emp AS e " +
                    "INNER JOIN erp.bpsu_bp AS b ON b.id_bp=e.id_emp " +
                    "INNER JOIN erp.hrsu_dep AS d ON d.id_dep=e.fk_dep " +
                    "INNER JOIN HRS_CFG_ACC_DEP_PACK_CC AS c ON c.id_dep=d.id_dep " +
                    "INNER JOIN HRS_PACK_CC_CC AS cc ON cc.id_pack_cc=c.fk_pack_cc " +
                    "INNER JOIN FIN_CC AS fc ON fc.pk_cc=cc.id_cc " +
                    "INNER JOIN HRS_EMP_MEMBER AS mem ON mem.id_emp=e.id_emp " +
                    "WHERE NOT e.b_del AND e.b_act=1 " +
                    "UNION ALL " +
                    "SELECT fc.id_cc AS cc_id,fc.cc AS cc_name,((e.sal_ssc*" + SHrsConsts.IMMS_INFONAVIT + ")*" + mnPeriodDays + ") AS amount," +
                    "'INFONAVIT 5% " + monthName + " " + aDt[0] + "' AS concept_type " +
                    "FROM erp.hrsu_emp AS e " +
                    "INNER JOIN erp.bpsu_bp AS b ON b.id_bp=e.id_emp " +
                    "INNER JOIN erp.hrsu_dep AS d ON d.id_dep=e.fk_dep " +
                    "INNER JOIN HRS_CFG_ACC_DEP_PACK_CC AS c ON c.id_dep=d.id_dep " +
                    "INNER JOIN HRS_PACK_CC_CC AS cc ON cc.id_pack_cc=c.fk_pack_cc " +
                    "INNER JOIN FIN_CC AS fc ON fc.pk_cc=cc.id_cc " +
                    "INNER JOIN HRS_EMP_MEMBER AS mem ON mem.id_emp=e.id_emp " +
                    "WHERE NOT e.b_del AND e.b_act=1) AS subquery " +
                    "GROUP BY subquery.cc_id,subquery.cc_name,subquery.concept_type " +
                    "ORDER BY subquery.cc_id,subquery.cc_name,subquery.concept_type;";

            pstmt = connection.prepareStatement(msSql);
            rs = pstmt.executeQuery();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            fileChooser.setSelectedFile(new File("CuotasPatronalesPorCentroCosto_" + currentYear + "_" + monthName + ".csv"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv");
            fileChooser.setFileFilter(filter);
            fileChooser.setDialogTitle("Guardar archivo CSV");

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().endsWith(".csv")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
                }

                try (FileWriter writer = new FileWriter(fileToSave)) {
                    writer.append("No. centro de costo,Centro de costo,Concepto,Debe,Codigo moneda\n");

                    while (rs.next()) {
                        String ccId = rs.getString("cc_id") != null ? rs.getString("cc_id") : "";
                        String ccName = rs.getString("cc_name") != null ? rs.getString("cc_name") : "";
                        String conceptType = rs.getString("concept_type");
                        double totalAmount = rs.getDouble("total_amount");

                        String cleanedCcId = cleanString(ccId);
                        String cleanedCcName = cleanString(ccName);
                        String cleanedConceptType = cleanString(conceptType);
                        String cleanedCurrencyCode = cleanString(currencyCode);

                        writer.append(cleanedCcId + "," +
                            cleanedCcName + "," +
                            cleanedConceptType + "," +
                            totalAmount + "," +
                            cleanedCurrencyCode + "\n");
                    }
                    writer.flush();
                }

                System.out.println("Archivo CSV generado exitosamente en: " + fileToSave.getAbsolutePath());
            } else {
                System.out.println("Guardado cancelado por el usuario.");
            }

        } catch (SQLException e) {
            throw new SQLException("Error al ejecutar la consulta SQL: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new IOException("Error al generar el archivo CSV: " + e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar ResultSet: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
            if (rsCurrency != null) {
                try {
                    rsCurrency.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar ResultSet de moneda: " + e.getMessage());
                }
            }
            if (pstmtCurrency != null) {
                try {
                    pstmtCurrency.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar PreparedStatement de moneda: " + e.getMessage());
                }
            }
        }
    }

    private static String cleanString(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }
}