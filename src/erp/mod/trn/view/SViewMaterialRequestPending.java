/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.form.SDialogMaterialRequestSupply;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Isabel Servín, Edwin Carmona
 */
public class SViewMaterialRequestPending extends SGridPaneView implements ActionListener {

    private JButton mjbSupply;
    private JButton mjbToNew;
    //private JButton mjbClose;
    //private JButton mjbOpen;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SDialogMaterialRequestSupply moDialogSupply;
    private boolean mbHasAdmRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_REV).HasRight;
    
    /**
     * @param client GUI client.
     * @param type
     * @param subtype
     * @param title View's GUI tab title.
     * @param params
     */
    public SViewMaterialRequestPending(SGuiClient client, int type, int subtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, type, subtype, title, params);
        initComponents();
    }
    
    private void initComponents() {
        mjbSupply = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_stk_out.gif")), "Suministrar", this);
        mjbToNew = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_left.gif")), "Regresar estatus nuevo", this);
//        mjbClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
//        mjbOpen = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));
//        mjbClose.setToolTipText("Cerrar para suministro");
//        mjbOpen.setToolTipText("Abrir para suministro");
//        mjbClose.setSize(23, 23);
//        mjbOpen.setSize(23, 23);
        mbHasAdmRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_ADMOR).HasRight;
        
        jbRowNew.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowCopy.setEnabled(false);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbSupply);
        //getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToNew);
        
        mjbSupply.setEnabled(mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP);
        mjbToNew.setEnabled(false);
        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            mjbToNew.setEnabled(true);
        }

        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED || mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            if (mnGridMode == SModSysConsts.TRNX_MAT_REQ_PUR) {
                moFilterDatePeriod.initFilter(null);
            }
            else {
                moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            }
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        moDialogSupply = new SDialogMaterialRequestSupply(miClient, "Surtidos de la requisición");
    }
    
    private void actionSupply() {
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
                    
                    moDialogSupply.setFormParams(key, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);
                    moDialogSupply.setVisible(true);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionToNew() {
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
                    if (miClient.showMsgBoxConfirm("¿Esta seguro/a de devolver la requisición a estatus de nuevo?\nEsta acción no se puede deshacer.") == JOptionPane.OK_OPTION) {
                        int[] key = (int[]) gridRow.getRowPrimaryKey();
                        String sql = "SELECT * FROM trn_diog_ety " 
                                + "WHERE fid_mat_req_n = " + key[0] + " "
                                + "AND NOT b_del;";
                        ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                        if (resultSet.next()) {
                            miClient.showMsgBoxInformation("No se puede devolver la requisición, ya tiene suministros.");
                        }
                        else {
                            SDbMaterialRequest req = new SDbMaterialRequest();
                            req.read(miClient.getSession(), key);
                            req.setFkMatRequestStatusId(SModSysConsts.TRNS_ST_MAT_REQ_NEW);
                            req.save(miClient.getSession());
                        }
                    }
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
        String subWhere = "";
        String join = "";
        String groupOrderBy = "";
        String subGroupOrderBy = "";
        String select = "";
        String having = "";
        Object filter;
        int usrId = miClient.getSession().getUser().getPkUserId();
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        if (moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD) != null) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            if (filter != null) {
                where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
            }
        }
        
        where += (where.isEmpty() ? "" : "AND ") + "(cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + 
                ", '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, NULL, NULL, NULL, NULL) = " +
                SAuthorizationUtils.AUTH_STATUS_AUTHORIZED + " " +
                "OR cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + 
                ", '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, NULL, NULL, NULL, NULL) = " +
                SAuthorizationUtils.AUTH_STATUS_NA + ") ";
        
        if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP) {
            if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_DETAIL) {
                select = "i.item, u.unit, ve.id_ety, "
                        + "SUM(ve.qty) AS org_qty, "
                        + "COALESCE(de.sumi_qty, 0) AS sumi_qty, "
                        + "COALESCE(SUM(ve.qty) - de.sumi_qty, SUM(ve.qty)) AS pen_sumi_qty, "
                        + "COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per, "
                        + "rpe.name AS ety_pty, "
                        + "ve.dt_req_n, ";
                join += "INNER JOIN erp.itmu_item AS i ON ve.fk_item = i.id_item "
                        + "INNER JOIN erp.itmu_unit AS u ON ve.fk_unit = u.id_unit "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rpe ON ve.fk_mat_req_pty_n = rpe.id_mat_req_pty ";
                groupOrderBy = "ve.id_mat_req, ve.id_ety ";
                subGroupOrderBy = "de.fid_mat_req_n, de.fid_mat_req_ety_n ";
                where += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                subWhere += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                //having = "HAVING per < 1 "; // Descomentar para mostrar unicamente los que faltan por suministrar
            }
            else if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED) {
                select = "COUNT(ve.id_ety) AS ety, " 
                        + "COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per_sumi, " 
                        + "1 - COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per_x_sumi, ";
                groupOrderBy = "v.id_mat_req, v.dt, v.num ";
                subGroupOrderBy = "de.fid_mat_req_n ";
                having = "HAVING per_sumi >= 1 OR v.b_clo_prov ";
            }
            else if (mnGridSubtype == SLibConsts.UNDEFINED) {
                select = "COUNT(ve.id_ety) AS ety, " 
                        + "COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per_sumi, " 
                        + "1 - COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per_x_sumi, ";
                where += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                subWhere += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                groupOrderBy = "v.id_mat_req, v.dt, v.num ";
                subGroupOrderBy = "de.fid_mat_req_n ";
                having = "HAVING per_sumi < 1 ";
            }
        }
        else if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR) {
            select = "SUM(ve.qty) AS org_qty, "
                    + "COALESCE(de.sumi_qty, 0) AS sumi_qty, "
                    + "COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per_sumi, " 
                    + "1 - COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per_x_sumi, "
                    + "COALESCE(req_pur.pur_qty, 0) / (SUM(ve.qty) - COALESCE(de.sumi_qty, 0)) AS per_pur, "
                    + "1 - COALESCE(req_pur.pur_qty, 0) / (SUM(ve.qty) - COALESCE(de.sumi_qty, 0)) AS per_x_pur, ";
            
            if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_DETAIL) {
                select += "i.item, u.unit, ve.id_ety, "
                        + "COALESCE(SUM(ve.qty) - de.sumi_qty, SUM(ve.qty)) AS pen_sumi_qty, "
                        + "COALESCE(de.sumi_qty, 0) / SUM(ve.qty) AS per, "
                        + "COALESCE(req_pur.pur_qty, 0) AS pur_qty,"
                        + "COALESCE(SUM(ve.qty) - COALESCE(de.sumi_qty, 0) - COALESCE(req_pur.pur_qty, 0), SUM(ve.qty) - COALESCE(de.sumi_qty, 0)) AS pen_pur_qty, "
                        + "rpe.name AS ety_pty, "
                        + "ve.dt_req_n, ";
                join += "INNER JOIN erp.itmu_item AS i ON ve.fk_item = i.id_item "
                        + "INNER JOIN erp.itmu_unit AS u ON ve.fk_unit = u.id_unit "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rpe ON ve.fk_mat_req_pty_n = rpe.id_mat_req_pty ";
                groupOrderBy = "ve.id_mat_req, ve.id_ety ";
                subGroupOrderBy = "de.fid_mat_req_n, de.fid_mat_req_ety_n ";
                subWhere += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                //having = "HAVING per < 1 "; // Descomentar para mostrar unicamente los que faltan por suministrar
            }
            else if (mnGridSubtype == SLibConsts.UNDEFINED) {
                select += "COUNT(ve.id_ety) AS ety, ";
                groupOrderBy = "v.id_mat_req, v.dt, v.num ";
                subGroupOrderBy = "de.fid_mat_req_n ";
                having = "HAVING per_sumi < 1 ";
            }
            
            where += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
        }
        if (usrId != 2 || !mbHasAdmRight) { // SUPER
            join += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu ON "  
                    +  "pe.id_mat_prov_ent = peu.id_mat_prov_ent ";
            where += (where.isEmpty() ? "" : "AND ") + "peu.id_usr = " + usrId + " ";
        }
        
        msSql = "SELECT v.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "LPAD(v.num, 6, 0) AS folio, "
                + "v.dt_req_n, "
                + "v.ref, "
                + "v.b_ext_sys, "
                + "v.b_clo_prov, "
                + select
                + "pe.name AS prov_ent, "
                + "rp.name AS req_pty, "
                + "smr.name AS status, "
                + "ur.usr AS usr_req, "
                + "bmu.bp AS contractor, "
                + "IF(ISNULL(entc.name), trn_get_cons_info(v.id_mat_req, 1), entc.name) AS ent_cons, "
                + "IF(ISNULL(sentc.name), trn_get_cons_info(v.id_mat_req, 2), sentc.name) AS s_ent_cons, "
                + "IF(ISNULL(fcc.id_cc), trn_get_cons_info(v.id_mat_req, 3), fcc.id_cc) AS f_cc, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_clo_prov, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_clo_prov, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS usr_clo, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS ve ON "
                + "v.id_mat_req = ve.id_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "v.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rp ON "
                + "v.fk_mat_req_pty = rp.id_mat_req_pty "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " AS smr ON "
                + "v.fk_st_mat_req = smr.id_st_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "v.fk_usr_req = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS mu ON "
                + "v.fk_contractor_n = mu.id_maint_user "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS entc ON "
                + "ve.fk_ent_mat_cons_ent_n = entc.id_mat_cons_ent "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS sentc ON "
                + "ve.fk_subent_mat_cons_ent_n = sentc.id_mat_cons_ent AND ve.fk_subent_mat_cons_subent_n = sentc.id_mat_cons_subent "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS fcc ON "
                + "ve.fk_cc_n = fcc.pk_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bmu ON "
                + "mu.id_maint_user = bmu.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "v.fk_usr_clo_prov = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT JOIN (SELECT de.fid_mat_req_n, de.fid_mat_req_ety_n, SUM(de.qty * IF(d.fid_ct_iog = 1, -1, 1)) AS sumi_qty "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v ON de.fid_mat_req_n = v.id_mat_req " 
                + "WHERE de.fid_mat_req_n IS NOT NULL AND de.fid_mat_req_ety_n IS NOT NULL " 
                + "AND NOT de.b_del AND NOT d.b_del " 
                + subWhere
                + "GROUP BY " + subGroupOrderBy + " " 
                + "ORDER BY " + subGroupOrderBy + " ) AS de ON " 
                + "ve.id_mat_req = de.fid_mat_req_n ";
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_DETAIL) {
            msSql += "AND ve.id_ety = de.fid_mat_req_ety_n ";
        }
        msSql += "LEFT JOIN (SELECT "
                + "ddmr.fid_mat_req, "
                + "ddmr.fid_mat_req_ety, "
                + "SUM(ddmr.qty * IF(1 = 1, 1, - 1)) AS pur_qty "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_MAT_REQ) + " AS ddmr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps ON "
                + "ddmr.fid_dps_year = dps.id_year AND ddmr.fid_dps_doc = dps.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dpsety ON "
                + "ddmr.fid_dps_year = dpsety.id_year AND ddmr.fid_dps_doc = dpsety.id_doc AND ddmr.fid_dps_ety = dpsety.id_ety "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON ddmr.fid_mat_req = mr.id_mat_req "
                + "WHERE "
                + "NOT dps.b_del AND NOT dpsety.b_del AND NOT mr.b_del "
                + "AND dps.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "AND dps.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "AND dps.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "\n #AND mr.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + " "
                + "\n #AND NOT mr.fk_usr_clo_pur "
                + "\n GROUP BY ddmr.fid_mat_req "
                + "ORDER BY ddmr.fid_mat_req) AS req_pur ON ve.id_mat_req = req_pur.fid_mat_req ";
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_DETAIL) {
            msSql += "AND ve.id_ety = req_pur.fid_mat_req_ety ";
        }
        msSql += join
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "GROUP BY " + groupOrderBy + " " 
                + having
                + "ORDER BY " + groupOrderBy ;
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "prov_ent", "Centro Suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio", "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_req", "Solicitante"));
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_DETAIL) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "id_ety", "Número partida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "ety_pty", "Prioridad partida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "ve.dt_req_n", "Fecha requerida partida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "item", "Ítem"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "org_qty", "Cant. requerida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "sumi_qty", "Cant. suministrada"));
            if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP) {
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "pen_sumi_qty", "Cant. pendiente suministrar"));
            }
            if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR) {
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "pur_qty", "Cant. comprada"));
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "pen_pur_qty", "Cant. pendiente comprar"));
            }
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "unit", "unidad"));
            if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP) {
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per", "% suministro"));
            }
        }
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED || mnGridSubtype == SLibConsts.UNDEFINED) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "req_pty", "Prioridad"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_req_n", "Fecha requerida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "contractor", "Contratista"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "ety", "Partidas"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_sumi", "% suministro"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_x_sumi", "% x suministrar"));
        }
        if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_pur", "% comprado"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_x_pur", "% x comprar"));
        }
        
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_DETAIL) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ent_cons", "Centro consumo"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "s_ent_cons", "Subcentro consumo"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cc", "Centro de costo"));
        }
        
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_clo_prov", "Cerrado suministro"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_clo", "Usr cerrado"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_clo_prov", "Usr TS cerrado"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        }
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRNU_MAT_REQ_PTY);
        moSuscriptionsSet.add(SModConsts.TRNS_ST_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_MAINT_USER);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
        moSuscriptionsSet.add(SModConsts.CFGU_AUTHORN_STEP);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG_ETY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbSupply) {
                actionSupply();
            }
            else if (button == mjbToNew) {
                actionToNew();
            }
        }
    }
}
