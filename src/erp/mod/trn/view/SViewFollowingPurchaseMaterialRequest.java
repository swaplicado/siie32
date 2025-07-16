/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbSupplierFile;
import erp.mod.trn.db.SMaterialRequestUtils;
import erp.mod.trn.form.SDialogMaterialRequestDocsCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewFollowingPurchaseMaterialRequest extends SGridPaneView implements ActionListener {
    
    private static final int ROW_QUOT = 14; 
    private static final int ROW_AMN_QUOT_P = 15; 
    private static final int ROW_AMN_QUOT_O = 16; 
    private static final int ROW_DIFF = 17; 
    private static final int ROW_DIFF_ICON = 19; 
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private JButton jbPrint;
    private JButton jbDocsCardex;
    private JButton mjbCloseOpenPurchase;
    
    private SDialogMaterialRequestDocsCardex moDialogDocsCardex;

    /**
     * Create new SViewFollowingPurchaseMaterialRequest view.
     * @param client GUI client.
     * @param gridSubtype SModSysConsts.TRNX_MAT_REQ_FOLL_PUR_CLOSED (closed MR) or SLibConstants.UNDEFINED (open MR).
     * @param title View tab label.
     */
    public SViewFollowingPurchaseMaterialRequest(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_MAT_REQ_FOLL_PUR, gridSubtype, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        jtbFilterDeleted.setEnabled(false);
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir", this);
        jbDocsCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_type.gif")), "Ver documentos relacionados de la requisición", this);
        mjbCloseOpenPurchase = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_con.gif")), "Cerrar/abrir compras", this);        
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocsCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCloseOpenPurchase);
        
        moDialogDocsCardex = new SDialogMaterialRequestDocsCardex(miClient, "Documentos relacionados de la requisición");
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        if (mnGridSubtype == SUtilConsts.ACTION_CLOSE) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
    }
    
    private void actionPrint() {
        if (jbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        print(gridRow.getRowPrimaryKey()[0]);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    private void print(int idMatReq) throws Exception {
        HashMap<String, Object> params;
        
        params = miClient.createReportParams();
        params.put("nMatReqId", idMatReq);
        
        miClient.getSession().printReport(SModConsts.TRN_MAT_REQ, SLibConsts.UNDEFINED, null, params);
    }
    
    private void actionDocsKardex() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (gridRow.isRowSystem()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                try {
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    
                    moDialogDocsCardex.setFormParams(key[0], 0);
                    moDialogDocsCardex.setVisible(true);
                    
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionCloseOpenToPurchase() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (gridRow.isRowSystem()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                String message = SMaterialRequestUtils.openOrCloseToPurchase(miClient.getSession(), key);
                if (!message.isEmpty()) {
                    miClient.showMsgBoxError(message);
                }

                miClient.getSession().notifySuscriptors(mnGridType);
            }
        }
    }
    
    @Override
    public void computeGridData() {
        moModel.getGridRows().forEach((row) -> {
            if (((double) row.getRowValueAt(ROW_AMN_QUOT_O)) != 0.0) {
                double dif = (double) row.getRowValueAt(ROW_AMN_QUOT_O) - (double) row.getRowValueAt(ROW_AMN_QUOT_P);
                row.setRowValueAt(dif, ROW_DIFF);
                if ((dif < 0 && ((String) row.getRowValueAt(ROW_QUOT)).equals(SDbSupplierFile.PRICE_LOWER)) ||
                        (dif > 0 && ((String) row.getRowValueAt(ROW_QUOT)).equals(SDbSupplierFile.PRICE_HIGHER))) {
                    row.setRowValueAt(SGridConsts.ICON_WARN, ROW_DIFF_ICON);
                }
            }
        });
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        String having = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        moPaneSettings.setUserApplying(false);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setSystemApplying(false);
        
        if (mnGridSubtype == SUtilConsts.ACTION_CLOSE) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            where += "AND " + SGridUtils.getSqlFilterDate("r.dt", (SGuiDate) filter);
            having += "(r.b_clo_pur OR per_pur = 1) ";
        }
        else {
            where += "AND NOT r.b_clo_pur ";
            having += "per_pur < 1 OR per_pur IS NULL ";
        }
        
        msSql = "SELECT "
                //+ "#REQUISICIÓN\n" //preservar para depuración de la consulta
                + "re.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "re.id_ety AS " + SDbConsts.FIELD_ID + "2, "
                + "pe.name AS _wah_name, "
                + "LPAD(r.num, " + SDataConstantsSys.NUM_LEN_MAT_REQ + ", 0) AS " + SDbConsts.FIELD_CODE + ", "
                + "r.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "ur.usr AS _user, "
                + "'' AS f_name, "
                + "re.dt_req_n AS _req_date, "
                + "mp.name AS _pty_req, "
                + "ireq.item AS _item_req, "
                + "ir.item AS _item_aux_req, "
                + "r.b_clo_pur, "
                //+ "#LOG DE CAMBIOS DE ESTADO\n" //preservar para depuración de la consulta
                + "MIN(slp.ts_usr) AS _first_prov_date, "
                + "MAX(slp.ts_usr) AS _last_prov_date, "
                + "MIN(sls.ts_usr) AS _first_pur_date, "
                + "MAX(sls.ts_usr) AS _last_pur_date, "
                //+ "#SOLICITUD DE COTIZACIÓN\n" //preservar para depuración de la consulta
                + "MIN(er.ts_usr) AS _first_quot_date, "
                + "uc.usr, "
                //+ "#SOPORTES\n" //preservar para depuración de la consulta
                + "IF(sup.quot = 0 OR sup.quot IS NULL, '', "
                + "IF(sup.expe = 1 AND sup.chea = 1, '" + SDbSupplierFile.PRICE_INTER + "', "
                + "IF(sup.expe = 0 AND sup.chea = 0, '" + SDbSupplierFile.PRICE_UNIQUE + "', "
                + "IF(sup.expe = 1, '" + SDbSupplierFile.PRICE_LOWER + "', '" + SDbSupplierFile.PRICE_HIGHER + "')))) AS _quot, "
                + "IF(sup.quot = 0 OR sup.quot IS NULL, 0, sup.quot_p) AS _quot_p, "
                + "IF(sup.expe = 0 OR sup.expe IS NULL, sup.chea_p, sup.expe_p) AS _quot_o, "
                + "'MXN' AS _local_cur_quot, "
                + "0 AS diff, "
                + "" + SGridConsts.ICON_NULL + " AS icon, "
                //+ "#PEDIDO\n" //preservar para depuración de la consulta
                + "IF(dp.dt IS NULL, NULL, 'PED') AS _ord_code, "
                + "dp.num AS _ord_num, "
                + "dp.ts_new AS _ord_date, "
                + "irp.item AS _ord_item_aux, "
                + "dp.dt_doc_delivery_n AS _ord_deli_date, "
                + "bp.bp, "
                + "ip.item AS _item, "
                + "dpe.qty, "
                + "up.symbol, "
                //+ "#%PEDIDO\n" //preservar para depuración de la consulta
                + "IF(COALESCE(SUM(req_pur.pur_qty), 0) > (SUM(re.qty) - COALESCE(SUM(de.sumi_qty), 0)), (SUM(re.qty) - COALESCE(SUM(de.sumi_qty), 0)), "
                + "COALESCE(SUM(req_pur.pur_qty), 0)) / (SUM(re.qty) - COALESCE(SUM(de.sumi_qty), 0)) AS per_pur, "
                //+ "#AUTORIZACIÓN\n" //preservar para depuración de la consulta
                + "MIN(dpa.ts_new) AS _first_auth_date, "
                + "MAX(dpa.ts_new) AS _last_auth_date, "
                + "IF(dp.b_authorn, dp.ts_authorn, NULL) AS _ord_auth, "
                //+ "#FACTURA\n" //preservar para depuración de la consulta
                + "IF(df.dt IS NULL, NULL, 'FAC') AS _inv_code, "
                + "df.num AS _inv_num, "
                + "df.ts_new AS _inv_date, "
                + "df.stot_r AS _stot, "
                + "c.cur_key AS _cur, "
                + "df.stot_cur_r AS _local_stot, "
                + "IF(df.stot_cur_r IS NULL, NULL, 'MXN') AS _local_cur "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS r "
                //+ "#REQUISICIÓN\n" //preservar para depuración de la consulta
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS re ON "
                + "r.id_mat_req = re.id_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "r.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ireq ON "
                + "re.fk_item = ireq.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "r.fk_usr_req = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS mp ON "
                + "re.fk_mat_req_pty_n = mp.id_mat_req_pty "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ir ON "
                + "re.fk_item_ref_n = ir.id_item "
                //+ "#LOG DE CAMBIOS DE ESTADO\n" //preservar para depuración de la consulta
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ST_LOG) + " AS slp ON "
                + "r.id_mat_req = slp.id_mat_req AND slp.fk_st_mat_req = 3 "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ST_LOG) + " AS sls ON "
                + "r.id_mat_req = sls.id_mat_req AND sls.fk_st_mat_req = 4 "
                //+ "#DPS PEDIDO Y FACTURA\n" //preservar para depuración de la consulta
                + "LEFT JOIN("
                + "  SELECT dr.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dr "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "  dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc AND NOT d.b_del AND "
                + "  d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " AND d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + ") "
                + "AS ped ON re.id_mat_req = ped.fid_mat_req AND re.id_ety = ped.fid_mat_req_ety "
                + "LEFT JOIN("
                + "  SELECT dr.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dr "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "  dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc AND NOT d.b_del AND "
                + "  d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " AND d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + ") "
                + "AS fac ON re.id_mat_req = fac.fid_mat_req AND re.id_ety = fac.fid_mat_req_ety "
                //+ "#PEDIDO\n" //preservar para depuración de la consulta
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dp ON "
                + "ped.fid_dps_year = dp.id_year AND ped.fid_dps_doc = dp.id_doc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dpe ON "
                + "ped.fid_dps_year = dpe.id_year AND ped.fid_dps_doc = dpe.id_doc AND ped.fid_dps_ety = dpe.id_ety "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS irp ON "
                + "dpe.fid_item_ref_n = irp.id_item "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ip ON "
                + "dpe.fid_item = ip.id_item "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS up ON "
                + "dpe.fid_unit = up.id_unit "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "dp.fid_bp_r = bp.id_bp "
                //+ "#% SUMINISTRADO\n" //preservar para depuración de la consulta
                + "LEFT JOIN("
                + "  SELECT de.fid_mat_req_n, de.fid_mat_req_ety_n, SUM(de.qty * IF(d.fid_ct_iog = 1, -1, 1)) AS sumi_qty "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON "
                + "  d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v ON "
                + "  de.fid_mat_req_n = v.id_mat_req "
                + "  WHERE de.fid_mat_req_n IS NOT NULL AND de.fid_mat_req_ety_n IS NOT NULL AND NOT de.b_del AND NOT d.b_del "
                + "  AND d.fid_ct_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_DEV_CONS[0] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[0] + ") "
                + "  AND d.fid_cl_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_DEV_CONS[1] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[1] + ") "
                + "  AND d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_DEV_CONS[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[2] + ") "
                + "  GROUP BY de.fid_mat_req_n, de.fid_mat_req_ety_n "
                + "  ORDER BY de.fid_mat_req_n, de.fid_mat_req_ety_n ) "
                + "AS de ON re.id_mat_req = de.fid_mat_req_n AND re.id_ety = de.fid_mat_req_ety_n "
                //+ "#% PEDIDO\n" //preservar para depuración de la consulta
                + "LEFT JOIN("
                + "  SELECT ddmr.fid_mat_req, ddmr.fid_mat_req_ety, SUM(ddmr.qty) AS pur_qty "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS ddmr "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dpsety ON "
                + "  ddmr.fid_dps_year = dpsety.id_year AND ddmr.fid_dps_doc = dpsety.id_doc AND ddmr.fid_dps_ety = dpsety.id_ety "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps ON "
                + "  dpsety.id_year = dps.id_year AND dpsety.id_doc = dps.id_doc "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS mre ON "
                + "  ddmr.fid_mat_req = mre.id_mat_req AND ddmr.fid_mat_req_ety = mre.id_ety "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON "
                + "  mre.id_mat_req = mr.id_mat_req "
                + "  WHERE NOT dps.b_del AND NOT dpsety.b_del AND NOT mr.b_del "
                + "  AND dps.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "  AND dps.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "  AND dps.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "  GROUP BY ddmr.fid_mat_req ORDER BY ddmr.fid_mat_req) "
                + "AS req_pur ON re.id_mat_req = req_pur.fid_mat_req AND re.id_ety = req_pur.fid_mat_req_ety "
                //+ "#SOPORTES\n" //preservar para depuración de la consulta
                + "LEFT JOIN("
                + "  SELECT id_year, id_doc, "
                + "  COUNT(DISTINCT CASE WHEN sup_file_dps_type = '" + SDbSupplierFile.QUA + "' THEN '" + SDbSupplierFile.QUA + "' END) AS quot,"
                + "  COUNT(DISTINCT CASE WHEN sup_file_dps_type = '" + SDbSupplierFile.QUA_EXP + "' THEN '" + SDbSupplierFile.QUA_EXP + "' END) AS expe,"
                + "  COUNT(DISTINCT CASE WHEN sup_file_dps_type = '" + SDbSupplierFile.QUA_CHE + "' THEN '" + SDbSupplierFile.QUA_CHE + "' END) AS chea, "
                + "  MAX(IF(sup_file_dps_type = '" + SDbSupplierFile.QUA + "', tot_loc_r, 0)) AS quot_p, "
                + "  MAX(IF(sup_file_dps_type = '" + SDbSupplierFile.QUA_EXP + "', tot_loc_r, 0)) AS expe_p, "
                + "  MAX(IF(sup_file_dps_type = '" + SDbSupplierFile.QUA_CHE + "', tot_loc_r, 0)) AS chea_p "
                + "  FROM trn_sup_file_dps GROUP BY id_year, id_doc) "
                + "AS sup ON dp.id_year = sup.id_year AND dp.id_doc = sup.id_doc "
                //+ "#ENVÍO AUTORIZACIÓN\n" //preservar para depuración de la consulta
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_AUTHORN) + " AS dpa ON "
                + "dp.id_year = dpa.id_year AND dp.id_doc = dpa.id_doc "
                //+ "#SOLICUTUD DE COTIZACIÓN\n" //preservar para depuración de la consulta
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ) + " AS er ON "
                + "re.id_mat_req = er.fk_mat_req_n "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_ETY) + " AS ere ON "
                + "re.id_mat_req = ere.fk_mat_req_n AND re.id_ety = ere.fk_mat_req_ety_n "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_REC) + " AS err ON "
                + "er.id_est_req = err.id_est_req " //"#AND dp.fid_bp_r = err.fk_bp_n " //descomentar para mostrar sólo la solicitud de cotización elegida
                + "LEFT JOIN erp.usru_usr AS uc ON er.fk_usr = uc.id_usr "
                //+ "#FACTURA\n" //preservar para depuración de la consulta
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS df ON "
                + "fac.fid_dps_year = df.id_year AND fac.fid_dps_doc = df.id_doc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dfe ON "
                + "fac.fid_dps_year = dfe.id_year AND fac.fid_dps_doc = dfe.id_doc AND fac.fid_dps_ety = dfe.id_ety "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON "
                + "df.fid_cur = c.id_cur "
                + "WHERE NOT r.b_del AND NOT re.b_del "
                + "AND sls.ts_usr IS NOT NULL " + where
                + "GROUP BY re.id_mat_req, re.id_ety, pe.name, r.num, r.dt, ur.usr, re.dt_req_n, mp.name "
                + "HAVING "
                + having + ";";
        }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "_wah_name", "Cto suministro requisición")); //0
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_CODE, "Folio requisición"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha requisición"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_user", "Solicitante requisición"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_req_date", "Fecha requerida requisición"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_pty_req", "Prioridad requisición"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "_item_req", "Ítem requisición"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "_item_aux_req", "Concepto/gasto requisición"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_prov_date", "Primera vez requisición en almacén"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_last_prov_date", "Última vez requisición en almacén"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_pur_date", "Primera vez requisición en compras")); //10
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_last_pur_date", "Última vez requisición en compras"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_quot_date", "Primera cot"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr", "Cotizador"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_quot", "Tipo cot elegida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_quot_p", "Total cot elegida $")); //15
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_quot_o", "Total cot descartada $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "diff", "Dif total descartado - elegido $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_local_cur_quot", "Moneda local cot"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "icon", "Discrepancia tipo cot elegida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "_ord_code", "Tipo doc ped")); //20
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_ord_num", "Folio ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_ord_date", "Fecha ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "_ord_item_aux", "Concepto/gasto ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_ord_deli_date", "Fecha entrega ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Proveedor ped")); //25
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "_item", "Ítem ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "qty", "Cantidad ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "symbol", "Unidad ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_auth_date", "Primer envío ped a autorización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_last_auth_date", "Último envío ped a autorización")); //30
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_ord_auth", "Fecha autorización ped"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "_inv_code", "Tipo doc fac"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_inv_num", "Folio fac"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_inv_date", "Fecha fac"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_stot", "Total fac mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_cur", "Moneda fac"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_local_stot", "Total fac $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_local_cur", "Moneda local fac")); //39
       
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ_ST_LOG);
        moSuscriptionsSet.add(SModConsts.TRN_EST_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_EST_REQ_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_EST_REQ_REC);
        moSuscriptionsSet.add(SModConsts.TRN_DPS_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.TRN_DPS_ETY);
        moSuscriptionsSet.add(SModConsts.TRNU_MAT_REQ_PTY);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbDocsCardex) {
                actionDocsKardex();
            }
            else if (button == mjbCloseOpenPurchase) {
                actionCloseOpenToPurchase();
            }
        }
    }
}
