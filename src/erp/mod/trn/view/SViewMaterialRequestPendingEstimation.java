/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SMaterialRequestUtils;
import erp.mod.trn.form.SDialogMaterialRequestEstimation;
import erp.mod.trn.form.SDialogMaterialRequestEstimationKardex;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
 * @author Edwin Carmona
 */
public class SViewMaterialRequestPendingEstimation extends SGridPaneView implements ActionListener {

    private JButton jbPrint;
    private JButton mjbToSupply;
    private JButton mjbToEstimate;
    private JButton mjbEstimationKardex;
    private JButton mjbToSearch;
    private JButton mjbCleanSearch;
    //private JButton mjbClose;
    //private JButton mjbOpen;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SDialogMaterialRequestEstimation moDialogEstimate;
    private SDialogMaterialRequestEstimationKardex moDialogEstimationKardex;
    private boolean mbHasAdmRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_REV).HasRight;
    private String msSeekQueryText;
    private JTextField moTextToSearch;
    
    /**
     * @param client GUI client.
     * @param type
     * @param subtype
     * @param title View's GUI tab title.
     * @param params
     */
    public SViewMaterialRequestPendingEstimation(SGuiClient client, int type, int subtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, type, subtype, title, params);
        initComponents();
    }
    
    private void initComponents() {
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir", this);
        mjbToSupply = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_left.gif")), "Regresar a suministro", this);
        mjbToEstimate = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money_out.gif")), "Cotizar requisición", this);
        mjbEstimationKardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver solicitudes de cotización", this);
        mjbToSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif")), "Buscar", this);
        mjbCleanSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif")), "Limpiar búsqueda", this);
        mbHasAdmRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_ADMOR).HasRight;
        
        jbRowNew.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowCopy.setEnabled(false);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToSupply);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToEstimate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbEstimationKardex);
        moTextToSearch = new JTextField("");
        moTextToSearch.setPreferredSize(new Dimension(150, 23));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moTextToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCleanSearch);
        
        jbPrint.setEnabled(true);
        mjbToSupply.setEnabled(true);
        mjbToEstimate.setEnabled(true);
        mjbEstimationKardex.setEnabled(mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_ESTIMATED);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_ESTIMATE) {
            moFilterDatePeriod.initFilter(null);
        }
        else {
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        }
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        msSeekQueryText = "";
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
    
    private void actionToSupply() {
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
                if (miClient.showMsgBoxConfirm("¿Esta seguro/a de regresar la requisición a suministro?") == JOptionPane.OK_OPTION) {
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    String message = SMaterialRequestUtils.updateStatusOfMaterialRequest(miClient.getSession(), new int[] { key[0] }, SModSysConsts.TRNS_ST_MAT_REQ_PROV);
                    if (! message.isEmpty()) {
                        miClient.showMsgBoxError(message);
                    }

                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }
    
    private void actionEstRequestKardex() {
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
                moDialogEstimationKardex = new SDialogMaterialRequestEstimationKardex(miClient, "Solicitudes de cotización");
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                moDialogEstimationKardex.setValue(SModConsts.TRN_MAT_REQ, key);
                moDialogEstimationKardex.setVisible(true);
            }
        }
    }
    
    private void actionToEstimate() {
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
                moDialogEstimate = new SDialogMaterialRequestEstimation(miClient, "Cotizar requisición de materiales");
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                moDialogEstimate.setValue(SModConsts.TRN_MAT_REQ, new int[] { key[0] });
                moDialogEstimate.setVisible(true);

                miClient.getSession().notifySuscriptors(mnGridType);
            }
        }
    }
    
    private void actionSearch() {
        if (jtTable.getRowCount() > 1) {
            String text = moTextToSearch.getText().trim();
            if (text.length() > 0) {
                msSeekQueryText = "(LPAD(v.num, 6, 0) LIKE '%" + text + "%' OR "
                        + "v.dt LIKE '%" + text + "%' OR "
                        + "ur.usr LIKE '%" + text + "%' OR "
                        + "ref LIKE '%" + text + "%' OR "
                        + "bmu.bp LIKE '%" + text + "%' OR "
                        + "smr.name LIKE '%" + text + "%' ";
                
                msSeekQueryText += "OR i.item LIKE '%" + text + "%' "
                        + "OR i.item_key LIKE '%" + text + "%' "
                        + "OR i.part_num LIKE '%" + text + "%' "
                        + "OR u.unit LIKE '%" + text + "%' ";
                
                msSeekQueryText += ") ";
            }
            else {
                msSeekQueryText = "";
            }

            actionGridReload();
        }
    }
    
    private void actionCleanSearch() {
        moTextToSearch.setText("");
        msSeekQueryText = "";
        actionGridReload();
    }
    
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        String join = "";
        String groupOrderBy = "";
        String select = "";
        Object filter;
        int usrId = miClient.getSession().getUser().getPkUserId();

        moPaneSettings = new SGridPaneSettings(2);

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
        
        where += (where.isEmpty() ? "" : "AND ") + (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PEND_ESTIMATE ? "mre.qty IS NULL " : "mre.qty IS NOT NULL ");

        where += (where.isEmpty() ? "" : "AND ") + "(cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST
                + ", '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, NULL, NULL, NULL, NULL) = "
                + SAuthorizationUtils.AUTH_STATUS_AUTHORIZED + " "
                + "OR cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST
                + ", '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, NULL, NULL, NULL, NULL) = "
                + SAuthorizationUtils.AUTH_STATUS_NA + ") ";

        select = "SUM(ve.qty) AS org_qty, i.item, i.item_key, i.part_num, u.unit, ve.id_ety, "
                + "rpe.name AS ety_pty, ve.dt_req_n, ";
        join += "INNER JOIN erp.itmu_item AS i ON ve.fk_item = i.id_item "
                + "INNER JOIN erp.itmu_unit AS u ON ve.fk_unit = u.id_unit "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rpe ON ve.fk_mat_req_pty_n = rpe.id_mat_req_pty ";
        groupOrderBy = "ve.id_mat_req, ve.id_ety ";

        where += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + " AND NOT v.b_clo_pur  ";

        if (usrId != 2 || !mbHasAdmRight) { // SUPER
            join += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu ON "
                    + "pe.id_mat_prov_ent = peu.id_mat_prov_ent ";
            where += (where.isEmpty() ? "" : "AND ") + "peu.id_usr = " + usrId + " ";
        }

        if (msSeekQueryText.length() > 0) {
            where += (where.isEmpty() ? "" : "AND ") + msSeekQueryText;
        }

        msSql = "SELECT v.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "ve.id_ety AS " + SDbConsts.FIELD_ID + "2,"
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "LPAD(v.num, 6, 0) AS folio, "
                + "v.dt_req_n, "
                + "v.ref, "
                + "v.b_ext_sys, "
                + "v.b_clo_prov, "
                + select
                + "NOT ISNULL(mre.qty) AS is_estimated, "
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
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS ve ON "
                + "v.id_mat_req = ve.id_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "v.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rp ON "
                + "v.fk_mat_req_pty = rp.id_mat_req_pty "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " AS smr ON "
                + "v.fk_st_mat_req = smr.id_st_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "v.fk_usr_req = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_ETY) + " AS mre ON "
                + "ve.id_mat_req = mre.fk_mat_req_n AND ve.id_ety = fk_mat_req_ety_n "
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
                + "v.fk_usr_upd = uu.id_usr ";
        msSql += join
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "GROUP BY " + groupOrderBy + " "
                + "ORDER BY " + groupOrderBy;
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "prov_ent", "Centro Suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio", "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_req", "Solicitante"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "id_ety", "Número partida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "ety_pty", "Prioridad partida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "ve.dt_req_n", "Fecha requerida partida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "is_estimated", "Cotizada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "i.item_key", "Clave"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", "Ítem"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "i.part_num", "# parte"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "org_qty", "Cant. requerida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "unit", "unidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ent_cons", "Centro consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "s_ent_cons", "Subcentro consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cc", "Centro de costo"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
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
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRNX_MAT_REQ_PEND_SUP);
        moSuscriptionsSet.add(SModConsts.TRNX_MAT_REQ_PEND_PUR);
        moSuscriptionsSet.add(SModConsts.TRNX_MAT_REQ_EST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == mjbToSupply) {
                actionToSupply();
            }
            else if (button == mjbToEstimate) {
                actionToEstimate();
            }
            else if (button == mjbEstimationKardex) {
                actionEstRequestKardex();
            }
            else if (button == mjbToSearch) {
                actionSearch();
            }
            else if (button == mjbCleanSearch) {
                actionCleanSearch();
            }
        }
    }
}
