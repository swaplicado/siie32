/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.cfd.SCfdConsts;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas, Uriel Castañeda, Alfredo Pérez
 */
public class SViewBankLayoutPayments extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton jbPayment;
    private JButton jbCloseLayout;
    
    public SViewBankLayoutPayments(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_LAY_BANK, gridSubtype, title, params);
        setRowButtonsEnabled(false);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        if (mnGridMode == SModConsts.VIEW_ST_DONE) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        jbPayment = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money.gif")), "Aplicar pagos", this);
        jbCloseLayout = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), (mnGridMode == SModConsts.VIEW_ST_PEND ? "Cerrar" : "Abrir") + " layout para pago", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPayment);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCloseLayout);
    }
    
    private void actionApplyPayments() {
        if (jbPayment.isEnabled()) {
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
                    miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(mnGridType, SModSysConsts.FIN_LAY_BANK_ACC, new SGuiParams(gridRow.getRowPrimaryKey()));
                }
            }
        }
    }
    
    private void actionCloseLayout() {
        boolean canClose = true;
        SDbBankLayout bankLayout = null;
        
        if (jbCloseLayout.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    bankLayout = (SDbBankLayout) miClient.getSession().readRegistry(SModConsts.FIN_LAY_BANK, gridRow.getRowPrimaryKey());
                    if (miClient.showMsgBoxConfirm("Está por " + (mnGridMode == SModConsts.VIEW_ST_PEND ? "cerrar" : "abrir") + " el layout para pago.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        if (mnGridMode == SModConsts.VIEW_ST_DONE) {
                            if (!bankLayout.isClosedPayment()) {
                                miClient.showMsgBoxWarning("¡El layout no se puede abrir, no fué cerrado de forma manual!");
                                canClose = false;
                            }
                        }
                        
                        if (canClose) {
                            bankLayout.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbBankLayout.FIELD_CLOSE, !bankLayout.isClosedPayment());
                            bankLayout.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbBankLayout.FIELD_CLOSE_USER, miClient.getSession().getUser().getPkUserId());
                            miClient.getSession().notifySuscriptors(mnGridType);
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
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "l.b_del = 0 ";
        }
        
        if (mnGridMode == SModConsts.VIEW_ST_DONE) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("l.dt_lay", (SGuiDate) filter);
        }

        msSql = "SELECT "
                + "l.id_lay_bank AS " + SDbConsts.FIELD_ID + "1, "
                + "' ' AS " + SDbConsts.FIELD_CODE + ", "
                + "l.cpt AS " + SDbConsts.FIELD_NAME + ", "
                + "l.dt_lay, l.dt_due, l.cpt, l.con, "
                + "l.amt, l.amt_pay, (l.amt - l.amt_pay) AS f_amt_x_pay, "
                + "l.tra, l.tra_pay, (l.tra - l.tra_pay) AS f_tra_x_pay, "
                + "tp.tp_lay_bank AS f_tp_lay, "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_HSBC + ", '" + SFinConsts.TXT_LAY_BANK_HSBC + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_SANTANDER + ", '" + SFinConsts.TXT_LAY_BANK_SANTANDER + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_BANBAJIO + ", '" + SFinConsts.TXT_LAY_BANK_BANBAJIO + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_BBVA + ", '" + SFinConsts.TXT_LAY_BANK_BBVA + "', "
                + "IF(tp.lay_bank = " + SFinConsts.LAY_BANK_BANAMEX + ", '" + SFinConsts.TXT_LAY_BANK_BANAMEX + "', "
                + "'" + SCfdConsts.UNIDENTIFIED + "'))))) AS f_lay, "
                + "CONCAT(e.ent, ' (', e.code, ')') AS f_ent, l.b_del, "
                + "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE a.fid_cur = c.id_cur) AS f_cur_key, l.b_clo_pay, "
                + "l.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "l.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "l.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "l.fk_usr_upd AS " + SDbConsts.FIELD_USER_USR_ID + ", "
                + "l.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "l.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "l.ts_usr_upd AS " + SDbConsts.FIELD_USER_USR_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "uc.usr AS " + SDbConsts.FIELD_USER_USR_NAME + " "
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
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "l.fk_usr_clo_pay = uc.id_usr "
                + "WHERE l.trn_tp = " + (mnGridSubtype == SModSysConsts.FIN_LAY_BANK_DPS ? SFinConsts.LAY_BANK_TYPE_DPS : SFinConsts.LAY_BANK_TYPE_ADV) + (sql.length() == 0 ? "" : " AND " + sql) + " "
                //+ (sql.length() == 0 ? "" : "WHERE " + sql)
                + (mnGridMode == SModConsts.VIEW_ST_PEND ? "HAVING f_tra_x_pay <> 0 AND l.b_clo_pay = 0 " : "HAVING f_tra_x_pay = 0 OR l.b_clo_pay = 1 ")
                + "ORDER BY l.dt_lay, l.dt_due, l.id_lay_bank ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "l.dt_lay", "Pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, SDbConsts.FIELD_ID + "1", "Folio", 50));
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
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "l.b_clo_pay", "Cerrado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_USR_NAME, SGridConsts.COL_TITLE_USER_USR_NAME + " cerrado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_USR_TS, SGridConsts.COL_TITLE_USER_USR_TS + " cerrado"));
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

            if (button == jbPayment) {
                actionApplyPayments();
            }
            else if (button == jbCloseLayout) {
                actionCloseLayout();
            }
        }
    }
}
