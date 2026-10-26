package erp.mod.trn.utils;

import erp.mod.SModConsts;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sa.lib.gui.SGuiSession;

/**
 * Exporta las tablas fuente de una valuación de inventarios.
 */
public abstract class SStockValuationSourceData {

    private static final Logger LOGGER = Logger.getLogger(SStockValuationSourceData.class.getName());
    private static final int MAX_EXCEL_ROWS = 1048576;
    private static final int MAX_CELL_TEXT_LENGTH = 30000;

    public static Date[] getLatestValuationPeriod(SGuiSession session) throws SQLException {
        String sql = "SELECT dt_sta, dt_end FROM trn_stk_val WHERE b_del = 0 ORDER BY dt_end DESC LIMIT 1;";
        try (Statement statement = session.getStatement().getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new SQLException("No existe una valuación vigente para determinar el periodo de exportación.");
            }
            return new Date[] { resultSet.getDate("dt_sta"), resultSet.getDate("dt_end") };
        }
    }

    public static File[] writeTo(SGuiSession session, File directory, String timestamp, Date dateStart, Date dateEnd) throws SQLException, IOException {
        if (dateStart == null || dateEnd == null || dateStart.after(dateEnd)) {
            throw new IllegalArgumentException("El periodo de la valuación no es válido.");
        }

        String start = new SimpleDateFormat("yyyy-MM-dd").format(dateStart);
        String end = new SimpleDateFormat("yyyy-MM-dd").format(dateEnd);
        String kardexTable = SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX);
        File[] files = new File[] {
            new File(directory, "Datos_DIOG_y_partidas_" + timestamp + ".xlsx"),
            new File(directory, "Datos_trn_stk_" + timestamp + ".xlsx"),
            new File(directory, "Datos_trn_stk_val_kardex_" + timestamp + ".xlsx")
        };

        writeWorkbook(session, files[0], "Movimientos de almacén", start, end, new SnapshotQuery[] {
            new SnapshotQuery("trn_diog", "SELECT d.* FROM trn_diog d WHERE d.dt BETWEEN '" + start + "' AND '" + end + "';"),
            new SnapshotQuery("trn_diog_ety", "SELECT e.* FROM trn_diog_ety e INNER JOIN trn_diog d "
                    + "ON d.id_year = e.id_year AND d.id_doc = e.id_doc "
                    + "WHERE d.dt BETWEEN '" + start + "' AND '" + end + "';")
        });
        writeWorkbook(session, files[1], "Existencias de almacén", start, end, new SnapshotQuery[] {
            new SnapshotQuery("trn_stk", "SELECT s.* FROM trn_stk s WHERE s.dt BETWEEN '" + start + "' AND '" + end + "';")
        });
        writeWorkbook(session, files[2], "Kardex de valuación", start, end, new SnapshotQuery[] {
            new SnapshotQuery("trn_stk_val_kardex", "SELECT k.* FROM " + kardexTable + " k WHERE k.dt_mov BETWEEN '" + start + "' AND '" + end + "';")
        });
        return files;
    }

    private static void writeWorkbook(SGuiSession session, File file, String title, String start, String end, SnapshotQuery[] queries) throws SQLException, IOException {
        Date captureStartedAt = new Date();
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = createHeaderStyle(workbook);
        Sheet controlSheet = workbook.createSheet("Control");
        Row titleRow = controlSheet.createRow(0);
        writeCell(titleRow, 0, title, headerStyle);
        writeControlValue(controlSheet, 1, "Inicio del periodo", start);
        writeControlValue(controlSheet, 2, "Fin del periodo", end);
        writeControlValue(controlSheet, 3, "Inicio de captura", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(captureStartedAt));

        Sheet queriesSheet = workbook.createSheet("Consultas");
        Row queryHeader = queriesSheet.createRow(0);
        writeCell(queryHeader, 0, "Tabla", headerStyle);
        writeCell(queryHeader, 1, "Consulta SQL aplicada", headerStyle);
        Sheet longValuesSheet = workbook.createSheet("Datos largos");
        Row longHeader = longValuesSheet.createRow(0);
        writeCell(longHeader, 0, "Tabla", headerStyle);
        writeCell(longHeader, 1, "Fila", headerStyle);
        writeCell(longHeader, 2, "Columna", headerStyle);
        writeCell(longHeader, 3, "Parte", headerStyle);
        writeCell(longHeader, 4, "Contenido", headerStyle);

        for (int index = 0; index < queries.length; index++) {
            SnapshotQuery query = queries[index];
            Row queryRow = queriesSheet.createRow(index + 1);
            queryRow.createCell(0).setCellValue(query.name);
            queryRow.createCell(1).setCellValue(query.sql);
            LOGGER.info("Iniciando exportación de " + query.name + " al archivo " + file.getName());
            long rowCount = exportQuery(session, workbook, query, longValuesSheet);
            writeControlValue(controlSheet, index + 4, "Filas de " + query.name, Long.toString(rowCount));
        }

        writeControlValue(controlSheet, queries.length + 4, "Fin de captura", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        controlSheet.setColumnWidth(0, 18000);
        controlSheet.setColumnWidth(1, 12000);
        queriesSheet.setColumnWidth(0, 6000);
        queriesSheet.setColumnWidth(1, 20000);
        longValuesSheet.setColumnWidth(0, 6000);
        longValuesSheet.setColumnWidth(1, 3000);
        longValuesSheet.setColumnWidth(2, 6000);
        longValuesSheet.setColumnWidth(3, 3000);
        longValuesSheet.setColumnWidth(4, 18000);

        LOGGER.info("Escribiendo archivo de datos fuente de valuación: " + file.getAbsolutePath());
        try (FileOutputStream output = new FileOutputStream(file)) {
            workbook.write(output);
        }
        LOGGER.info("Terminó la escritura del archivo de datos fuente de valuación: " + file.getAbsolutePath());
    }

    private static long exportQuery(SGuiSession session, XSSFWorkbook workbook, SnapshotQuery query, Sheet longValuesSheet) throws SQLException, IOException {
        long rowCount = 0;
        int sheetNumber = 1;
        int rowIndex = 1;

        try (Statement statement = session.getStatement().getConnection().createStatement(
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(Integer.MIN_VALUE);
            try (ResultSet resultSet = statement.executeQuery(query.sql)) {
                ResultSetMetaData metadata = resultSet.getMetaData();
                int columnCount = metadata.getColumnCount();
                Sheet sheet = createDataSheet(workbook, query.name, sheetNumber, metadata, columnCount);

                while (resultSet.next()) {
                    if (rowIndex >= MAX_EXCEL_ROWS) {
                        sheet = createDataSheet(workbook, query.name, ++sheetNumber, metadata, columnCount);
                        rowIndex = 1;
                    }

                    Row row = sheet.createRow(rowIndex);
                    for (int column = 1; column <= columnCount; column++) {
                        String value = getCellValue(resultSet.getObject(column));
                        if (value.length() > MAX_CELL_TEXT_LENGTH) {
                            String marker = "Ver conjunto 'Datos largos', fila " + (rowIndex + 1) + ", columna " + metadata.getColumnLabel(column);
                            row.createCell(column - 1).setCellValue(marker);
                            writeLongValue(longValuesSheet, query.name, rowIndex + 1, metadata.getColumnLabel(column), value);
                        }
                        else {
                            row.createCell(column - 1).setCellValue(value);
                        }
                    }
                    rowIndex++;
                    rowCount++;
                    if (rowCount % 1000 == 0) {
                        LOGGER.info("Exportación en progreso de " + query.name + "; filas: " + rowCount);
                    }
                }
            }
        }
        LOGGER.info("Terminó exportación de " + query.name + "; filas: " + rowCount);
        return rowCount;
    }

    private static Sheet createDataSheet(XSSFWorkbook workbook, String baseName, int sheetNumber, ResultSetMetaData metadata, int columnCount) throws SQLException {
        String suffix = sheetNumber == 1 ? "" : "_" + sheetNumber;
        int maxBaseNameLength = 31 - suffix.length();
        String sheetName = baseName.substring(0, Math.min(baseName.length(), maxBaseNameLength)) + suffix;
        Sheet sheet = workbook.createSheet(sheetName);
        Row header = sheet.createRow(0);
        for (int column = 1; column <= columnCount; column++) {
            header.createCell(column - 1).setCellValue(metadata.getColumnLabel(column));
        }
        return sheet;
    }

    private static String getCellValue(Object value) throws SQLException, IOException {
        if (value == null) {
            return "<NULL>";
        }
        if (value instanceof byte[]) {
            return Base64.getEncoder().encodeToString((byte[]) value);
        }
        if (value instanceof Blob) {
            Blob blob = (Blob) value;
            long length = blob.length();
            if (length > Integer.MAX_VALUE) {
                throw new IOException("El valor binario excede el tamaño que se puede exportar a Excel.");
            }
            return Base64.getEncoder().encodeToString(blob.getBytes(1, (int) length));
        }
        if (value instanceof Clob) {
            Clob clob = (Clob) value;
            long length = clob.length();
            if (length > Integer.MAX_VALUE) {
                throw new IOException("El valor de texto excede el tamaño que se puede exportar a Excel.");
            }
            return clob.getSubString(1, (int) length);
        }
        return value.toString();
    }

    private static void writeLongValue(Sheet sheet, String tableName, int rowNumber, String columnName, String value) {
        int part = 1;
        for (int start = 0; start < value.length(); start += MAX_CELL_TEXT_LENGTH) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(tableName);
            row.createCell(1).setCellValue(rowNumber);
            row.createCell(2).setCellValue(columnName);
            row.createCell(3).setCellValue(part++);
            row.createCell(4).setCellValue(value.substring(start, Math.min(start + MAX_CELL_TEXT_LENGTH, value.length())));
        }
    }

    private static void writeControlValue(Sheet sheet, int rowIndex, String label, String value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
    }

    private static void writeCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private static CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }

    private static final class SnapshotQuery {
        private final String name;
        private final String sql;

        private SnapshotQuery(String name, String sql) {
            this.name = name;
            this.sql = sql;
        }
    }
}
