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
import erp.mod.trn.form.SDialogMaterialRequestDocsCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private JButton jbPrint;
    private JButton jbDocsCardex;
    
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
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir", this);
        jbDocsCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_type.gif")), "Ver documentos relacionados de la RM", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocsCardex);
        
        moDialogDocsCardex = new SDialogMaterialRequestDocsCardex(miClient, "Documentos relacionados de la RM");
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_FOLL_PUR_CLOSED) {
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
                    
                    moDialogDocsCardex.setFormParams(key[0]);
                    moDialogDocsCardex.setVisible(true);
                    
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        moPaneSettings.setUserApplying(false);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setSystemApplying(false);
        
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_FOLL_PUR_CLOSED) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            where += "r.b_clo_pur AND " + SGridUtils.getSqlFilterDate("r.dt", (SGuiDate) filter);
        }
        else {
            where += "NOT r.b_clo_pur ";
        }
        
        msSql = "SELECT "
                //+ "#REQUISICIÓN\n" //preservar para depuración de la consulta
                + "re.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "re.id_ety AS " + SDbConsts.FIELD_ID + "2, "
                + "pe.name AS _wah_name, "
                + "LPAD(r.num, " + SDataConstantsSys.NUM_LEN_MAT_REQ + ", 0) AS " + SDbConsts.FIELD_CODE + ", "
                + "r.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "ur.usr AS _user, "
                + "re.dt_req_n AS _req_date, "
                + "mp.name AS _pty_req, "
                + "ireq.item AS _item_req, "
                + "ir.item AS _item_aux_req, "
                + "MIN(slp.ts_usr) AS _first_prov_date, "
                + "MAX(slp.ts_usr) AS _last_prov_date, "
                + "MIN(sls.ts_usr) AS _first_pur_date, "
                + "MAX(sls.ts_usr) AS _last_pur_date, "
                //+ "#SOLICITUD DE COTIZACIÓN\n" //preservar para depuración de la consulta
                + "MIN(er.ts_usr) AS _first_quot_date, "
                //+ "#SOPORTES\n" //preservar para depuración de la consulta
                + "IF(sup.quot = 0 OR sup.quot IS NULL, '', "
                + "IF(sup.expe = 1 AND sup.chea = 1, '" + SDbSupplierFile.PRICE_INTER + "', "
                + "IF(sup.expe = 1, '" + SDbSupplierFile.PRICE_LOWER + "', '" + SDbSupplierFile.PRICE_HIGHER + "'))) AS _quot, "
                //+ "#PEDIDO\n" //preservar para depuración de la consulta
                + "IF(dp.dt IS NULL, NULL, 'PED') AS _ord_code, "
                + "dp.num AS _ord_num, "
                + "dp.ts_new AS _ord_date, "
                + "irp.item AS _ord_item_aux, "
                + "dp.dt_doc_delivery_n AS _ord_deli_date, "
                + "bp.bp, "
                + "ip.item AS _item, "
                + "dpe._qty, "
                + "up._symbol, "
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
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ST_LOG) + " AS slp ON "
                + "r.id_mat_req = slp.id_mat_req AND slp.fk_st_mat_req = 3 "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ST_LOG) + " AS sls ON "
                + "r.id_mat_req = sls.id_mat_req AND sls.fk_st_mat_req = 4 "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS mp ON "
                + "re.fk_mat__req_pty_n = mp.id_mat__req_pty "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ir ON "
                + "re.fk_item_ref_n = ir.id_item "
                //+ "#DPS PEDIDO Y FACTURA\n" //preservar para depuración de la consulta
                + "LEFT JOIN (SELECT dr.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dr "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "  dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc AND NOT d.b_del AND"
                + "  d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " AND d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + ")"
                + "AS ped ON re.id_mat_req = ped.fid_mat_req AND re.id_ety = ped.fid_mat_req_ety "
                + "LEFT JOIN (SELECT dr.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dr "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "  dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc AND NOT d.b_del AND"
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
                //+ "#SOPORTES\n" //preservar para depuración de la consulta
                + "LEFT JOIN (SELECT id_year, id_doc,"
                + "  COUNT(DISTINCT CASE WHEN sup_file_dps_type = '" + SDbSupplierFile.QUA + "' THEN '" + SDbSupplierFile.QUA + "' END) AS quot,"
                + "  COUNT(DISTINCT CASE WHEN sup_file_dps_type = '" + SDbSupplierFile.QUA_EXP + "' THEN '" + SDbSupplierFile.QUA_EXP + "' END) AS expe,"
                + "  COUNT(DISTINCT CASE WHEN sup_file_dps_type = '" + SDbSupplierFile.QUA_CHE + "' THEN '" + SDbSupplierFile.QUA_CHE + "' END) AS chea "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_SUP_FILE_DPS) + " "
                + "GROUP BY id_year, id_doc) AS sup ON dp.id_year = sup.id_year AND dp.id_doc = sup.id_doc "
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
                //+ "#FACTURA\n" //preservar para depuración de la consulta
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS df ON "
                + "fac.fid_dps_year = df.id_year AND fac.fid_dps_doc = df.id_doc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dfe ON "
                + "fac.fid_dps_year = dfe.id_year AND fac.fid_dps_doc = dfe.id_doc AND fac.fid_dps_ety = dfe.id_ety "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON "
                + "df.fid_cur = c.id_cur "
                + "WHERE " + where + " AND NOT r.b_del AND NOT re.b_del "
                + "GROUP BY re.id_mat_req, re.id_ety, pe.name, r.num, r.dt, ur.usr, re.dt_req_n, mp.name;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "_wah_name", "Cen suministro"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_CODE, "Folio RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_user", "Solicitante"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_req_date", "Fecha requerida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_pty_req", "Prioridad RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "_item_req", "Ítem RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "_item_aux_req", "Concepto/gasto RM"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_prov_date", "Pri fecha/hr almacén"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_last_prov_date", "Últ fecha/hr almacén"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_pur_date", "Pri fecha/hr compras"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_last_pur_date", "Últ fecha/hr compras"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_quot_date", "Pri fecha/hr cotización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_quot", "Cotización elegida"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "_ord_code", "Tipo doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_ord_num", "Folio pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_ord_date", "Creación pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "_ord_item_aux", "Concepto/gasto pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_ord_deli_date", "Fecha entrega pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Asociado negocios"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "_item", "Item pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_qty", "Cantidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "_symbol", "Unidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_first_auth_date", "Pri fecha envío autorización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_last_auth_date", "Ult fecha envío autorización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_ord_auth", "Fecha autorización pedido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CO, "_inv_code", "Tipo doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_inv_num", "Folio factura"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "_inv_date", "Creación factura"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_stot", "Monto mon $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_cur", "Moneda"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_local_stot", "Monto $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_local_cur", "Moneda local"));
       
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
        }
    }
}
