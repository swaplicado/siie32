/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnDiogComplement;
import erp.mtrn.data.STrnDpsUtilities;
import erp.mtrn.form.SDialogDpsFinder;
import erp.table.SFilterConstants;
import erp.table.STabFilterFunctionalArea;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridUtils;

/**
 *
 * @author Uriel Castañeda, Sergio Flores
 * 2017-03-08 (sflores): Reordering of command buttons and corresponding action methods.
 */
public class SViewOrderSuppliedWithMovs extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private JButton mjbImport;
    private JButton mjbSupply;
    private JButton mjbViewDps;
    private JButton mjbViewNotes;
    private JButton mjbViewLinks;
    private erp.lib.table.STabFilterDatePeriod moFilterDatePeriod;
    private erp.mtrn.form.SDialogDpsFinder moDialogDpsFinder;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    
    public SViewOrderSuppliedWithMovs(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_SUPPLIED_ORDER, auxType01);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        int i = 0;
        int columnNumber = 0;
        STableColumn[] aoTableColumns = null;
        
        mjbImport = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT), "Importar documento", this);
        mjbSupply = SGridUtils.createButton(new ImageIcon(getClass().getResource(isViewForPurchases() ? "/erp/img/icon_std_dps_supply.gif" : "/erp/img/icon_std_dps_supply.gif")), "Surtir", this);
        
        mjbViewDps = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LOOK), "Ver documento", this);
        mjbViewNotes = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_NOTES), "Ver notas", this);
        mjbViewLinks = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LINK), "Ver vínculos del documento", this);
        
        moFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moDialogDpsFinder = new SDialogDpsFinder((SClientInterface) miClient, SDataConstants.TRNX_DPS_PEND_LINK);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
        
        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(mjbImport);
        addTaskBarUpperComponent(mjbSupply);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
        
        STableField[] aoKeyFields = new STableField[2];
        
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "f_id_1");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "f_id_2");
        
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        aoTableColumns = new STableColumn[15];
        
        columnNumber = 0;
        
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dt_code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Referencia doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cb_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", isViewForPurchases() ? "Proveedor" : "Cliente", 200);
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave" + (isViewForPurchases() ? "Proveedor" : "Cliente"), 200);
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal" + (isViewForPurchases() ? "Proveedor" : "Cliente"), 200);
        aoTableColumns[columnNumber] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[columnNumber++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererCurrency());
        aoTableColumns[columnNumber++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda doc.", STableConstants.WIDTH_CODE_DOC);        
        aoTableColumns[columnNumber] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_orig_qty", "Cant. original", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[columnNumber++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[columnNumber] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_sup_orig_qty", "Cant. surtida", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[columnNumber++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[columnNumber] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_per_sup", "Surtido %", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[columnNumber++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[columnNumber] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_sup_orig_qty_pend", "Cant. pendiente", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[columnNumber++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[columnNumber] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_per_pend", "Pendiente %", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[columnNumber++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        
        for (columnNumber = 0; columnNumber < aoTableColumns.length; columnNumber++) {
            moTablePane.addTableColumn(aoTableColumns[columnNumber]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DPS);

        populateTable();               
    }
    
    private boolean isViewForPurchases() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }
    
    private void actionImport() {
        if (mjbImport.isEnabled()) {
            int[] keyOrder = isViewForPurchases() ?  SDataConstantsSys.TRNS_CL_DPS_PUR_ORD : SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;
            
            moDialogDpsFinder.formReset();
            moDialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, keyOrder);
            moDialogDpsFinder.setVisible(true);

            if (moDialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                int[] keyIog = isViewForPurchases() ? SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR : SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL;
                SDataDps dps = (SDataDps) moDialogDpsFinder.getValue(SDataConstants.TRN_DPS);
                
                if (STrnDpsUtilities.isDpsAuthorized(miClient, dps)) {
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).setFormComplement(new STrnDiogComplement(keyIog, dps));
                    if (((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                        ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                    }
                
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
                }
            }
        }
    }
    
    private void actionSupply() {
        if (mjbSupply.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int[] key = isViewForPurchases() ? SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR : SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                miClient.getGuiModule(SDataConstants.MOD_INV).setFormComplement(new STrnDiogComplement(key, dps));
                if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                }
            }
            
            miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
        }
    }
    
    private void actionViewDps() {
        if (mjbViewDps.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = isViewForPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                if (dps != null) {
                    miClient.getGuiModule(gui).setFormComplement(dps.getDpsTypeKey());
                    miClient.getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, dps.getPrimaryKey());
                }
            }
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
        }
    }

    private void actionViewLinks() {
        if (mjbViewLinks.isEnabled()) {
            SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
        }
    }
    
    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbImport) {
                actionImport();
            }
            else if (button == mjbSupply) {
                actionSupply();
            }
            else if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == mjbViewLinks) {
                actionViewLinks();
            }
           
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlBizPartner = "";
        String sqlOrderByDoc = "";
        String sqlDiogPeriod = "";
        String sqlFunctArea= "";
        STableSetting setting = null;
        
        for (STableSetting mvTableSetting : mvTableSettings) {
            setting = (erp.lib.table.STableSetting) mvTableSetting;
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {               
                sqlDiogPeriod += (sqlDiogPeriod.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "g.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlFunctArea = " AND d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }
        
        if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL) {
            // Sales & customers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " ";
            sqlOrderByDoc = "f_id_1,f_id_2,f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt, bp_key, bp, id_bp ";
        }
        else {
            // Purchases & suppliers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " ";
            sqlOrderByDoc = "f_id_1,f_id_2,bp_key, bp, id_bp, f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt ";
        }
        
        msSql = "SELECT " +
                "id_year AS " + SDbConsts.FIELD_ID + "1, " +
                "id_doc AS " + SDbConsts.FIELD_ID + "2, " +
                "'' AS " + SDbConsts.FIELD_CODE + ", " +
                "'' AS " + SDbConsts.FIELD_NAME + ", " +
                "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, " +
                "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, " +
                "SUM(f_qty) AS f_qty, " +
                "SUM(f_orig_qty) AS f_orig_qty, " +
                "COALESCE(SUM(f_sup_qty), 0) AS f_sup_qty, " +
                "COALESCE(SUM(f_sup_orig_qty), 0) AS f_sup_orig_qty, " +
                "((COALESCE(SUM(f_sup_orig_qty), 0) * 100 ) / SUM(f_orig_qty))/100 AS f_per_sup, " +
                "SUM(f_orig_qty)- COALESCE(SUM(f_sup_orig_qty), 0) AS f_sup_orig_qty_pend, " +
                "(((SUM(f_orig_qty) - COALESCE(SUM(f_sup_orig_qty), 0)) * 100 ) / SUM(f_orig_qty))/100 AS f_per_pend " +
                "FROM (SELECT de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "de.qty AS f_qty, " +
                "de.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ge.qty * CASE WHEN ge.fid_dps_adj_year_n IS NULL THEN 1 ELSE -1 END) FROM trn_diog_ety AS ge, trn_diog AS g WHERE " +
                "ge.fid_dps_year_n = de.id_year AND ge.fid_dps_doc_n = de.id_doc AND ge.fid_dps_ety_n = de.id_ety AND " +
                "ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND ge.b_del = 0 AND g.b_del = 0), 0) AS f_sup_qty, " +
                "COALESCE((SELECT SUM(ge.orig_qty * CASE WHEN ge.fid_dps_adj_year_n IS NULL THEN 1 ELSE -1 END) FROM trn_diog_ety AS ge, trn_diog AS g WHERE " +
                "ge.fid_dps_year_n = de.id_year AND ge.fid_dps_doc_n = de.id_doc AND ge.fid_dps_ety_n = de.id_ety AND " +
                "ge.id_year = g.id_year AND ge.id_doc = g.id_doc  AND ge.b_del = 0 AND g.b_del = 0), 0) AS f_sup_orig_qty " +
                "FROM trn_dps AS d " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp " + sqlBizPartner +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                "de.b_del = 0 AND de.b_inv = 1 AND de.qty > 0 AND de.orig_qty > 0 " +
                "INNER JOIN trn_diog_ety AS ge ON ge.fid_dps_year_n = de.id_year AND ge.fid_dps_doc_n = de.id_doc " +
                "INNER JOIN trn_diog AS g ON ge.id_year = g.id_year AND ge.id_doc = g.id_doc " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                "WHERE " + sqlDiogPeriod + " AND d.b_close = 0 " + sqlFunctArea + " AND " +
                "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                "d.fid_ct_dps = " + (isViewForPurchases() ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0]) + " AND " +
                "d.fid_cl_dps = " + (isViewForPurchases() ?  SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1]) + " AND " + 
                "d.fid_tp_dps = " + (isViewForPurchases() ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2]) + " " +  
                "GROUP BY de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "de.qty, de.orig_qty) AS DPS_ETY_TMP " + 
                "GROUP BY id_year, id_doc, dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, " +
                "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
                "ORDER BY " + sqlOrderByDoc + "; ";
    }

    @Override
    public void actionNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionEdit() {
       actionViewDps();
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
