/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.view;

import erp.mod.SModConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SViewAuthorizations extends SGridPaneView implements ActionListener {
    
    private JButton jbAuthorize;
    private JButton jbReject;

    public SViewAuthorizations(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.CFGU_AUTHORN_STEP, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, false, false, false, false);
        
        jbAuthorize = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_up.gif")), "Autorizar", this);
        jbReject = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_down.gif")), "Rechazar", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthorize);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbReject);
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
                        String response = "Error";
                        if (iAction == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE) {
                            response = SAuthorizationUtils.authorizeById(miClient.getSession(), gridRow.getRowPrimaryKey()[0]);
                        }
                        else {
                            response = SAuthorizationUtils.rejectById(miClient.getSession(), gridRow.getRowPrimaryKey()[0], "");
                        }
                        
                        if (response.length() > 0) {
                            miClient.showMsgBoxError(response);
                        }
                        else {
                            miClient.showMsgBoxInformation("Documento " + 
                                    (iAction == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE ? "autorizado" : "rechazado") + 
                                    " con éxito");
                            miClient.getSession().notifySuscriptors(mnGridType);
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
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " +
                    "    CASE " +
                    "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + " THEN " +
                    "             (SELECT LPAD(tmq.num, 6, '0') AS num_req " +
                    "                FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS tmq " +
                    "               WHERE tmq.id_mat_req = v.res_pk_n1_n) " +
                    "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_DPS + " THEN " +
                    "            (SELECT  " +
                    "                    CONCAT(t.dt_doc, ' ', t.num_ser, ' ', t.num, ' ', t.num_ref) " +
                    "                FROM " +
                    "                    trn_dps AS t " +
                    "                WHERE " +
                    "                    t.id_year =  v.res_pk_n1_n " +
                    "                        AND t.id_doc =  v.res_pk_n2_n) " +
                    "        ELSE '---' " +
                    "END AS doc_reference, " +
                    "v.id_authorn_step AS " + SDbConsts.FIELD_ID + "1, " +
                    "'' AS " + SDbConsts.FIELD_CODE + ", " +
                    "cta.name AS " + SDbConsts.FIELD_NAME + ", " +
                    "v.*, " +
                    "IF(v.b_authorn, " + SGridConsts.ICON_THUMBS_UP + ", IF(v.b_reject, " + SGridConsts.ICON_THUMBS_DOWN + ", " + SGridConsts.ICON_NULL + ")) AS f_ico_auth_st, " +
                    "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " + 
                    "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", " +
                    "ua.usr AS auth_user, " +
                    "v.dt_time_authorn_n, " +
                    "ur.usr AS rej_user, " +
                    "v.dt_time_reject_n, " +
                    "us.usr AS step_user, " +
                    "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " +
                    "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " +
                    "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " +
                    "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " +
                    "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", " +
                    "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS v " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGS_TP_AUTHORN) + " AS cta ON " +
                    "v.fk_tp_authorn = cta.id_tp_authorn " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON " +
                    "v.fk_usr_ins = ui.id_usr " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON " +
                    "v.fk_usr_upd = uu.id_usr " +
                    "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON " +
                    "v.fk_usr_authorn_n = ua.id_usr " +
                    "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON " +
                    "v.fk_usr_reject_n = ur.id_usr " +
                    "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON " +
                    "v.fk_usr_step = us.id_usr " +
                    "WHERE " +
                    "NOT v.b_del " +
                    "ORDER BY v.fk_tp_authorn ASC, v.lev ASC";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "doc_reference", "Referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "f_ico_auth_st", "Status autorización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "lev", "Nivel"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "dt_time_authorn_n", "Autorizado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "auth_user", "Autorizado por"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "dt_time_reject_n", "Rechazado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "rej_user", "Rechazado por"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "step_user", "Corresponde a"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbAuthorize) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_AUTHORIZE);
            }
            else if (button == jbReject) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_REJECT);
            }
        }
    }
}
