package erp.mod.hrs.utils;

import erp.mod.hrs.db.SHrsConsts;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
 *
 * @author Claudio Peña
 */
public class SReportEmpRetiremet {

    /**
     *
     * @param connection Conexión a la base de datos
     * @param dateStart Fecha de inicio del rango (inicio del bimestre)
     * @param dateEnd Fecha de fin del rango (fin del bimestre)
     * @throws SQLException Si hay un error al ejecutar la consulta SQL
     * @throws IOException Si hay un error al generar el archivo CSV
     */
    public static void generateCcTotalsReportStatic(Connection connection, Date dateStart, Date dateEnd) throws SQLException, IOException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
        String formattedDateStart = dateFormat.format(dateStart);
        String monthName = monthFormat.format(dateStart).toUpperCase();
        String formattedDateEnd = dateFormat.format(dateEnd);
        int mnPeriodDays = SLibTimeUtils.countPeriodDays(dateStart, dateEnd);

        try {
            if (connection == null) {
                throw new IllegalArgumentException("La conexión a la base de datos no puede ser nula.");
            }
            if (dateStart == null || dateEnd == null) {
                throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas.");
            }
            
            int[] aDt = SLibTimeUtils.digestDate(dateStart);

            String query = "SELECT " +
                "e.id_emp, " +
                "e.num, " +
                "e.b_act, " +
                "b.bp, " +
                "e.sal_ssc, " +
                "d.name, " +
                "fc.cc, " +
                "fc.id_cc, " +
                "(SELECT wage FROM HRS_MWZ_WAGE ORDER BY id_wage DESC LIMIT 1 ) AS _SM, " +  // Salario mínimo (_SM)
                "(SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1) AS _UMA, " +       // Monto de UMA más reciente
                "(e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) AS cos, " +  // Cálculo de cos (salario / UMA)
                "CASE " +  // Determinar el id_row
                "    WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN 1 " + // Salario menor a _SM
                "    ELSE COALESCE( " +
                "        (SELECT r.id_row " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year =  " + aDt[0] + " "+
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " + // Valor grande para el límite superior si no hay siguiente
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.id_row " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " "+
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS id_row, " +
                "CASE " +  // low_lim_type
                "    WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN 'SM' " +
                "    ELSE COALESCE( " +
                "        (SELECT r.low_lim_type " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year =  " + aDt[0] + " "+
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " +
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.low_lim_type " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " "+
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS low_lim_type, " +
                "CASE " +  // low_lim
                "    WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN 1.0 " +
                "    ELSE COALESCE( " +
                "        (SELECT r.low_lim " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " "+
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " +
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.low_lim " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " "+
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS low_lim, " +
                " @empr_rate:= CASE " +  // empr_rate
                "     WHEN e.sal_ssc < (SELECT wage FROM HRS_MWZ_WAGE WHERE dt_sta >= '" + formattedDateStart + "' AND dt_sta <= '" + formattedDateEnd + "') " +
                "    THEN (SELECT r.empr_rate " +
                "          FROM HRS_EMPR_SSC s " +
                "          INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "          WHERE s.tbl_year = " + aDt[0] + " AND r.id_row IN (1, 2) " +
                "          ORDER BY r.id_row ASC LIMIT 1) " +
                "    ELSE COALESCE( " +
                "        (SELECT r.empr_rate " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " "+
                "           AND (e.sal_ssc / (SELECT amt FROM HRS_UMA ORDER BY id_uma DESC LIMIT 1)) " +
                "               BETWEEN r.low_lim AND COALESCE( " +
                "                   (SELECT MIN(low_lim) FROM HRS_EMPR_SSC_ROW WHERE low_lim > r.low_lim), " +
                "                   999999 " +
                "               ) " +
                "         ORDER BY r.low_lim ASC LIMIT 1), " +
                "        (SELECT r.empr_rate " +
                "         FROM HRS_EMPR_SSC s " +
                "         INNER JOIN HRS_EMPR_SSC_ROW r ON s.id_empr_ssc = r.id_empr_ssc " +
                "         WHERE s.tbl_year = " + aDt[0] + " "+
                "         ORDER BY r.low_lim DESC LIMIT 1) " +
                "    ) " +
                "END AS empr_rate, " +
                mnPeriodDays + " AS period_days, " +
                SHrsConsts.IMMS_RETIREMET + " AS f_retirement, " +
                SHrsConsts.IMMS_INFONAVIT + " AS f_infonavit, " +
                "((e.sal_ssc * " + SHrsConsts.IMMS_RETIREMET + ") * " + mnPeriodDays + " ) AS t_retirement, " +
                "((e.sal_ssc * @empr_rate) * " + mnPeriodDays + " ) AS t_unemployment_old_age, " +
                "((e.sal_ssc * " + SHrsConsts.IMMS_INFONAVIT + ") * " + mnPeriodDays + " ) AS t_infonavit " +
                "FROM erp.hrsu_emp AS e " +
                "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = e.id_emp " +
                "INNER JOIN erp.hrsu_dep AS d ON d.id_dep = e.fk_dep " +
                "INNER JOIN HRS_CFG_ACC_DEP_PACK_CC AS c ON c.id_dep = d.id_dep " +
                "INNER JOIN HRS_PACK_CC_CC AS cc ON cc.id_pack_cc = c.fk_pack_cc " +
                "INNER JOIN FIN_CC AS fc ON fc.pk_cc = cc.id_cc " +
                "INNER JOIN HRS_EMP_MEMBER AS mem ON mem.id_emp = e.id_emp " +
                "WHERE " +
                " e.b_act = 0 AND NOT e.b_del " +
                "GROUP BY e.id_emp " +
                "ORDER BY fc.id_cc, fc.cc";

            pstmt = connection.prepareStatement(query);

            rs = pstmt.executeQuery();

            Map<String, CcTotals> ccTotalsMap = new HashMap<>();

            while (rs.next()) {
                final String finalIdCc = rs.getString("fc.id_cc") != null ? rs.getString("fc.id_cc") : "";
                final String finalCcName = rs.getString("fc.cc") != null ? rs.getString("fc.cc") : "";
                final double finalTRetirement = rs.getDouble("t_retirement");
                final double finalTUnemploymentOldAge = rs.getDouble("t_unemployment_old_age");
                final double finalTInfonavit = rs.getDouble("t_infonavit");

                ccTotalsMap.computeIfAbsent(finalIdCc, k -> new CcTotals(finalCcName, finalIdCc))
                           .addTotals(finalTRetirement, finalTUnemploymentOldAge, finalTInfonavit);
            }

            if (ccTotalsMap.isEmpty()) {
                throw new SQLException("No se encontraron datos para generar el reporte.");
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            fileChooser.setSelectedFile(new File("CuotasPatronales_" + currentYear + "_" + monthName + ".csv"));
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
                    writer.append("No. centro de costo,Centro de costo,RETIRO,IMSS CESANTIA Y VEJEZ,IMSS INFONAVIT \n");

                    for (CcTotals totals : ccTotalsMap.values()) {
                        String cleanedCcName = cleanString(totals.ccName);
                        String cleanedIdCc = cleanString(totals.idCc);
                        writer.append(cleanedIdCc + "," + cleanedCcName + "," +
                                    totals.tRetirement + "," + totals.tUnemploymentOldAge + "," + 
                                    totals.tInfonavit + "\n");
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
        }
    }

    private static String cleanString(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }
    
    private static class CcTotals {
        String idCc;
        String ccName;
        double tRetirement = 0.0;
        double tUnemploymentOldAge = 0.0;
        double tInfonavit = 0.0;

        CcTotals(String ccName, String idCc) {
            this.idCc = (idCc != null) ? idCc : "";
            this.ccName = (ccName != null) ? ccName : "";
        }

        void addTotals(double retirement, double unemploymentOldAge, double infonavit) {
            this.tRetirement += retirement;
            this.tUnemploymentOldAge += unemploymentOldAge;
            this.tInfonavit += infonavit;
        }
    }
}