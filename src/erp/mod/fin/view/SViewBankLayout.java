/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.mfin.data.SFinUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinConsts;
import erp.mod.fin.form.SDialogLayoutsBankCardex;
import erp.mod.fin.util.SFinUtils;
import erp.mod.fin.util.STreasuryBankLayoutRequest;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas, Uriel Castañeda
 */
public class SViewBankLayout extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton jbPayment;
    private JButton jbGetLayout;
    private JButton jbSend;
    private JButton jbBackToNew;
    private JButton jbCardex;
    private SDialogLayoutsBankCardex moDialogLayoutsCardex;
    
    public SViewBankLayout(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_LAY_BANK, gridSubtype, title);
        setRowButtonsEnabled(true, false, true, false, true);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        
        jbGetLayout = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_type.gif")), "Obtener layout", this);
        jbPayment = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money.gif")), "Aplicar pagos", this);
        jbSend = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar solicitud de autorización", this);
        jbBackToNew = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_left.gif")), "Regresar a estatus anterior", this);
	jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        moDialogLayoutsCardex = new SDialogLayoutsBankCardex(miClient, mnGridSubtype, "Detalle del layout");
    
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetLayout);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPayment);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSend);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbBackToNew);
	
        if (mnGridSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
           getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
        }
    }
    
    @Override
    public void actionRowEdit(boolean showSystemRegistries) {
        SDbBankLayout bankLayout = null;
        
        if (jbRowEdit.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                SGuiParams params = null;

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (!showSystemRegistries && gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else if (moPaneSettings.isUserInsertApplying() && mnUserLevelAccess == SUtilConsts.LEV_AUTHOR && gridRow.getFkUserInsertId() != miClient.getSession().getUser().getPkUserId()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_DENIED_RIGHT);
                }
                else {
                    bankLayout = (SDbBankLayout) miClient.getSession().readRegistry(SModConsts.FIN_LAY_BANK, gridRow.getRowPrimaryKey(), SDbConsts.MODE_VERBOSE);
                    
                    if (bankLayout.getLayoutStatus() == SFinConsts.LAY_BANK_APPROVED_ST) {
                        miClient.showMsgBoxWarning("El layout no puede ser modificado en estatus " + SFinConsts.LAY_BANK_APPROVED_TEXT_ST);
                    }
                    else if (bankLayout.getDocsPayed() > 0) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_DENIED_UPDATE + "\n¡Existen documentos con pagos aplicados!");
                    }
                    else if (bankLayout.getTransfersPayed() > 0) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_DENIED_UPDATE + "\n¡Existen transferencias con pagos aplicados!");
                    }
                    else {
                        params = moFormParams != null ? moFormParams : new SGuiParams();
                        params.setKey(gridRow.getRowPrimaryKey());

                        miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(mnGridType, mnGridSubtype, params);
                        moFormParams = null;
                    }
                }
            }
        }
    }
    
    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            }
            else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                SGridRow[] gridRows = getSelectedGridRows();

                for (SGridRow gridRow : gridRows) {
                    if (((SGridRowView) gridRow).getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (((SGridRowView) gridRow).isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!((SGridRowView) gridRow).isDeletable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                    }
                    else {
                        if (miClient.getSession().getModule(mnModuleType, mnModuleSubtype).deleteRegistry(mnGridType, gridRow.getRowPrimaryKey()) == SDbConsts.SAVE_OK) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                }
            }
        }
    }
    
    private void actionApplyPayments() {
        if (jbPayment.isEnabled()) {
            if (validateRow()) {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(mnGridType, SModConsts.FIN_REC, new SGuiParams(gridRow.getRowPrimaryKey()));
                }
            }     
        }
    }
    
    private boolean validateRow() {
        boolean valid = true;
       
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            valid = false;
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                valid = false;
            }
            else if (gridRow.isRowSystem()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                valid = false;
            }
        }
        
        return valid;
    }
    
    private SDbBankLayout readLayout() {
        SDbBankLayout bankLayout = null;
        
        if (jbGetLayout.isEnabled()) {
            if (validateRow()) {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                bankLayout = (SDbBankLayout) miClient.getSession().readRegistry(SModConsts.FIN_LAY_BANK, gridRow.getRowPrimaryKey());
            }
        }
        
        return bankLayout;
    }
    
    private void actionGetLayout() {
        SDbBankLayout bankLayout = null;
        
        bankLayout = readLayout();
        if (bankLayout != null) {
            SFinUtilities.writeLayout((SClientInterface) miClient, bankLayout.getLayoutText(), "");
        }
    }
    
    private void actionSend() {
        boolean done = false;
        SDbBankLayout bankLayout = null;
        STreasuryBankLayoutRequest treasuryBankLayoutRequest = null;
        
        bankLayout = readLayout();
        
        if (bankLayout != null) {
            bankLayout.setAuxLayoutType(mnGridSubtype == SModSysConsts.FIN_LAY_BANK_DPS ? "TRANSFERENCIAS" : "ANTICIPOS");
            treasuryBankLayoutRequest = new STreasuryBankLayoutRequest(miClient, bankLayout);
            done = treasuryBankLayoutRequest.makeRequestToTreasury();
            
            if (done) {
                miClient.showMsgBoxInformation("La solicitud fue enviada.");
                miClient.getSession().notifySuscriptors(mnGridType);
            }
        }
        else {
            miClient.showMsgBoxWarning("Error al leer el layout");
        }
    }
    
    private void actionBack() {
        SDbBankLayout bankLayout = null;
        
        bankLayout = readLayout();
        if (bankLayout != null) {
            try {
                if (bankLayout.getLayoutStatus() == SFinConsts.LAY_BANK_APPROVED_ST) {
                    SFinUtils.changeLayoutStatus(miClient, bankLayout, SFinConsts.LAY_BANK_NEW_ST);
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
        }
        else {
            miClient.showMsgBoxWarning("Error al leer el layout");
        }
    }
    
    private void actionShowCardex() {
        if (jbCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moDialogLayoutsCardex.setFormParams(gridRow.getRowPrimaryKey());
                    moDialogLayoutsCardex.setVisible(true);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "l.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("l.dt_lay", (SGuiDate) filter);

        msSql = "SELECT "
                + "l.id_lay_bank AS " + SDbConsts.FIELD_ID + "1, "
                + "' ' AS " + SDbConsts.FIELD_CODE + ", "
                + "l.cpt AS " + SDbConsts.FIELD_NAME + ", "
                + "l.dt_lay, l.dt_due, l.cpt, l.con, "
                + "l.amt, l.amt_pay, (l.amt - l.amt_pay) AS f_amt_x_pay, "
                + "l.tra, l.tra_pay, (l.tra - l.tra_pay) AS f_tra_x_pay, "
                + "l.dps, l.dps_pay, (l.dps - l.dps_pay) AS f_doc_x_pay, "
                + "IF (l.lay_st = "+ SFinConsts.LAY_BANK_APPROVED_ST + ", '" + SFinConsts.LAY_BANK_APPROVED_TEXT_ST + "', '" + SFinConsts.LAY_BANK_NEW_TEXT_ST + "') AS status, l.auth_req, "
                + "tp.tp_lay_bank AS f_tp_lay, "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_HSBC + ", '" + SFinConsts.TXT_LAY_BANK_HSBC + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_SANTANDER + ", '" + SFinConsts.TXT_LAY_BANK_SANTANDER + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_BANBAJIO + ", '" + SFinConsts.TXT_LAY_BANK_BANBAJIO + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_BBVA + ", '" + SFinConsts.TXT_LAY_BANK_BBVA + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_BANAMEX + ", '" + SFinConsts.TXT_LAY_BANK_BANAMEX + "', "
                + "'" + SCfdConsts.UNIDENTIFIED + "'))))) AS f_lay, "
                + "CONCAT(e.ent, ' (', e.code, ')') AS f_ent, l.b_del, "
                + "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE a.fid_cur = c.id_cur) AS f_cur_key, "
                + "l.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "l.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "l.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "l.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "l.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK) + " AS l "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINU_TP_LAY_BANK) + " AS tp ON "
                + "l.fk_tp_lay_bank = tp.id_tp_lay_bank "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS a ON "
                + "a.id_cob = l.fk_bank_cob AND a.id_acc_cash = l.fk_bank_acc_cash "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS e ON "
                + "a.id_cob = e.id_cob AND a.id_acc_cash = e.id_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "l.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "l.fk_usr_upd = uu.id_usr "
                + "WHERE l.trn_tp = " + (mnGridSubtype == SModSysConsts.FIN_LAY_BANK_DPS ? SFinConsts.LAY_BANK_TYPE_DPS : SFinConsts.LAY_BANK_TYPE_ADV) + (sql.length() == 0 ? "" : " AND " + sql) + " "
                + "ORDER BY l.dt_lay, l.dt_due, l.id_lay_bank ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "l.dt_lay", "Pago"));
	gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, SDbConsts.FIELD_ID + "1", "Folio", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "status", "Estatus"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_tp_lay", "Tipo layout", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_ent", "Cuenta bancaria", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "l.cpt", "Concepto/Descripción", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "l.con", "Consecutivo día", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "l.amt", "Monto $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "l.amt_pay", "Monto pagado $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_amt_x_pay", "Monto x pagar $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur_key", "Moneda"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "l.tra", "Transferencias", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "l.tra_pay", "Transferencias pagadas", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "f_tra_x_pay", "Transferencias x pagar", 100));
        if (mnGridSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "l.dps", "Documentos", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "l.dps_pay", "Documentos pagados", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "f_doc_x_pay", "Documentos x pagar", 100));            
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbGetLayout) {
                actionGetLayout();
            }
            else if (button == jbPayment) {
                actionApplyPayments();
            }
            else if (button == jbSend) {
                actionSend();
            }
            else if (button == jbBackToNew) {
                actionBack();
            }
            else if (button == jbCardex) {
                actionShowCardex();
            }
        }
    }
}
