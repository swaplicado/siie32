/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Claudio Peña
 */
public class SViewStockCostUnit extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private static String[] columns = {"Numero de parte", "Ítem", "Unidad", "Costo unitario"};
    private static final List<SDataStockExcel> stockExportExcel = new ArrayList<>();
    
    private javax.swing.JButton jbExportExcel;
    private javax.swing.JButton jbExportTxt;
    private erp.lib.table.STabFilterDate moTabFilterDate;

    /*
     * @param auxType01 Constants defined in SDataConstats (TRNX_STK_...).
     */
    public SViewStockCostUnit(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_STK_STK, auxType01);
        initComponents();
    }
      
    private void initComponents() {
        int i = 0;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        moTabFilterDate = new STabFilterDate(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(moTabFilterDate);
       
        jbExportExcel = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_TYPE));
        jbExportExcel.setPreferredSize(new Dimension(23, 23));
        jbExportExcel.setToolTipText("Importar a excel");
        jbExportExcel.addActionListener(this);
        addTaskBarUpperComponent(jbExportExcel);
        jbExportTxt = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_TYPE));
        jbExportTxt.setPreferredSize(new Dimension(23, 23));
        jbExportTxt.setToolTipText("Importar a txt");
        jbExportTxt.addActionListener(this);
        addTaskBarUpperComponent(jbExportTxt);
                
        switch (mnTabTypeAux01) {
            case SDataConstants.TRNX_STK_ITEM:
                aoTableColumns = new STableColumn[8];
                break;
            default:
                aoTableColumns = null;
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.part_num", "Número parte", 75);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "priceUnit", "Ultimo precio compra", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_MovI", "Entradas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_MovO", "Salidas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "s.dt", "Ultima fecha compra", STableConstants.WIDTH_DATE);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.TRN_LOT);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.TRNX_MAINT_DIOG);
        mvSuscriptors.add(SDataConstants.TRN_STK_CFG);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND_ETY);

        populateTable();

    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    public void actionExportExcel() throws FileNotFoundException, IOException, SQLException {
        File folder = new File("/Inventarios-Refacciones");
        

        if (!folder.exists()) {
            folder.mkdir();
        }
        String routeXLSX = "C:\\Inventarios-Refacciones\\" + ("Inventario.xls");
                
        if (jbExportExcel.isEnabled()) {
            for (int i = 0 ; i < moTablePane.getTableModelRowCount(); i++) {
                int [] idItem = (int []) moTablePane.getTableRow(i).getPrimaryKey();
                
                stockExportExcel.add(new SDataStockExcel(
                        (String) moTablePane.getTableRow(i).getValues().elementAt(1), (String) moTablePane.getTableRow(i).getValues().elementAt(1),
                        (String) moTablePane.getTableRow(i).getValues().elementAt(5), (double) moTablePane.getTableRow(i).getValues().elementAt(2))); // Verificar el precio o quitarlo
            }

            Workbook workbook = new XSSFWorkbook();
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Existencias");
            //Formato de texto header
            Font headerFont = workbook.createFont();
            headerFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            //Formato color solido header celdas
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(headerCellStyle.SOLID_FOREGROUND);
            for (int i = 0; i < columns.length; i++) { 
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            int rowNum = 1;
            for (SDataStockExcel SDataStockExcel: stockExportExcel) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(SDataStockExcel.getClave());
                row.createCell(1).setCellValue(SDataStockExcel.getItem());
                row.createCell(2).setCellValue(SDataStockExcel.getUnidad());
                row.createCell(3).setCellValue(SDataStockExcel.getCosto());

            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            File file;
	    file = new File(routeXLSX);
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close(); 
            
            
        }
        
        miClient.showMsgBoxInformation("Excel de existencias generado exitosamente \nEl archivo se a guardado en: Disco local (C)->Inventarios-Refacciones/Existencias");        
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int year = 0;
        int[] key = null;
        Date date = null;
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                date = (Date) setting.getSetting();
                year = SLibTimeUtilities.digestYear(date)[0];
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_year = " + year + " AND " +
                        "s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(date) + "' ";
            }
        }
        
        msSql = "SELECT "
            + "(SELECT cost_u FROM trn_stk AS s WHERE id_item = i.id_item AND cost_u > 0 "
            + "ORDER BY s.dt DESC LIMIT 1) AS priceUnit , "
            + "i.id_item, s.id_unit, s.dt, i.item_key, i.item, u.symbol, i.part_num, "
            + "SUM(s.mov_in) AS _MovI, SUM(s.mov_out) AS _MovO "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON s.id_item = i.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON s.id_unit = u.id_unit WHERE  not s.b_del AND " + sqlWhere + " "
            + "GROUP BY s.id_item, s.id_unit, i.item_key, i.item, u.symbol "
            + "ORDER BY s.dt DESC, i.item, i.item_key, s.id_item, u.symbol, s.id_unit ";
    }
    
    public void actionExportTxt(SGuiClient client) throws FileNotFoundException, IOException, SQLException {
        ResultSet resultSet = null;
        BufferedWriter bw = null;
        String sql = "";
        String buffer = "";
        
        try {
            Calendar fecha = Calendar.getInstance();
            int anio = fecha.get(Calendar.YEAR);
            
            File folder = new File("/Inventarios-Refacciones");
            
            if (!folder.exists()) {
                folder.mkdir();
            }
            
            String routetxt = "C:\\Inventarios-Refacciones\\Inventario.txt";
            File file = new File(routetxt);
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            sql = ("SELECT i.item_key, i.part_num, i.item, u.symbol, "
                    + "s.id_cob, s.id_wh, cob.ent, cfg.qty_min, cfg.qty_max, "
                    + "(SELECT cost_u FROM trn_stk AS s WHERE id_item = i.id_item AND cost_u > 0 ORDER BY s.dt DESC LIMIT 1) AS priceUnit, "
                    + "SUM(s.mov_in) AS f_mov_i, SUM(s.mov_out) AS f_mov_o, SUM(s.mov_in - s.mov_out) AS f_stk "
                    + "FROM TRN_STK AS s "
                    + "INNER JOIN erp.ITMU_ITEM AS i ON s.id_item = i.id_item "
                    + "INNER JOIN erp.ITMU_UNIT AS u ON s.id_unit = u.id_unit "
                    + "INNER JOIN erp.CFGU_COB_ENT as cob ON s.id_cob = cob.id_cob AND s.id_wh = cob.id_ent "
                    + "INNER JOIN trn_stk_cfg AS cfg ON cfg.id_item = s.id_item AND cfg.id_unit = u.id_unit AND cfg.id_cob = s.id_cob AND cfg.id_wh = s.id_wh "
                    + "WHERE not s.b_del and cfg.id_wh = 19 and s.id_year = " + anio + " AND s.dt <= '" + anio + "-12-31' "
                    + "GROUP BY s.id_item, s.id_unit, i.item_key, i.item, u.symbol HAVING f_stk <> 0 "
                    + "ORDER BY s.dt DESC, i.item, i.item_key, s.id_item, u.symbol, s.id_unit ");
             
            resultSet = client.getSession().getStatement().executeQuery(sql);            
            
            buffer += "Identificador,No. de Parte,Descripción,Unidad,CostoUltimo,Existencias,Localización,Nivel Máximo,Nivel Mímino" + "\n";
            while(resultSet.next()){
                buffer += resultSet.getString("i.item_key");
                if (resultSet.getString("i.part_num").equals(null) || resultSet.getString("i.part_num").equals("")) buffer += "," + "0"; else buffer += "," + resultSet.getString("i.part_num");
                buffer += "," + resultSet.getString("i.item");
                buffer += "," + resultSet.getString("u.symbol");
                buffer += "," + resultSet.getDouble("priceUnit");
                buffer += "," + resultSet.getInt("f_stk");
                buffer += "," + resultSet.getString("cob.ent");
                buffer += "," + resultSet.getInt("cfg.qty_max");
                buffer += "," + resultSet.getInt("cfg.qty_min");
                buffer += "\n";
            }

                bw.write(buffer);
            bw.close();
            resultSet.close();
            miClient.showMsgBoxInformation("Archivo de texto de existencias generado exitosamente.\nEl archivo se a guardado en: Disco local C:\\Inventarios-Refacciones");    
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }        
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbExportExcel) {
                try {
                    actionExportExcel();
                } catch (IOException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (button == jbExportTxt) {
                try {
                    actionExportTxt((SGuiClient) miClient);
                } catch (IOException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}