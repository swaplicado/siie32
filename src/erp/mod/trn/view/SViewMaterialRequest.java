/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelMatReqStatus;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.form.SDialogAuthorizationCardex;
import erp.mod.trn.form.SDialogMaterialRequestSegregation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
public class SViewMaterialRequest extends SGridPaneView implements ActionListener {

    private JButton jbPrint;
    private JButton jbAuthCardex;
    private JButton jbAuthorize;
    private JButton jbReject;
    private JButton jbSegregate;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterPanelMatReqStatus moFilterMatReqStatus;
    private SDialogAuthorizationCardex moDialogAuthCardex;
    private SDialogMaterialRequestSegregation moDialogSegregations;
    
    private boolean hasAuthRight;
    
    /**
     * @param client GUI client.
     * @param subType
     * @param title View's GUI tab title.
     * @param params
     */
    public SViewMaterialRequest(SGuiClient client, int subType, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_MAT_REQ, subType, title, params);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(true);
        
        hasAuthRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_REV).HasRight;
        
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir", this);
        jbAuthCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Kardex de autorizaciones", this);
        jbAuthorize = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_up.gif")), "Autorizar", this);
        jbReject = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_down.gif")), "Rechazar", this);
        jbSegregate = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")), "Apartar/Liberar", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_LEFT).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthorize);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbReject);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSegregate);
        
        jbPrint.setEnabled(true);
        jbAuthCardex.setEnabled(true);
        jbAuthorize.setEnabled(hasAuthRight);
        jbReject.setEnabled(hasAuthRight);
        jbSegregate.setEnabled(hasAuthRight);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterMatReqStatus = new SGridFilterPanelMatReqStatus(miClient, this);
        moFilterMatReqStatus.initFilter(mnGridMode);
        
        if (mnGridMode == SLibConsts.UNDEFINED) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterMatReqStatus);
        
        moDialogAuthCardex = new SDialogAuthorizationCardex(miClient, "Cardex de autorizaciones");
        moDialogSegregations = new SDialogMaterialRequestSegregation(miClient, "Apartados de la requisición");
        
        if (mnGridMode != SModSysConsts.TRNS_ST_MAT_REQ_NEW) {
            jbRowNew.setEnabled(false);
            jbRowCopy.setEnabled(false);
            jbRowDisable.setEnabled(false);
            jbRowDelete.setEnabled(false);
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
    
    private void actionCardex() {
        int[] key;
        
        if (jbAuthCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        key = (int[]) gridRow.getRowPrimaryKey();
                    
                        moDialogAuthCardex.setFormParams(SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ), key);
                        moDialogAuthCardex.setVisible(true);
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger(SViewMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void actionAuthorizeOrRejectResource(final int iAction) {
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
                    SDbMaterialRequest req = new SDbMaterialRequest();
                    req.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                    String response = SAuthorizationUtils.authorizeOrReject(miClient.getSession(), 
                                                                    req.getPrimaryKey(), 
                                                                    SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, 
                                                                    iAction);
                    if (response.length() > 0) {
                            miClient.showMsgBoxError(response);
                    }
                    else {
                        miClient.showMsgBoxInformation((iAction == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE ? "Autorizado" : "Rechazado") + 
                                " con éxito");
                        req.save(miClient.getSession());
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionSegregateOrRelease() {
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
                    
                    moDialogSegregations.setFormParams(key);
                    moDialogSegregations.setVisible(true);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
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
    
    @Override
    public void prepareSqlQuery() {
        String select = "";
        String where = "";
        String join = "";
        boolean needJoin = false;
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

        switch (mnGridMode) {
            case SModSysConsts.TRNS_ST_MAT_REQ_NEW:
                where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_NEW + " ";
                if (usrId != 2 ) { // SUPER
                    needJoin = true;
                    where += (where.isEmpty() ? "" : "AND ") + "(v.fk_usr_req = " + usrId + " OR v.ts_usr_ins = " + usrId + " "
                            + "OR (ceu.id_link = " + SModSysConsts.USRS_LINK_USR + " AND ceu.id_ref = " + usrId + ")) ";
                }
                break;
            case SModSysConsts.TRNS_ST_MAT_REQ_AUTH:
                join += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS aut ON "
                        + "v.id_mat_req = aut.res_pk_n1_n ";
                where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_AUTH + " ";
                break;
            case SModSysConsts.TRNX_MAT_REQ_AUTHO_RECH:
                select += "uaut.usr AS autorizo, urej.usr AS rechazo, ";
                join += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS aut ON "
                        + "v.id_mat_req = aut.res_pk_n1_n "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uaut ON " 
                        + "aut.fk_usr_authorn_n = uaut.id_usr "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS urej ON " 
                        + "aut.fk_usr_reject_n = urej.id_usr ";
                where += (where.isEmpty() ? "" : "AND ") + "(aut.fk_usr_authorn_n IS NOT NULL OR aut.fk_usr_reject_n IS NOT NULL) AND aut.fk_usr_step = " + usrId + " ";
                break;
            case SModSysConsts.TRNS_ST_MAT_REQ_PROV:
                where += (where.isEmpty() ? "" : "AND ") + "(v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " OR v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + ") " ;
                break;
            default:
                filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.TRNS_ST_MAT_REQ)).getValue();
                if (filter != null && ((int[]) filter).length == 1) {
                    where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + ((int[]) filter)[0] + " ";
                }
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
            break;
        }
        
        join += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu ON "
                + "v.fk_mat_prov_ent = peu.id_mat_prov_ent AND peu.id_usr = " + usrId + " " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_CC) + " AS mrc ON " 
                + "v.id_mat_req = mrc.id_mat_req "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " AS ceu ON "
                + "mrc.id_mat_ent_cons_ent = ceu.id_mat_cons_ent AND ceu.id_link = " + SModSysConsts.USRS_LINK_USR + " AND ceu.id_ref = " + usrId + " ";
        
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_REV) {
            if (usrId != 2 ) { // SUPER
                needJoin = true;
                where += (where.isEmpty() ? "" : "AND ") + "(v.fk_usr_req = " + usrId + " OR v.ts_usr_ins = " + usrId + " "
                        + "OR (ceu.id_link = " + SModSysConsts.USRS_LINK_USR + " AND ceu.id_ref = " + usrId + ") OR aut.fk_usr_step = " + usrId + ") ";
            }
        }
        
        msSql = "SELECT DISTINCT v.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.num AS folio, "
                + "v.dt_req_n, "
                + "v.ref, "
                + "v.b_ext_sys, "
                + "v.b_clo_prov, "
                + "v.b_clo_pur, "
                + "pe.code AS prov_ent, "
                + "rp.name AS req_pty, "
                + "smr.name AS req_status, "
                + "smp.name AS sum_status, "
                + "smpu.name AS comp_status, "
                + "ur.usr AS usr_req, "
                + "bmu.bp AS contractor, "
                + select
                + "CASE "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_AUTHORIZED + " THEN 'AUTORIZADO' "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_REJECTED + " THEN 'RECHAZADO' "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_PENDING + " THEN 'PENDIENTE' "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_IN_PROCESS + " THEN 'EN PROCESO' "
                    + "ELSE '(NO APLICA)' "
                + "END AS auth_status, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_clo_prov, "
                + "v.fk_usr_clo_pur, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_clo_prov, "
                + "v.ts_usr_clo_pur, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS usr_clo, "
                + "ucp.usr AS usr_pur, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "v.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rp ON "
                + "v.fk_mat_req_pty = rp.id_mat_req_pty "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " AS smr ON "
                + "v.fk_st_mat_req = smr.id_st_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PROV) + " AS smp ON "
                + "v.fk_st_mat_prov = smp.id_st_mat_prov "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PUR) + " AS smpu ON "
                + "v.fk_st_mat_pur = smpu.id_st_mat_pur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "v.fk_usr_req = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS mu ON "
                + "v.fk_contractor_n = mu.id_maint_user "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bmu ON "
                + "mu.id_maint_user = bmu.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "v.fk_usr_clo_prov = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ucp ON "
                + "v.fk_usr_clo_pur = ucp.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (needJoin ? join : "")
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.dt, v.num ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "prov_ent", "Cen suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio", "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_req", "Solicitante"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "contractor", "Contratista"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ref", "Referencia"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_req_n", "Fecha requerida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "req_pty", "Prioridad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "req_status", "Estatus"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "auth_status", "Autorización"));
        if (mnGridMode == SModSysConsts.TRNX_MAT_REQ_AUTHO_RECH) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "autorizo", "Autorizó"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "rechazo", "Rechazó"));
        }
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "sum_status", "Suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "comp_status", "Compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_L, "b_clo_prov", "Terminado suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_clo", "Usr terminado suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_clo_prov", "Usr TS terminado suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_L, "b_clo_pur", "Terminado compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_clo", "Usr terminado compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_clo_prov", "Usr TS terminado compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbAuthCardex) {
                actionCardex();
            }
            else if (button == jbAuthorize) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_AUTHORIZE);
            }
            else if (button == jbReject) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_REJECT);
            }
            else if (button == jbSegregate) {
                actionSegregateOrRelease();
            }
        }
    }
}
