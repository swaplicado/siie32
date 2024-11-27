/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import erp.mtrn.form.SDialogStockCardex;
import erp.mtrn.form.SDialogStockSegregations;
import static erp.mtrn.view.SViewStock.TXT_DEC_DEC;
import static erp.mtrn.view.SViewStock.TXT_DEC_INC;
import erp.table.STabFilterCompanyBranchEntity;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
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
 * @author Claudio Peña, Isabel Servín, Sergio Flores
 * 2024-11-26 Sergio Flores:
 * Esta vista ya es obsoleta. Se usaba para generar archivos para exportar existencias para MP Software.
 */
public class SViewStockCostUnit extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private static final String[] COLUMNS = { "Número de parte", "Ítem", "Unidad", "Costo unitario" };
    private static final List<SDataStockExcel> stockExportExcel = new ArrayList<>();
    
    private int mnColIn;
    private int mnColOut;
    private int mnColStk;
    private javax.swing.JButton jbCardex;
    private javax.swing.JButton jbSegregations;
    private javax.swing.JButton jbExportExcel;
    private javax.swing.JButton jbExportTxt;
    private javax.swing.JToggleButton jtbDecimals;
    private erp.lib.table.STabFilterDate moTabFilterDate;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    
    private erp.mtrn.form.SDialogStockCardex moDialogStockCardex;
    private erp.mtrn.form.SDialogStockSegregations moDialogStockSegregations;

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
        moTabFilterDeleted = new STabFilterDeleted(this, "Filtrar ítems sin existencias");
        moDialogStockCardex = new SDialogStockCardex(miClient);
        moDialogStockSegregations = new SDialogStockSegregations(miClient);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);
        
        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbCardex.addActionListener(this);
        addTaskBarUpperComponent(jbCardex);

        jbSegregations = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")));
        jbSegregations.setPreferredSize(new Dimension(23, 23));
        jbSegregations.setToolTipText("Ver unidades segregadas");
        jbSegregations.addActionListener(this);
        addTaskBarUpperComponent(jbSegregations);
        addTaskBarUpperSeparator();        
        addTaskBarUpperComponent(moTabFilterDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();
        
        jtbDecimals = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_inc.gif")));
        jtbDecimals.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_dec.gif")));
        jtbDecimals.setPreferredSize(new Dimension(23, 23));
        jtbDecimals.setToolTipText(TXT_DEC_INC);
        jtbDecimals.addActionListener(this);
        addTaskBarUpperComponent(jtbDecimals);
        addTaskBarUpperSeparator();
        
        jbExportExcel = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_TYPE));
        jbExportExcel.setPreferredSize(new Dimension(23, 23));
        jbExportExcel.setToolTipText("Exportar a archivo de Excel (*.xls)");
        jbExportExcel.addActionListener(this);
        addTaskBarUpperComponent(jbExportExcel);
        
        jbExportTxt = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_TYPE));
        jbExportTxt.setPreferredSize(new Dimension(23, 23));
        jbExportTxt.setToolTipText("Exportar a archivo de texto (*.txt)");
        jbExportTxt.addActionListener(this);
        addTaskBarUpperComponent(jbExportTxt);
                
        i = 0;
        aoKeyFields = new STableField[2];
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "i.id_item");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
        
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;
        
        switch (mnTabTypeAux01) {
            case SDataConstants.TRNX_STK_ITEM:
                aoTableColumns = new STableColumn[8];
                break;
            default:
                aoTableColumns = null;
        }
        
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", 100);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.part_num", "Número parte", 75);
        
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "priceUnit", "Valor promedio", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        
        mnColIn = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_mov_in", "Entradas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        
        mnColOut = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_mov_out", "Salidas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        
        mnColStk = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_stock", "Existencias", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        //aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "s.dt", "Ultima fecha compra", STableConstants.WIDTH_DATE);
        
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
                        (String) moTablePane.getTableRow(i).getValues().elementAt(0), (String) moTablePane.getTableRow(i).getValues().elementAt(1),
                        (String) moTablePane.getTableRow(i).getValues().elementAt(6), (double) moTablePane.getTableRow(i).getValues().elementAt(2))); // Verificar el precio o quitarlo
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
            for (int i = 0; i < COLUMNS.length; i++) { 
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNS[i]);
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

            for (int i = 0; i < COLUMNS.length; i++) {
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
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int year = 0;
        int[] key = null;
        Date date = null;
        String sqlWhere = "";
        String sqlHaving = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlHaving = "HAVING _stk <> 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                date = (Date) setting.getSetting();
                year = SLibTimeUtilities.digestYear(date)[0];
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_year = " + year + " AND " +
                        "s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(date) + "' ";
            }
        }
        
        msSql = "SELECT "
                + "AVG(s.cost_u) AS priceUnit, "
                + "i.id_item, s.id_unit, s.dt, i.item_key, i.item, u.symbol, i.part_num, "
                + "SUM(s.mov_in) AS _mov_in, SUM(s.mov_out) AS _mov_out, SUM(s.mov_in - s.mov_out) AS _stk "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON s.id_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON s.id_unit = u.id_unit "
                + "WHERE NOT s.b_del AND " + sqlWhere + " "
                + "GROUP BY s.id_item, s.id_unit, i.item_key, i.item, u.symbol "
                + sqlHaving
                + "ORDER BY s.dt DESC, i.item, i.item_key, s.id_item, u.symbol, s.id_unit ";
    }
    
    public void actionCardex() {
        int mode = jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        if (jbCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] key = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int[] whKey = showWarehouses() ? new int[] { key[key.length - 2], key[key.length - 1] } : moTabFilterCompanyBranchEntity.getCompanyBranchEntityKey();
                int itemId = key[0];
                int unitId = key[1];
                int lotId = showLots() ? key[2] : SLibConstants.UNDEFINED;

                moDialogStockCardex.formReset();
                moDialogStockCardex.setFormParams(moTabFilterDate.getDate(), itemId, unitId, lotId, whKey, mode);
                moDialogStockCardex.setVisible(true);
           }
        }
    }
    
    public void actionSegregations() {
        int mode = jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        if (jbSegregations.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] key = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int[] whKey = showWarehouses() ? new int[] { key[key.length - 2], key[key.length - 1] } : moTabFilterCompanyBranchEntity.getCompanyBranchEntityKey();
                int itemId = key[0];
                int unitId = key[1];

                moDialogStockSegregations.formReset();
                moDialogStockSegregations.setFormParams(moTabFilterDate.getDate(), itemId, unitId, whKey, mode);
                moDialogStockSegregations.setVisible(true);
            }
        }
    }
    
    public void actionDecimals() {
        String toolTipText = !jtbDecimals.isSelected() ? TXT_DEC_INC : TXT_DEC_DEC;
        
        DefaultTableCellRenderer tcr = !jtbDecimals.isSelected() ?
            miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity() :
            miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary();

        moTablePane.getTableColumn(mnColIn).setCellRenderer(tcr);
        moTablePane.getTableColumn(mnColOut).setCellRenderer(tcr);
        moTablePane.getTableColumn(mnColStk).setCellRenderer(tcr);

        jtbDecimals.setToolTipText(toolTipText);

        actionRefresh(STableConstants.REFRESH_MODE_RELOAD);
    }
    
     private boolean showWarehouses() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_WH || mnTabTypeAux01 == SDataConstants.TRNX_STK_LOT_WH;
    }

    private boolean showLots() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_LOT || mnTabTypeAux01 == SDataConstants.TRNX_STK_LOT_WH;
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbExportExcel) {
                try {
                    actionExportExcel();
                }
                catch (IOException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (SQLException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (button == jbExportTxt) {
                try {
                    actionExportTxt((SGuiClient) miClient);
                }
                catch (IOException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (SQLException ex) {
                    Logger.getLogger(SViewStockCostUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (button == jbCardex) {
                actionCardex();
            }
            else if (button == jbSegregations) {
                actionSegregations();
            }
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbDecimals) {
                actionDecimals();
            }
        }
    }
}